package com.dede.dedegame.presentation.register

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.dede.dedegame.AppConfig
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.usecase.RegisterAction
import com.dede.dedegame.extension.startActivity
import com.dede.dedegame.presentation.home.HomeActivity
import com.dede.dedegame.presentation.login.LoginActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity
import java.util.Locale

@Layout(R.layout.activity_register)
class RegisterActivity : JetActivity<RegisterView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is RegisterView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is RegisterView.MoveLoginCmd -> {
                startActivity(LoginActivity::class.java)
            }

            is RegisterView.OnClickRegisterCmd -> {
                register(
                    command.email,
                    command.name,
                    command.password,
                    command.rePassword,
                    Locale.getDefault().language,
                    AppConfig.clientId,
                    AppConfig.clientSecret
                )
            }
        }
    }

    private fun register(
        email: String,
        name: String,
        password: String,
        rePassword: String,
        lang: String,
        clientId: Int,
        clientSecret: String
    ) {
        showLoading()
        val rv = RegisterAction.RV().apply {
            this.email = email
            this.name = name
            this.password = password
            this.rePassword = rePassword
            this.lang = lang
            this.clientId = clientId
            this.clientSecret = clientSecret
        }

        actionManager.executeAction(
            RegisterAction(),
            rv,
            object : Action.SimpleActionCallback<UserInfo>() {
                override fun onSuccess(responseValue: UserInfo?) {
                    super.onSuccess(responseValue)
                    hideLoading()
                    if (responseValue != null) {
                        goToHome()
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    hideLoading()
                    Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun goToHome() {
        val intent = Intent(this@RegisterActivity, HomeActivity::class.java);
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

}