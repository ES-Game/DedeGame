package com.dede.dedegame.presentation.profile

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.ProfileSettingModel
import com.dede.dedegame.domain.usecase.GetProfileSettingAction
import com.dede.dedegame.extension.startActivityExt
import com.dede.dedegame.presentation.login.LoginActivity
import com.dede.dedegame.presentation.story_followed.StoryBookmarkActivity
import com.dede.dedegame.presentation.widget.dialog.UserLogoutDialog
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_profile)
class ProfileActivity : JetActivity<ProfileView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
        getMenuForProfile()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is ProfileView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is ProfileView.OnViewProfileDetailCmd -> {
                Toast.makeText(this, this.getString(R.string.app_common_coming_soon), Toast.LENGTH_SHORT).show()
            }

            is ProfileView.OnMenuProfileCmd -> {
                when (command.item.profileType) {
                    ProfileSettingModel.MenuType.FOLLOWED -> {
                        navigateToStoryBookmark()
                    }

                    else -> {

                    }
                }
            }

            is ProfileView.OnUserLogoutCmd -> {
                logOut()
            }
        }
    }

    private fun getMenuForProfile() {
        val getMenuProfileAction = GetProfileSettingAction(this@ProfileActivity)
        mActionManager.executeAction(
            getMenuProfileAction,
            object : Action.SimpleActionCallback<ArrayList<ProfileSettingModel>>() {
                override fun onSuccess(responseValue: ArrayList<ProfileSettingModel>?) {
                    super.onSuccess(responseValue)
                    responseValue?.let {
                        mvpView.fillDataToMenu(it)
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    Toast.makeText(this@ProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun navigateToStoryBookmark() {
        startActivityExt(StoryBookmarkActivity::class.java)
    }

    private fun logOut() {
        val fm: FragmentManager = supportFragmentManager
        val dialog: UserLogoutDialog = UserLogoutDialog.newInstance()
        dialog.setOnEventDialogListener(object : UserLogoutDialog.OnEventDialogListener {
            override fun onClickApply() {
                dialog.dismiss()
                DedeSharedPref.saveUserInfo(null)
                val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        })
        dialog.show(fm, dialog.tag)
    }

}