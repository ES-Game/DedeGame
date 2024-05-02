package com.quangph.sync.core.enqueue

import com.quangph.sync.core.action.SyncAction
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by QuangPH on 2021-02-01.
 */
class ExecutorSyncEnqueue: ISyncEnqueue {

    private val executor: Executor

    init {
        executor = ThreadPoolExecutor(1, 20, 3,
            TimeUnit.SECONDS, SynchronousQueue<Runnable>(),
            SyncThreadFactory()
        )
    }

    override fun enqueue(syncAction: SyncAction) {
        executor.execute {
            syncAction.sync()
        }
    }


    private class SyncThreadFactory: ThreadFactory {

        private val count = AtomicInteger(1)

        override fun newThread(r: Runnable?): Thread {
            return Thread(r, "SyncTask #" + count.getAndIncrement())
        }
    }
}