package com.dede.dedegame.domain.usecase

import com.quangph.base.mvp.action.Action
import com.dede.dedegame.domain.model.payment.Payment
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory

class FetchPaymentAction : Action<FetchPaymentAction.RV, Payment>() {

    class RV : Action.RequestValue {
    }

    override fun onExecute(rv: RV): Payment {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return dedeRepo.fetchPayment()
    }
}