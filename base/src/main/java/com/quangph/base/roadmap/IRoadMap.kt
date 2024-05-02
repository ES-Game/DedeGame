package com.quangph.base.roadmap

import com.quangph.jetpack.IJetContext

/**
 * Created by QuangPH on 2020-11-30.
 */
interface IRoadMap {
    fun log(screen: IJetContext)
    fun finish(screen: IJetContext)
    fun traceDirection(direction: ROAD_DIRECTION)
}