package com.dede.dedegame.presentation.home.fragments.shop

import com.dede.dedegame.R
import com.dede.dedegame.domain.model.payment.Payment
import com.dede.dedegame.domain.usecase.FetchPaymentAction
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
                        }
                    })
            }
        }
    }


}