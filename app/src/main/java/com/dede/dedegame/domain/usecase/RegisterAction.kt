package com.dede.dedegame.domain.usecase

import com.dede.dedegame.DedeSharedPref
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.quangph.base.mvp.action.Action

class RegisterAction : Action<RegisterAction.RV, UserInfo>() {

    class RV : Action.RequestValue {
        var email: String = ""
        var name: String = ""
        var password: String = ""
        var rePassword: String = ""
        var lang: String = ""
        var clientId: Int = -1
        var clientSecret: String = ""
    }

    override fun onExecute(rv: RV): UserInfo {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        val localUserInfo = DedeSharedPref.getUserInfo()
        localUserInfo?.let {
            return it
        } ?: run {
            val userInfo = dedeRepo.register(
                rv.email,
                rv.name,
                rv.password,
                rv.rePassword,
                rv.lang,
                rv.clientId,
                rv.clientSecret
            )
            DedeSharedPref.saveUserInfo(userInfo)
            return userInfo
        }
    }
}