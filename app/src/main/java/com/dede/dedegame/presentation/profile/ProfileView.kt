package com.dede.dedegame.presentation.profile

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.ProfileSettingModel
import com.dede.dedegame.presentation.common.CustomItemDecoration
import com.dede.dedegame.presentation.profile.groups.BottomLogoutGroupData
import com.dede.dedegame.presentation.profile.groups.ListMenuGroupData
import com.dede.dedegame.presentation.profile.groups.TopProfileGroupData
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.mvpcomponent.view.BaseConstraintView
import com.quangph.base.view.recyclerview.adapter.group.GroupRclvAdapter

class ProfileView(context: Context?, attrs: AttributeSet?) : BaseConstraintView(context, attrs) {

    private val rcvInfo by lazy { findViewById<RecyclerView>(R.id.rcvInfo) }
    private val profileSettingAdapter = GroupRclvAdapter()
    private var topProfileGroupData: TopProfileGroupData = TopProfileGroupData(null)
    private var listMenuGroupData: ListMenuGroupData = ListMenuGroupData(null)
    private var bottomLogoutGroupData: BottomLogoutGroupData = BottomLogoutGroupData(null)

    override fun onInitView() {
        super.onInitView()
        setupToolbar()

        rcvInfo?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcvInfo?.adapter = profileSettingAdapter
        rcvInfo?.addItemDecoration(
            CustomItemDecoration(
                context,
                R.dimen.margin_top_bottom_decorate,
                R.dimen.margin_left_right_decorate_16dp
            )
        )
        rcvInfo?.setItemAnimator(null)

        topProfileGroupData.onClickTopInfoItem = object : TopProfileGroupData.OnClickTopInfoItem {
            override fun onClickViewProfileDetail() {
                mPresenter.executeCommand(OnViewProfileDetailCmd())
            }
        }

        listMenuGroupData.onClickItemListener = object : ListMenuGroupData.OnClickItemListener {
            override fun onClickMenuItem(item: ProfileSettingModel, position: Int) {
                mPresenter.executeCommand(OnMenuProfileCmd(item))
            }
        }

        bottomLogoutGroupData.onClickLogoutItem = object : BottomLogoutGroupData.OnClickLogoutItem {
            override fun onClickLogoutUser() {
                mPresenter.executeCommand(OnUserLogoutCmd())
            }
        }

        profileSettingAdapter.addGroup(topProfileGroupData)
        if (DedeSharedPref.getUserInfo()?.user != null) {
            topProfileGroupData.reset(DedeSharedPref.getUserInfo()?.user)
            topProfileGroupData.show()
        }
        profileSettingAdapter.addGroup(listMenuGroupData)
        profileSettingAdapter.addGroup(bottomLogoutGroupData)
        bottomLogoutGroupData.reset("")
        bottomLogoutGroupData.show()
    }

    fun fillDataToMenu(listMenu: List<ProfileSettingModel>){
        listMenuGroupData.reset(listMenu)
        listMenuGroupData.show()
    }

    private fun setupToolbar() {
        val containerBack: View = findViewById(R.id.containerBack)
        val txtStartTitle: TextView = findViewById(R.id.txtStartTitle)
        val txtCenterTitle: TextView = findViewById(R.id.txtCenterTitle)
        txtStartTitle.text = context.getString(R.string.profile_setting_title)
        containerBack.setOnClickListener {
            mPresenter.executeCommand(OnBackCmd())
        }
    }

    class OnBackCmd() : ICommand
    class OnViewProfileDetailCmd() : ICommand
    class OnUserLogoutCmd() : ICommand
    class OnMenuProfileCmd(val item: ProfileSettingModel) : ICommand

}


