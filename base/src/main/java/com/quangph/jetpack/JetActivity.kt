package com.quangph.jetpack

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.quangph.base.common.ActivityNavi
import com.quangph.base.mvp.IView
import com.quangph.base.mvp.mvpcomponent.KMVPActivity
import com.quangph.jetpack.perm.PermsRequest
import com.quangph.base.tracking.ITrackerConfig
import com.quangph.base.tracking.ITrackingFactory
import com.quangph.jetpack.alert.HeaderAlertDefault
import com.quangph.jetpack.alert.IAlert
import com.quangph.jetpack.loading.ILoadingDialogController
import com.quangph.jetpack.loading.LoadingDialogController
import com.quangph.jetpack.navigation.ActivityNaviSource
import com.quangph.jetpack.navigation.JetNavi
import com.quangph.base.roadmap.ROAD_DIRECTION
import com.quangph.base.roadmap.RoadMap
import com.quangph.jetpack.error.INetworkError
import com.quangph.jetpack.error.NetworkErrorImpl

/**
 * Created by QuangPH on 2020-11-25.
 */
open class JetActivity<V : IView>: KMVPActivity<V>(), IJetContext, IJetStackable {

    private var loadingController: ILoadingDialogController? = null
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var networkError: INetworkError? = null
    private var alert: IAlert? = null
    private var trackerFactory: ITrackingFactory? = null
    private var jetNavi: JetNavi? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        loadingController?.enable(true)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (PermsRequest.hasRequestPerm()) {
            PermsRequest.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onResume() {
        super.onResume()
        loadingController?.enable(true)
        if (hasNetworkDetection()) {
            if (networkCallback == null) {
                networkCallback = initNetworkCallback()
            }
            val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback!!)
        }
        RoadMap.log(this)
    }

    override fun onPause() {
        super.onPause()
        if (hasNetworkDetection()) {
            if (networkCallback != null) {
                val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                cm.unregisterNetworkCallback(networkCallback!!)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        loadingController?.enable(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        PermsRequest.release()
        if (loadingController != null) {
            loadingController = null
        }

        if (networkCallback != null) {
            networkCallback = null
        }

        if (alert != null) {
            alert?.destroy()
            alert = null
        }

        if (jetNavi != null) {
            jetNavi = null
        }
    }

    override fun finish() {
        super.finish()
        RoadMap.finish(this)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        RoadMap.traceDirection(ROAD_DIRECTION.FORWARD)
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        RoadMap.traceDirection(ROAD_DIRECTION.FORWARD)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        RoadMap.saveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        RoadMap.restoreInstanceState(savedInstanceState)
    }

    override fun onViewDidCreated(savedInstanceState: Bundle?) {
        super.onViewDidCreated(savedInstanceState)
        initLoadingDialogControllerIfNeed()
        networkError = getNetworkError()
    }

    override fun showLoading() {
        loadingController?.show()
    }

    override fun showLoading(tag: String?) {
        tag?.let {
            loadingController?.show(it)
        }
    }

    override fun hideLoading() {
        loadingController?.hide()
    }

    override fun hideLoading(tag: String?) {
        tag?.let {
            loadingController?.hide(it)
        }
    }

    override fun getActivityContext(): Activity {
        return this
    }

    override fun getScreenName(): String {
        return javaClass.name
    }

    override fun showAlert(msg: String?, type: String) {
        if (alert == null) {
            alert = onInitAlert()
        }
        alert?.show(msg, type)
    }

    override fun requestFragmentManager(): FragmentManager {
        return supportFragmentManager
    }

    override fun <ACTIVITY : Activity?> navigate(
        clazz: Class<ACTIVITY>?,
        data: IScreenData?,
        callback: ActivityNavi.OnActivityResult<ActivityResult>?
    ) {
        val itn = Intent(this, clazz)
        if (data != null) {
            itn.putExtra(data.javaClass.name, data)
        }
        launch(itn, callback)
    }

    override fun allowPushToStack(): Boolean {
        return false
    }

    open fun hasLoading(): Boolean {
        return true
    }

    /**
     * Allow to show network error msg
     */
    open fun getNetworkError(): INetworkError? {
        return NetworkErrorImpl
    }

    /**
     * Allow to detect network status
     */
    open fun hasNetworkDetection(): Boolean {
        return false
    }

    open fun onNetworkConnectionTrigger(available: Boolean) {

    }

    open fun onInitAlert(): IAlert {
        return HeaderAlertDefault(this)
    }

    open fun <T : ITrackerConfig?> getTracker(trackerConfigClass: Class<T>?): T {
        if (trackerFactory == null) {
            trackerFactory = JetTrackerFactory(this.application)
        }
        return trackerFactory!!.getTracker(this, trackerConfigClass)
    }

    @Deprecated("Using #navigate instead")
    fun<A: AppCompatActivity> naviTo(dest: Class<A>, argsBuilder: (Intent.() -> Unit)? = null): JetNavi {
        createJetNaviIfNeed()
        return jetNavi!!.naviTo(dest, argsBuilder)
    }

    @Deprecated("Using #navigate instead")
    fun naviTo(intent: Intent): JetNavi {
        createJetNaviIfNeed()
        return jetNavi!!.naviTo(intent)
    }

    private fun initLoadingDialogControllerIfNeed() {
        if (loadingController == null && hasLoading()) {
            loadingController = LoadingDialogController(this)
        }
    }

    private fun initNetworkCallback(): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                postRunnable{
                    networkError?.onNetworkConnected()
                    onNetworkConnectionTrigger(true)
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                postRunnable {
                    networkError?.onNetworkError(this@JetActivity)
                    onNetworkConnectionTrigger(false)
                }
            }
        }
    }

    private fun createJetNaviIfNeed() {
        if (jetNavi == null) {
            jetNavi = JetNavi(ActivityNaviSource(this))
            addLifeCycle(jetNavi)
        }
    }

    inline fun <reified ACTIVITY: Activity> JetActivity<*>.navigate(input: IScreenData?) {
        navigate<ACTIVITY>(input, null)
    }

    inline fun <reified ACTIVITY: Activity> JetActivity<*>.navigate(input: IScreenData?,
                                                              onResult: ActivityNavi.OnActivityResult<ActivityResult>?) {
        val itn = Intent(this, ACTIVITY::class.java)
        if (input != null) {
            itn.putExtra(input.javaClass.name, input)
        }
        launch(itn, onResult)
    }


    inline fun <reified Input: IScreenData> JetActivity<*>.getInput(): Input? {
//        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra(Input::class.java.name, Input::class.java)
//        } else {
//            intent.getParcelableExtra(Input::class.java.name)
//        }

        return intent.getParcelableExtra(Input::class.java.name)
    }

    inline fun <reified Output: IScreenData> Intent.getOutput(): Output? {
//        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
//            return getParcelableExtra(Output::class.java.name, Output::class.java)
//        } else {
//            return getParcelableExtra(Output::class.java.name)
//        }

        return getParcelableExtra(Output::class.java.name)
    }

    inline fun <reified Output: IScreenData> returnData(data: Output): Intent {
        val itn = Intent()
        itn.putExtra(Output::class.java.name, data)
        return itn
    }
}