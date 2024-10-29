package com.dede.dedegame.domain.usecase

import android.content.Context
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.ProfileSettingModel
import com.quangph.base.mvp.action.Action
import java.util.ArrayList

class GetProfileSettingAction(val context: Context) : Action<Action.VoidRequest, ArrayList<ProfileSettingModel>>() {

    override fun onExecute(requestValue: VoidRequest): ArrayList<ProfileSettingModel> {
        val result = ArrayList<ProfileSettingModel>()
//        result.add(ProfileSettingModel(R.drawable.ic_stories_history, context.getString(R.string.profile_setting_stories_history), ProfileSettingModel.MenuType.HISTORY))
        result.add(ProfileSettingModel(R.drawable.ic_story_followed, context.getString(R.string.profile_setting_stories_followed), ProfileSettingModel.MenuType.FOLLOWED))
        return result
    }

}