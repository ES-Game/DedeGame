package com.quangph.dedegame.domain.model

abstract class DataPage<T> {
    var dataList: List<T> = emptyList()
    var pageOffset: Int = -1
    var total: Int = 0
}