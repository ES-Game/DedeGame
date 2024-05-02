package com.quangph.base.thread

import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by QuangPH on 4/20/21.
 */
class SerialExecutor : Executor {
    private val executor: Executor = ThreadPoolExecutor(1, 20, 3,
        TimeUnit.SECONDS, SynchronousQueue<Runnable>(),
        SyncThreadFactory()
    )

    val tasks = ArrayDeque<Runnable>()
    var activeTask: Runnable? = null

    @Synchronized
    override fun execute(r: Runnable) {
        tasks.offer(Runnable {
            try {
                r.run()
            } finally {
                scheduleNext()
            }
        })

        if (activeTask == null) {
            scheduleNext()
        }
    }

    @Synchronized
    private fun scheduleNext() {
        if (tasks.poll().also { activeTask = it } != null) {
            executor.execute(activeTask)
        }
    }


    private class SyncThreadFactory: ThreadFactory {

        private val count = AtomicInteger(1)

        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "SyncTask #" + count.getAndIncrement())
        }
    }
}