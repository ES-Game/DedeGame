package com.quangph.base.mvp.action.actionjob;

/**
 * Created by QuangPH on 2020-05-05.
 */
public abstract class ActionJob<T> {
    private boolean isInterrupted = false;

    public abstract T submitJob() throws Exception;
    public void onResult(T result){}
    public void onException(Throwable e){}

    public void stop() {
        isInterrupted = true;
    }

    public boolean isInterrupted() {
        return isInterrupted;
    }
}
