package com.quangph.base.mvp.action.scheduler;

import android.os.AsyncTask;
import android.util.Log;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionException;
import com.quangph.base.mvp.action.ActionMonitor;

import java.lang.ref.WeakReference;


/**
 * Created by Pham Hai Quang on 1/4/2019.
 */
public class AsyncTaskScheduler implements IActionScheduler {

    private int mRetryCount;
    private boolean isOnExecutor;
    private ActionAsync mTask;

    public AsyncTaskScheduler() {
        this(false);
    }

    public AsyncTaskScheduler(boolean isOnExecutor) {
        this.isOnExecutor = isOnExecutor;
    }

    public AsyncTaskScheduler(boolean isOnExecutor, int retryCount) {
        this.isOnExecutor = isOnExecutor;
        this.mRetryCount = retryCount;
    }

    @Override
    public void execute(Action action) {
        mTask = new ActionAsync(action, this);
        if (isOnExecutor) {
            mTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            mTask.execute();
        }
    }

    @Override
    public void stop(Action action) {
        if (mTask != null) {
            action.setEventPublisher(null);
            try {
                action.stop();
            } catch (ActionException e) {
                e.printStackTrace();
            }
            mTask.cancel(true);
        }
    }

    private void notifyResult(Action action) {
        action.setEventPublisher(null);
        ActionMonitor.getInstance().notifySuccessAction(action);
    }

    private void notifyError(Action action) {
        action.setEventPublisher(null);
        ActionMonitor.getInstance().notifyErrorAction(action);
    }

    private void notifyProgress(Action action) {
        ActionMonitor.getInstance().notifyProgress(action);
    }

    private void notifyStartAction(Action action) {
        ActionMonitor.getInstance().notifyStartAction(action);
    }


    /**********************************************************************************************/

    private static class ActionAsync extends AsyncTask<Void, Object, Void> {

        Action action;
        WeakReference<AsyncTaskScheduler> schedulerWeakReference;
        int retryCount;

        public ActionAsync(Action action, AsyncTaskScheduler scheduler) {
            this.action = action;
            this.schedulerWeakReference = new WeakReference<>(scheduler);
            this.retryCount = scheduler.mRetryCount;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            action.setEventPublisher(new Action.IOnEventPublisher() {
                @Override
                public void onPublishEvent(Object event) {
                    action.setCurrentEvent(event);
                    publishProgress(event);
                }
            });
            AsyncTaskScheduler scheduler = schedulerWeakReference.get();
            if (scheduler != null) {
                scheduler.notifyStartAction(action);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            int retry = retryCount + 1;
            do {
                try {
                    Log.e("AsyncTaskScheduler", "Retry at: " + (retryCount + 1 - retry) + " times");
                    Object data = action.execute();
                    action.setResponse(data);
                    break;
                } catch (ActionException e) {
                    e.printStackTrace();
                    retry--;
                    if (retry == 0) {
                        action.setError(e);
                        break;
                    }
                }
            } while (retry > 0 && !isCancelled());

            return null;

            /*try {
                Object data = action.execute();
                action.setResponse(data);
                return null;
            } catch (ActionException e) {
                e.printStackTrace();
                action.setError(e);
                return null;
            }*/
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            AsyncTaskScheduler scheduler = schedulerWeakReference.get();
            if (scheduler != null) {
                scheduler.notifyProgress(action);
            }
        }

        @Override
        protected void onPostExecute(Void data) {
            super.onPostExecute(data);
            AsyncTaskScheduler scheduler = schedulerWeakReference.get();
            if (scheduler != null && !isCancelled()) {
                if (action.getError() == null)  {
                    scheduler.notifyResult(action);
                } else {
                    scheduler.notifyError(action);
                }
            }
        }
    }
}
