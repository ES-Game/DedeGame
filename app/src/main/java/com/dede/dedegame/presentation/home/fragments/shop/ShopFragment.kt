package com.dede.dedegame.presentation.home.fragments.shop

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.payment.Payment
import com.dede.dedegame.domain.usecase.FetchPaymentAction
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.login.LoginActivity
import com.dede.dedegame.presentation.widget.dialog.ExpiredSessionDialog
import com.dede.dedegame.repo.user.exception.LogoutException
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment


@Layout(R.layout.fragment_shop)
class ShopFragment : JetFragment<ShopFragmentView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()
        trackingOnShopScreen()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is ShopFragmentView.OnLogoutCmd -> {
                logOut()
            }

            is ShopFragmentView.OnFinishLoadWebCmd -> {
                hideLoading()
            }

            is ShopFragmentView.OnGetPaymentLinkCmd -> {
                showLoading()
                actionManager.executeAction(
                    FetchPaymentAction(),
                    FetchPaymentAction.RV(),
                    object : Action.SimpleActionCallback<Payment>() {
                        override fun onSuccess(responseValue: Payment?) {
                            super.onSuccess(responseValue)
                            hideLoading()
                            responseValue?.let {
                                it.link?.let { it1 -> mvpView.loadPaymentContent(it1) }
                            }
                        }

                        override fun onError(e: ActionException) {
                            super.onError(e)
                            hideLoading()
                            if (e.cause is LogoutException) {
                                logOut()
                                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(activity, e.message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })
            }
        }
    }

    private fun logOut() {
        val fm: FragmentManager = childFragmentManager
        val dialog: ExpiredSessionDialog = ExpiredSessionDialog.newInstance()
        dialog.setOnEventDialogListener(object : ExpiredSessionDialog.OnEventDialogListener {
            override fun onClickApply() {
                dialog.dismiss()
                DedeSharedPref.saveUserInfo(null)
                val intent = Intent(activity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        })
        dialog.show(fm, dialog.tag)
    }

    private fun trackingOnShopScreen() {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = EVENT_ON_SHOP
            this.param = "on_screen"
            this.paramValue = "on_screen"
        }
        DedeFirebaseTracker.track(fbModel)
    }

    inner class FirebaseLoginModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@ShopFragment.javaClass.simpleName
        var param: String = ""
        var paramValue: String = ""

        override fun createParams(bundle: Bundle) {
            super.createParams(bundle)
            bundle.putString(param, paramValue)
        }
    }

    companion object {
        const val EVENT_ON_SHOP = "event_on_shop"
    }
}