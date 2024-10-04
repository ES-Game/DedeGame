package com.dede.dedegame.domain.model

data class ProfileSettingModel(val icon: Int, val name: String, var profileType: MenuType){
    enum class MenuType {
        FOLLOWED,
        HISTORY,
        LOGOUT
    }
}

