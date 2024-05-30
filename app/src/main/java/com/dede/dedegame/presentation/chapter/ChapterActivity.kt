package com.dede.dedegame.presentation.chapter

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.StoryDetail
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTracker
import com.dede.dedegame.presentation.common.tracker.DedeFirebaseTrackerModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.IScreenData
import com.quangph.jetpack.JetActivity

@Layout(R.layout.activity_chapter)
class ChapterActivity : JetActivity<ChapterView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()

        val type = object : TypeToken<StoryDetail>() {}.type
        val input: StoryDetail? = Gson().fromJson(intent.getStringExtra("key_data_story"), type)
        input?.let {
            it.title?.let { it1 -> mvpView.setStoryName(it1) }
            it.chapters?.reversed()?.let { it1 -> mvpView.fillDataToSpinner(it1) }
            it.chapters?.reversed()?.first()?.id?.let { it1 -> getChapterDetail(it1) }
            trackingOnChapterScreen(PARAM_STORY, it.id)
        }
    }

    override fun onExecuteCommand(command: ICommand) {
        super.onExecuteCommand(command)
        when (command) {
            is ChapterView.ChangeChapterCmd -> {
                trackingTapEventChapter(EVENT_TAP_CHAPTER_ITEM, PARAM_CHAPTER, command.chapterId)
                getChapterDetail(command.chapterId)
            }

            is ChapterView.OnBackCmd -> {
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun getChapterDetail(id: Int) {
        val chapterLink = "https://www.dedegame.me/chapter/iframe/$id"
        mvpView.loadChapterContent(chapterLink)
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