package com.quangph.base.mvp.action.post;

import android.os.Handler;
import android.util.Log;

import com.quangph.base.mvp.action.ACTION_POST_POLICY;
import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionCallbackInfo;
import com.quangph.base.mvp.action.ActionException;
import com.quangph.base.mvp.action.ActionMonitor;
import com.quangph.base.mvp.action.scheduler.IActionScheduler;
import com.quangph.base.thread.PostableThread;

import java.util.ArrayList;
import java.util.List;

public class PostTaskScheduler implements IActionScheduler {

    private final Handler mUIHandler;
    private final PostableThread mThread;
    private final IPostIDGenerator mIDGenerator;
    private final String mActionManagerId;
    private final long mTimeInMillis;
    private final ACTION_POST_POLICY mPolicy;
    private Runnable mTask;
    private boolean isInit = false;

    public PostTaskScheduler(Handler uiHandler, PostableThread thread,
                             IPostIDGenerator idGenerator,
                             String actionManagerID,
                             long timeInMillis, ACTION_POST_POLICY policy) {
        mUIHandler = uiHandler;
        mThread = thread;
        mIDGenerator = idGenerator;
        mActionManagerId = actionManagerID;
        mTimeInMillis = timeInMillis;
        mPolicy = policy;
    }

    @Override
    public void execute(Action action) {
        Log.e("PostTask", "execute: " + action.getID());
        mUIHandler.removeCallbacksAndMessages(null);
        PostAction wrapAction = (PostAction) action;
        wrapAction.setEventPublisher(new Action.IOnEventPublisher() {
            @Override
            public void onPublishEvent(Object event) {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        wrapAction.setCurrentEvent(event);
                        ActionMonitor.getInstance().notifyProgress(wrapAction);
                    }
                });
            }
        });

        mTask = new Runnable() {
            @Override
            public void run() {
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("PostTask", "run");
                        ActionMonitor.getInstance().notifyStartAction(wrapAction);
                        mUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mThread.postTask(realTask(wrapAction));
                            }
                        });
                    }
                });
            }
        };

        if (!isInit) {
            isInit = true;
            if (mPolicy == ACTION_POST_POLICY.DELAY) {
                mThread.postTaskDelay(mTask, mTimeInMillis);
            } else if (mPolicy == ACTION_POST_POLICY.REPEAT) {
                mThread.postTaskDelay(mTask, mTimeInMillis);
            } else if (mPolicy == ACTION_POST_POLICY.REPEAT_INCLUDE) {
                mThread.postTask(mTask);
            }
        } else  {
            mThread.postTask(mTask);
        }
    }

    @Override
    public void stop(Action action) {
        if (mTask != null) {
            try {
                action.setEventPublisher(null);
                action.stop();
            } catch (ActionException e) {
                e.printStackTrace();
            }
            mThread.cancelTask(mTask);
        }
    }

    private Runnable realTask(PostAction action) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Object result = action.execute();
                    action.setResponse(result);
                    action.setEventPublisher(null);
                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ActionMonitor.getInstance().notifySuccessAction(action);
                        }
                    });
                } catch (ActionException e) {
                    e.printStackTrace();
                    mUIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            action.setEventPublisher(null);
                            action.setError(e);
                            ActionMonitor.getInstance().notifyErrorAction(action);
                        }
                    });
                } finally {
                    if (mPolicy == ACTION_POST_POLICY.REPEAT
                            || mPolicy == ACTION_POST_POLICY.REPEAT_INCLUDE) {
                        PostAction nextPostAction = new PostAction(action.getOriginAction(),
                                mIDGenerator.generate(action.getOriginAction()));
                        mThread.postTaskDelay(createRepeatRunnable(nextPostAction), mTimeInMillis);
                    }
                }
            }
        };
    }

    private Runnable createRepeatRunnable(PostAction action) {
        return new Runnable() {
            @Override
            public void run() {
                //Enqueue action with collected registered callback
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        action.setError(null);
                        action.setEventPublisher(null);
                        action.setResponse(null);
                        action.canExecute(false);
                        List<ActionCallbackInfo> callbackList = ActionMonitor.getInstance()
                                .findCallbacksRelatedAction(action.getOriginAction().getID());
                        if (callbackList == null) {
                            callbackList = new ArrayList<>();
                        }

                        ActionMonitor.getInstance().enqueueAction(mActionManagerId,
                                action, callbackList,
                                PostTaskScheduler.this);
                    }
                });
            }
        };
    }
}
