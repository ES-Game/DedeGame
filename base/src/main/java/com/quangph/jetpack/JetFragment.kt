package com.quangph.jetpack

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.quangph.base.common.ActivityNavi
import com.quangph.base.mvp.IParentPresenter
import com.quangph.base.mvp.IView
import com.quangph.base.mvp.mvpcomponent.MVPFragment
import com.quangph.jetpack.perm.PermsRequest
import com.quangph.base.tracking.ITrackerConfig
import com.quangph.base.tracking.ITrackingFactory
import com.quangph.jetpack.navigation.FragmentNaviSource
import com.quangph.jetpack.navigation.JetNavi

/**
 * Created by QuangPH on 2020-11-26.
 */
open class JetFragment<V: IView>: MVPFragment<V>(), IJetContext {

    private var trackerFactory: ITrackingFactory? = null
    private var jetNavi: JetNavi? = null

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermsRequest.hasRequestPerm()) {
            PermsRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        PermsRequest.release()

        if (jetNavi != null) {
            jetNavi = null
        }
    }

    override fun getParentPresenter(): IParentPresenter? {
        return if (activity is IParentPresenter) {
            activity as IParentPresenter
        } else {
            null
        }
    }

    override fun showLoading() {
        if (activity != null && activity is IJetContext) {
            (activity as IJetContext).showLoading()
        }
    }

    override fun showLoading(tag: String?) {
        if (activity != null && activity is IJetContext) {
            (activity as IJetContext).showLoading(tag)
        }
    }

    override fun hideLoading() {
        if (activity != null && activity is IJetContext) {
            (activity as IJetContext).hideLoading()
        }
    }

    override fun hideLoading(tag: String?) {
        if (activity != null && activity is IJetContext) {
            (activity as IJetContext).hideLoading(tag)
        }
    }

    override fun getActivityContext(): Activity? {
        return activity
    }

    override fun showAlert(msg: String?, type: String?) {
        if (activity != null && activity is IJetContext) {
            (activity as IJetContext).showAlert(msg, type)
        }
    }

    override fun requestFragmentManager(): FragmentManager {
        return childFragmentManager
    }

    override fun <ACTIVITY : Activity?> navigate(
        clazz: Class<ACTIVITY>?,
        data: IScreenData?,
        callback: ActivityNavi.OnActivityResult<ActivityResult>?
    ) {
        val itn = Intent(requireContext(), clazz)
        if (data != null) {
            itn.putExtra(data.javaClass.name, data)
        }
        launch(itn, callback)
    }

    override fun getScreenName(): String {
        return javaClass.name
    }

    open fun <T : ITrackerConfig?> getTracker(trackerConfigClass: Class<T>?): T? {
        if (activity == null) return null
        if (trackerFactory == null) {
            trackerFactory = JetTrackerFactory(requireActivity().application)
        }
        return trackerFactory!!.getTracker(activity, trackerConfigClass)
    }

    @Deprecated("Using #navigate")
    fun<A: AppCompatActivity> naviTo(dest: Class<A>, argsBuilder: (Intent.() -> Unit)? = null): JetNavi {
        createJetNaviIfNeed()
        return jetNavi!!.naviTo(dest, argsBuilder)
    }

    private fun createJetNaviIfNeed() {
        if (jetNavi == null) {
            jetNavi = JetNavi(FragmentNaviSource(this))
            addLifeCycle(jetNavi)
        }
    }

    inline fun <reified ACTIVITY: Activity> Fragment.navigate(input: IScreenData?) {
        navigate<ACTIVITY>(input, null)
    }

    inline fun <reified ACTIVITY: Activity> Fragment.navigate(input: IScreenData?,
                                                              onResult: ActivityNavi.OnActivityResult<ActivityResult>?) {
        val itn = Intent(requireContext(), ACTIVITY::class.java)
        if (input != null) {
            itn.putExtra(input.javaClass.name, input)
        }
        launch(itn, onResult)
    }
}