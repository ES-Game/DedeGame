package com.dede.dedegame.presentation.login

import android.content.Context
import android.content.Intent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.dede.dedegame.AppConfig
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.usecase.LoginAction
import com.dede.dedegame.extension.startActivity
import com.dede.dedegame.presentation.forget_password.ForgetPasswordActivity
import com.dede.dedegame.presentation.home.HomeActivity
import com.dede.dedegame.presentation.register.RegisterActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity
import java.util.Locale

@Layout(R.layout.activity_login)
class LoginActivity : JetActivity<LoginView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is LoginView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is LoginView.MoveRegisterCmd -> {
                startActivity(RegisterActivity::class.java)
            }

            is LoginView.MoveForgetPasswordCmd -> {
                startActivity(ForgetPasswordActivity::class.java)
            }

            is LoginView.OnClickLoginCmd -> {
                login(
                    command.userNameOrMail,
                    command.password,
                    Locale.getDefault().language,
                    AppConfig.clientId,
                    AppConfig.clientSecret
                )
            }
        }
    }

    private fun login(
        userNameOrMail: String,
        password: String,
        lang: String,
        clientId: Int,
        clientSecret: String
    ) {
        showLoading()
        val rv = LoginAction.RV().apply {
            this.userNameOrMail = userNameOrMail
            this.password = password
            this.lang = lang
            this.clientId = clientId
            this.clientSecret = clientSecret
        }

        actionManager.executeAction(
            LoginAction(),
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
                    Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun goToHome() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java);
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