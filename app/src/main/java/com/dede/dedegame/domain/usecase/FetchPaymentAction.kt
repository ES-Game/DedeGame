package com.dede.dedegame.domain.usecase

import com.dede.dedegame.AppConfig
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.domain.model.payment.Payment
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.dede.dedegame.repo.network.APIActionException
import com.dede.dedegame.repo.network.APIException
import com.dede.dedegame.repo.user.exception.LogoutException
import com.quangph.base.mvp.action.Action
import java.util.concurrent.CountDownLatch

class FetchPaymentAction : Action<FetchPaymentAction.RV, Payment>() {

    class RV : Action.RequestValue {
    }

    override fun onExecute(rv: RV): Payment {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return try {
            dedeRepo.fetchPayment()
        } catch (e: APIException) {
            if (e.isExpired()) {
                return handleTokenRefresh(dedeRepo)
            } else {
                throw APIActionException(e.code, e.message)
            }
        }
    }

    private fun handleTokenRefresh(dedeRepo: IDedeGameRepo): Payment {
        val latch = CountDownLatch(1)
        var result: Payment? = null
        var exception: Exception? = null

        val onSuccess = {
            try {
                result = dedeRepo.fetchPayment()
            } catch (e: APIException) {
                exception = e
            } finally {
                latch.countDown()
            }
        }

        val onError = { apiException: APIException ->
            exception = apiException
            latch.countDown()
        }

        refreshToken(onSuccess, onError)
        latch.await()
        exception?.let { throw it }
        return result ?: throw RuntimeException("Unexpected null result")
    }

    private fun refreshToken(onSuccess: () -> Unit, onError: (APIException) -> Unit) {
        try {
            val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
            val userInfo = dedeRepo.refreshToken(
                DedeSharedPref.getUserInfo()?.authen?.refreshToken!!,
                AppConfig.clientId,
                AppConfig.clientSecret
            )
            DedeSharedPref.getUserInfo()?.authen = userInfo.authen
            onSuccess()
        } catch (e: APIException) {
            onError(LogoutException())
        }
    }
}