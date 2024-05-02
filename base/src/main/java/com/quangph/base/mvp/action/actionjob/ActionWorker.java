package com.quangph.base.mvp.action.actionjob;


import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by QuangPH on 2020-03-23.
 */
public class ActionWorker {
    private final static int WAITING = 1;
    private final static int STOP = 2;

    private ActionThreadPoolExcutor mExecutor = new ActionThreadPoolExcutor();
    private List<Throwable> mExceptionList = new CopyOnWriteArrayList<>();
    private IOnException mExceptionHandler;
    private int mState = WAITING;

    public<T> void submitJob(ActionJob<T> job) {
        mExecutor.doActionJob(new WrapActionJob<T>(job));
    }

    public void setExceptionHandler(IOnException exceptionHandler) {
        mExceptionHandler = exceptionHandler;
    }

    public void execute(long timeoutMillis) {
        mExecutor.shutdown();
        try {
            boolean finished = mExecutor.awaitTermination(timeoutMillis, TimeUnit.MILLISECONDS);
            if (finished) {
                if (!mExceptionList.isEmpty() && mExceptionHandler != null) {
                    try {
                        mExceptionHandler.onException(mExceptionList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        release();
                    }
                }
            } else {
                if (mExceptionHandler != null) {
                    try {
                        mExceptionHandler.onNotFinished();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        release();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            release();
        }
    }

    public void release() {
        mState = STOP;
        mExecutor = null;
        mExceptionList.clear();
        mExceptionList = null;
        mExceptionHandler = null;
    }

    public void stop() {
        mExecutor.stop();
        release();
    }


    private class WrapActionJob<T> extends ActionJob<T> {

        ActionJob<T> mainJob;

        WrapActionJob(ActionJob<T> job) {
            mainJob = job;
        }

        @Override
        public T submitJob() throws Exception {
            return mainJob.submitJob();
        }

        @Override
        public void onResult(T result) {
            mainJob.onResult(result);
        }

        @Override
        public void onException(Throwable e) {
            mainJob.onException(e);
            if (mState != STOP) {
                mExceptionList.add(e);
            }
        }
    }


    public interface IOnException {
        void onException(List<Throwable> exceptionList);
        void onNotFinished();
    }
}
