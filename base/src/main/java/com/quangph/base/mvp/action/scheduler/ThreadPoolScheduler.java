package com.quangph.base.mvp.action.scheduler;

import android.os.Handler;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionException;
import com.quangph.base.mvp.action.ActionMonitor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Created by Pham Hai Quang on 1/8/2019.
 */
public class ThreadPoolScheduler implements IActionScheduler {

    private final Handler mHandler = new Handler();

    public static final int POOL_SIZE = 2;
    public static final int MAX_POOL_SIZE = 4;
    public static final int TIMEOUT = 30;

    private ThreadPoolExecutor mThreadPoolExecutor;

    public ThreadPoolScheduler() {
        mThreadPoolExecutor = new ThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, TIMEOUT,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(POOL_SIZE));
    }

    @Override
    public void execute(final Action action) {
        mThreadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                action.setEventPublisher(new Action.IOnEventPublisher() {
                    @Override
                    public void onPublishEvent(final Object event) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                action.setCurrentEvent(event);
                                ActionMonitor.getInstance().notifyProgress(action);
                            }
                        });
                    }
                });

                try {
                    Object re = action.execute();
                    action.setResponse(re);
                    action.setEventPublisher(null);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ActionMonitor.getInstance().notifySuccessAction(action);
                        }
                    });
                } catch (final ActionException e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            action.setEventPublisher(null);
                            action.setError(e);
                            ActionMonitor.getInstance().notifyErrorAction(action);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void stop(Action action) {
        action.setEventPublisher(null);
        try {
            action.stop();
        } catch (ActionException e) {
            e.printStackTrace();
        }
    }
}
