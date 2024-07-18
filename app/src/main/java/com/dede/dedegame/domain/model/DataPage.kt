package com.dede.dedegame.domain.model

open class DataPage<T> {
    var dataList: List<T> = emptyList()
    var currentPage: Int = -1
    var lastPage: Int = 0
    var perPage: Int = 0
    var hasNextPage: Boolean = false
}

class StoryListDataPage<T> : DataPage<T>() {
    var title: String = ""
}