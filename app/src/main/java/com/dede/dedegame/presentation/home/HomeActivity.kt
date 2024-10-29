package com.dede.dedegame.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import com.dede.dedegame.R
import com.dede.dedegame.extension.startActivityExt
import com.dede.dedegame.presentation.profile.ProfileActivity
import com.dede.dedegame.presentation.story_search.StorySearchActivity
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_home)
class HomeActivity : JetActivity<HomeView>() {

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE: Int = 11112
    }

    override fun onPresenterReady() {
        super.onPresenterReady()
        requestFragmentManager()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf<String>(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is HomeView.MoveProfileCmd -> {
                startActivityExt(ProfileActivity::class.java)
            }

            is HomeView.MoveSearchStoryCmd -> {
                startActivityExt(StorySearchActivity::class.java)
            }
        }
    }
}