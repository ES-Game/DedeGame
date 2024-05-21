package com.dede.dedegame.presentation.home.fragments.shop

import android.content.Intent
import android.widget.Toast
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.payment.Payment
import com.dede.dedegame.domain.usecase.FetchPaymentAction
import com.dede.dedegame.presentation.login.LoginActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetFragment


@Layout(R.layout.fragment_shop)
class ShopFragment : JetFragment<ShopFragmentView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()

    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)

        when (command) {
            is ShopFragmentView.OnLogoutCmd -> {
                logOut()
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
                            logOut()
                        }
                    })
            }
        }
    }

    private fun logOut() {
        Toast.makeText(activity, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show()
        DedeSharedPref.saveUserInfo(null)
        val intent = Intent(activity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }
}