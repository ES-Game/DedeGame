package com.dede.dedegame.presentation.chapter

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import com.dede.dedegame.DomainManager
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.Chapter
import com.dede.dedegame.domain.model.OptionChapter
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.domain.usecase.GetOptionChapterAction
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.dede.dedegame.presentation.widget.dialog.CommentChapterDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quangph.base.mvp.ICommand
import com.quangph.base.mvp.action.Action
import com.quangph.base.mvp.action.ActionException
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.IScreenData
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_chapter)
class ChapterActivity : JetActivity<ChapterView>() {
    private var mChapterId = -1
    private lateinit var mediaPlayer: MediaPlayer
    private var isPlaying = false
    override fun onPresenterReady() {
        super.onPresenterReady()

        val type = object : TypeToken<StoryDetail>() {}.type
        val input: StoryDetail? = Gson().fromJson(intent.getStringExtra("key_data_story"), type)
        mChapterId = intent.getIntExtra("chapter_id", -1)
        input?.let { story ->
            getOptionChapter() { listMenu ->
                story.title?.let { storyName -> mvpView.setStoryName(storyName) }
                story.chapters?.reversed()?.let { chapters ->
                    refreshStateItemViewMenu(listMenu, chapters, mChapterId)
                }
                trackingOnChapterScreen(PARAM_STORY, story.id)
            }
        }
        initMediaPlayer()
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is ChapterView.ChangeChapterCmd -> {
                getChapterDetail(command.chapterId)
            }

            is ChapterView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }

            is ChapterView.RefreshMenuCmd -> {
                refreshStateItemViewMenu(command.listMenu, command.chapters, command.chapterId)
            }

            is ChapterView.ChangeStateSoundCmd -> {
                if (isPlaying) {
                    mediaPlayer.pause()
//                    playButton.text = "Play"
                } else {
                    mediaPlayer.start()
//                    playButton.text = "Pause"
                }
                isPlaying = !isPlaying
                mvpView.setStateSoundMenu(isPlaying)
            }

            is ChapterView.MoveCommentChapterCmd -> {
                val commentChapterDialog = CommentChapterDialog.newInstance(command.chapterId)
                commentChapterDialog.show(supportFragmentManager, commentChapterDialog.tag)
            }
        }
    }

    private fun refreshStateItemViewMenu(
        listMenu: List<OptionChapter>,
        chapters: List<Chapter>,
        chapterId: Int
    ) {
        if (chapters.isEmpty()) {
            listMenu.forEach {
                it.enabled = false
                it.selected = false
            }
            mvpView.fillDataToBottomMenu(listMenu)
        } else {
            if (chapterId == -1) {
                if (chapters.size > 1) {
                    listMenu.first().enabled = false
                    listMenu.first().selected = false
                    listMenu.last().enabled = true
                    listMenu.last().selected = false
                } else {
                    listMenu.first().enabled = false
                    listMenu.first().selected = false
                    listMenu.last().enabled = false
                    listMenu.last().selected = false
                }
            } else {
                if (chapters.size > 1) {
                    val currentIndex = chapters.indexOfFirst { it.id == chapterId }
                    when (currentIndex) {
                        0 -> {
                            listMenu.first().enabled = false
                            listMenu.first().selected = false
                            listMenu.last().enabled = true
                            listMenu.last().selected = false
                        }

                        chapters.size - 1 -> {
                            listMenu.first().enabled = true
                            listMenu.first().selected = false
                            listMenu.last().enabled = false
                            listMenu.last().selected = false
                        }

                        else -> {
                            listMenu.first().enabled = true
                            listMenu.first().selected = false
                            listMenu.last().enabled = true
                            listMenu.last().selected = false
                        }
                    }
                } else {
                    listMenu.first().enabled = false
                    listMenu.first().selected = false
                    listMenu.last().enabled = false
                    listMenu.last().selected = false
                }
            }
            mvpView.fillDataToSpinner(chapterId, chapters)
            mvpView.fillDataToBottomMenu(listMenu)
        }
    }

    private fun getOptionChapter(callback: (List<OptionChapter>) -> Unit) {
        val getOptionAction = GetOptionChapterAction(this@ChapterActivity)
        mActionManager.executeAction(
            getOptionAction,
            object : Action.SimpleActionCallback<List<OptionChapter>>() {
                override fun onSuccess(responseValue: List<OptionChapter>?) {
                    super.onSuccess(responseValue)
                    responseValue?.let {
                        callback(it)
                    }
                }

                override fun onError(e: ActionException) {
                    super.onError(e)
                    Toast.makeText(this@ChapterActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getChapterDetail(id: Int) {
        val chapterLink = DomainManager.getCurrentDomain() + "chapter/iframe/$id"
        mvpView.loadChapterContent(chapterLink)
    }

    private fun initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.asmr)
        mediaPlayer.setOnCompletionListener {
//            playButton.text = "Play"
            isPlaying = false
        }
    }


    class ChapterInput() : IScreenData {
        var storyDetail: StoryDetail? = null

        constructor(parcel: Parcel) : this() {
            storyDetail = parcel.readParcelable(StoryDetail::class.java.classLoader)
        }


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeParcelable(storyDetail, flags)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<ChapterInput> {
            override fun createFromParcel(parcel: Parcel): ChapterInput {
                return ChapterInput(parcel)
            }

            override fun newArray(size: Int): Array<ChapterInput?> {
                return arrayOfNulls(size)
            }
        }


    }

    private fun trackingOnChapterScreen(
        param: String,
        paramValue: Any?,
    ) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = EVENT_ON_CHAPTER
            this.param = "on_screen"
            this.paramValue = "on_screen"
            this.param2 = param
            this.paramValue2 = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    private fun trackingTapEventChapter(eventName: String, param: String, paramValue: Any?) {
        val fbModel = FirebaseLoginModel().apply {
            this.eventName = eventName
            this.param = param
            this.paramValue = paramValue.toString()
        }
        DedeFirebaseTracker.track(fbModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }

    inner class FirebaseLoginModel : DedeFirebaseTrackerModel() {
        override var screenName: String? = this@ChapterActivity.javaClass.simpleName
        var param: String = ""
        var paramValue: String = ""
        var param2: String = ""
        var paramValue2: String = ""
        override fun createParams(bundle: Bundle) {
            super.createParams(bundle)
            bundle.putString(param, paramValue)
            bundle.putString(param2, paramValue2)
        }
    }

    companion object {
        const val EVENT_ON_CHAPTER = "event_on_chapter"
        const val EVENT_TAP_CHAPTER_ITEM = "event_tap_chapter_item"
        const val PARAM_STORY = "story_id"
        const val PARAM_CHAPTER = "chapter_id"
    }
}