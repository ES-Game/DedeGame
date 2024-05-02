package com.quangph.base.mvp.action.actionjob;

/**
 * Created by QuangPH on 2020-03-23.
 */
public abstract class SimpleActionJob<T> extends ActionJob<T> {

    @Override
    public void onResult(T result) {

    }

    @Override
    public void onException(Throwable e) {

    }
}
