package com.dede.dedegame.domain.model

class DataPage<T> {
    var dataList: List<T> = emptyList()
    var currentPage: Int = -1
    var title: String = ""
    var lastPage: Int = 0
    var perPage: Int = 0
    var hasNextPage: Boolean = false
}