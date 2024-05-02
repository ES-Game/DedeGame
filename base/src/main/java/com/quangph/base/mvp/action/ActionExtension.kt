package com.quangph.base.mvp.action

import com.quangph.base.mvp.action.actionjob.SimpleActionJob

/**
 * Created by QuangPH on 2020-03-23.
 */

typealias JOB = () -> Unit

fun Action<*, *>.addJob(job: JOB): Action.Builder {

    return this.backgroundJob(object : SimpleActionJob<Unit>() {
        override fun submitJob() {
            job.invoke()
        }
    })
}

fun Action.Builder.addJob(job: JOB): Action.Builder {
    return this.addJob(object : SimpleActionJob<Unit>() {
        override fun submitJob() {
            job.invoke()
        }
    })
}