package com.quangph.base.thread;

import android.os.Handler;
import android.os.HandlerThread;

public class PostableThread extends HandlerThread {

    private Handler mWorkerHandler;

    public PostableThread(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        mWorkerHandler = new Handler(getLooper());
    }

    /**
     * Sometimes you'll get NPE at the postTask call stating, that mWorkerHandler is null
     * The trick here is that run() method will be called only after new thread is created and started.
     * And this call can sometimes happen after your call to the postTask method, so you can be a
     * victim of race conditions between two threads (main and background)
     * @param task
     */
    public void postTask(Runnable task){
        if (mWorkerHandler != null) {
            mWorkerHandler.post(task);
        }
    }

    public void postTaskDelay(Runnable task, long timeInMillis) {
        if (mWorkerHandler != null) {
            mWorkerHandler.postDelayed(task, timeInMillis);
        }
    }

    public void cancelTask(Runnable task) {
        if (mWorkerHandler != null) {
            mWorkerHandler.removeCallbacks(task);
        }
    }

    public void clearAllTask() {
        if (mWorkerHandler != null) {
            mWorkerHandler.removeCallbacksAndMessages(null);
        }
    }
}
