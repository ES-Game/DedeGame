package com.dede.dedegame.domain.usecase

import android.content.Context
import com.dede.dedegame.R
import com.dede.dedegame.domain.model.OptionChapter
import com.dede.dedegame.domain.model.TypeOption
import com.quangph.base.mvp.action.Action

class GetOptionChapterAction(private val context: Context) :
    Action<Action.VoidRequest, List<OptionChapter>>() {

    override fun onExecute(requestValue: VoidRequest): List<OptionChapter> {
        val result = ArrayList<OptionChapter>()
        result.add(
            OptionChapter(
                R.drawable.ic_previous_chapter,
                context.getString(R.string.option_chapter_previous_action),
                TypeOption.PREVIOUS, enabled = false, selected = false
            )
        )
        result.add(
            OptionChapter(
                R.drawable.ic_chapter_list,
                context.getString(R.string.option_chapter_chapter_action),
                TypeOption.CHAPTER, enabled = true, selected = false
            )
        )
        result.add(
            OptionChapter(
                R.drawable.ic_chapter_comment,
                context.getString(R.string.option_chapter_comment_action),
                TypeOption.COMMENT, enabled = true, selected = false
            )
        )
        result.add(
            OptionChapter(
                R.drawable.ic_sound_off,
                context.getString(R.string.option_chapter_sound_action),
                TypeOption.SOUND, enabled = true, selected = false
            )
        )
        result.add(
            OptionChapter(
                R.drawable.ic_next_chapter,
                context.getString(R.string.option_chapter_next_action),
                TypeOption.NEXT, enabled = false, selected = false
            )
        )
        return result
    }
}