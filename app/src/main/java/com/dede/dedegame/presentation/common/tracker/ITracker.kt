package com.dede.dedegame.presentation.common.tracker

interface ITracker<T: ITrackerModel> {

    fun track(model: T)
}