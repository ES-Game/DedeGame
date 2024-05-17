package com.dede.dedegame.domain.usecase

import com.dede.dedegame.DedeSharedPref
import com.quangph.base.mvp.action.Action
import com.dede.dedegame.domain.model.UserInfo
import com.dede.dedegame.domain.repo.IDedeGameRepo
import com.dede.dedegame.domain.repo.RepoFactory
import com.dede.dedegame.presentation.common.LogUtil
import com.google.gson.Gson

class LoginAction : Action<LoginAction.RV, UserInfo>() {

    class RV : Action.RequestValue {
        var userNameOrMail: String = ""
        var password: String = ""
        var lang: String = ""
        var clientId: Int = -1
        var clientSecret: String = ""
    }

    override fun onExecute(rv: RV): UserInfo {
        val dedeRepo: IDedeGameRepo = RepoFactory.getDedeGameRepo()
        val localUserInfo = DedeSharedPref.getUserInfo()
        localUserInfo?.let {
            return it
        }?: run {
            val userInfo = dedeRepo.login(rv.userNameOrMail, rv.password, rv.lang, rv.clientId, rv.clientSecret)
            DedeSharedPref.saveUserInfo(userInfo)
            LogUtil.getInstance().e("fdsafdsafdsa =====================> "+ Gson().toJson(DedeSharedPref.getUserInfo()))
            return userInfo
        }
    }
}