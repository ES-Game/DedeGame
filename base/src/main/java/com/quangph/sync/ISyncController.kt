package com.quangph.sync

import android.content.Context
import android.content.Intent

/**
 * The controller is middle man between Presenter and SyncService. The Presenter using sync controller to start, get progress, bind,...
 * Created by QuangVH on 3/17/2021.
 */
interface ISyncController {
    /**
     * Start sync
     */
    fun start(context: Context, intent: Intent?)

    /**
     *  When sync service has already started, if you want to attach to that service to archive progress, call this func
     */
    fun bindToSync(context: Context, callback: IBindCallback?)

    /**
     * When you bind to sync service, if you leave the screen you need unbind
     */
    fun unbind(context: Context)
}

interface IBindCallback {
    fun bind(progressMap: Map<String, Float>)
    fun syncDone()
}