package com.dede.dedegame.domain.usecase

import com.dede.dedegame.AppConfig
import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.dede.dedegame.repo.network.APIActionException
import com.dede.dedegame.repo.network.APIException
import com.dede.dedegame.repo.user.exception.LogoutException
import com.quangph.base.mvp.action.Action
import java.util.concurrent.CountDownLatch

class UnLikeCommentStory : Action<UnLikeCommentStory.RV, Int>() {

    class RV : Action.RequestValue {
        var storyId: Int = -1
        var commentId: Int = -1
    }

    override fun onExecute(rv: RV): Int {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        return try {
            dedeRepo.unlikeCommentStory(rv.storyId, rv.commentId)
        } catch (e: APIException) {
            if (e.isExpired()) {
                return handleTokenRefreshAndRetry(dedeRepo, rv)
            } else {
                throw APIActionException(e.code, e.message)
            }
        }
    }

    private fun handleTokenRefreshAndRetry(dedeRepo: IDedeGameRepo, rv: RV): Int {
        val latch = CountDownLatch(1)
        var result: Int? = null
        var exception: Exception? = null

        val onSuccess = {
            try {
                result = dedeRepo.unlikeCommentStory(rv.storyId, rv.commentId)
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