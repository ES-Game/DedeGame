package com.quangph.base.view.recyclerview;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Choreographer;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Created by Pham Hai Quang on 5/11/2019.
 */
public class UIJobScheduler {
    private static final long MAX_JOB_TIME_MS = 4;

    private static Deque<IUIJob> sJobDeque = new ArrayDeque<IUIJob>();
    private static Handler sHandler = new Handler();
    private static long sTotalJobTime = 0L;

    public static void submitJob(IUIJob job) {
        sJobDeque.add(job);
        if (sJobDeque.size() == 1) {
            sHandler.post(new Runnable() {
                @Override
                public void run() {
                    internalSubmitJob();
                }
            });
        }
    }

    private static void internalSubmitJob() {
        while (!sJobDeque.isEmpty() && sTotalJobTime < MAX_JOB_TIME_MS) {
            Log.e("UIJobScheduler", "do job");
            long start = SystemClock.elapsedRealtime();
            sJobDeque.poll().onNextFrame();
            sTotalJobTime += SystemClock.elapsedRealtime() - start;
        }

        if (sJobDeque.isEmpty()) {
            sTotalJobTime = 0;
        } else {
            onNextFrame();
        }
    }

    private static void onNextFrame() {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                Log.e("UIJobScheduler", "next frame");
                sTotalJobTime = 0;
                internalSubmitJob();
            }
        });
    }

    public interface IUIJob {
        void onNextFrame();
    }
}
