package com.dede.dedegame.presentation.chapter

import android.os.Parcel
import android.os.Parcelable
import com.quangph.base.mvp.ICommand
import com.quangph.base.viewbinder.Layout
import com.quangph.jetpack.JetActivity
import com.dede.dedegame.R
import com.quangph.dedegame.domain.model.StoryDetail
import com.quangph.dedegame.presentation.chapter.ChapterView
import com.quangph.jetpack.IScreenData

@Layout(R.layout.activity_chapter)
class ChapterActivity : JetActivity<ChapterView>() {

    override fun onPresenterReady() {
        super.onPresenterReady()

        val input : StoryDetail? = intent.getParcelableExtra("key_data_story")
        input?.let {
            it.title?.let { it1 -> mvpView.setStoryName(it1) }
            it.chapters?.reversed()?.let { it1 -> mvpView.fillDataToSpinner(it1) }
            it.chapters?.reversed()?.first()?.id?.let { it1 -> getChapterDetail(it1) }
        }
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
}