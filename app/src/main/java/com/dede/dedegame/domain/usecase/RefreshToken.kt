package com.dede.dedegame.domain.usecase

import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.dede.dedegame.repo.network.APIActionException
import com.dede.dedegame.repo.network.APIException
import com.quangph.base.mvp.action.Action

class RefreshToken : Action<RefreshToken.RV, UserInfo>() {

    class RV : Action.RequestValue {
        var token: String = ""
        var clientId: Int = -1
        var clientSecret: String = ""
    }

    override fun onExecute(rv: RV): UserInfo {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        try {
            val userInfo = dedeRepo.refreshToken(rv.token, rv.clientId, rv.clientSecret)
            DedeSharedPref.getUserInfo()?.authen = userInfo.authen
            return userInfo
        } catch (e: APIException) {
            throw APIActionException(e.code, e.message)
        }
    }
}