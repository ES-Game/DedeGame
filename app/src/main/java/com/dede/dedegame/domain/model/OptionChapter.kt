package com.dede.dedegame.domain.model

data class OptionChapter(val id: Int, val name: String, val type: TypeOption, var enabled: Boolean, var selected: Boolean)

enum class TypeOption {
    NEXT, PREVIOUS, COMMENT, CHAPTER
}