package com.dede.dedegame.presentation.splash

import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.presentation.home.HomeActivity
import com.dede.dedegame.presentation.login.LoginActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_splash)
class SplashActivity : JetActivity<SplashView>() {

    override fun onStart() {
        super.onStart()
        supportActionBar?.hide()
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        if (DedeSharedPref.getUserInfo() == null){
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, 1000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }, 1000)
        }
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {

        }
    }
}