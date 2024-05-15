package com.dede.dedegame.presentation.forget_password

import com.dede.dedegame.R
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_forget_password)
class ForgetPasswordActivity : JetActivity<ForgetPasswordView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is ForgetPasswordView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

}