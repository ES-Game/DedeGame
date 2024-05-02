package com.quangph.base.mvp.action;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quangph.base.mvp.action.actionjob.ActionJob;
import com.quangph.base.mvp.action.actionjob.ActionWorker;
import com.quangph.base.mvp.action.uid.IDGenerator;
import com.quangph.base.mvp.action.uid.IIDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pham Hai Quang on 1/4/2019.
 */
public abstract class Action<R extends Action.RequestValue, T> {

    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int RUNNING = 3;
    public static final int PENDING = 4;
    public static final int INTERRUPT = 5;
    public static final int START = 6;

    private R mRequestVal;
    private T mResponseVal;
    private IOnEventPublisher mPublisher;
    private ActionException mError;
    private Object mCurrentEvent;
    private int mStatus = PENDING;
    private boolean canExecute;
    private IIDGenerator mIDGenerator;

    private ActionWorker mWorker;

    public void start() throws ActionException {
        try {
            onStart(mRequestVal);
        } catch (Exception e) {
            throw new ActionException(e);
        }
    }

    public T execute() throws ActionException {
        if (!canExecute) {
            throw new ActionStartErrorException();
        }

        try {
            return onExecute(mRequestVal);
        } catch (Exception e) {
            throw new ActionException(e);
        }
    }

    public void stop() throws ActionException{
        mStatus = INTERRUPT;
        if (mWorker != null) {
            mWorker.release();
        }

        try {
            onStop();
        } catch (Exception e) {
            throw new ActionException(e);
        }
    }

    public void setRequestValue(R requestValue) {
        mRequestVal = requestValue;
    }

    public void setError(ActionException error) {
        this.mError = error;
    }

    public void setResponse(T response) {
        mResponseVal = response;
    }

    public void setCurrentEvent(Object event) {
        mCurrentEvent = event;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public void setEventPublisher(IOnEventPublisher publisher) {
        mPublisher = publisher;
    }

    public void canExecute(boolean canExecute) {
        this.canExecute = canExecute;
    }

    public int getStatus() {
        return mStatus;
    }

    public @Nullable ActionException getError() {
        return mError;
    }

    public T getResponseVal() {
        return mResponseVal;
    }

    public R getRequestVal() {
        return mRequestVal;
    }

    public String getID() {
        return createID();
    }

    public Object getCurrentEvent() {
        return mCurrentEvent;
    }

    public boolean isSuccessful() {
        return mStatus == SUCCESS;
    }

    public boolean isError() {
        return mStatus == ERROR;
    }

    public boolean isInterrupt() {
        return mStatus == INTERRUPT;
    }

    public boolean isFinished() {
        return mStatus != RUNNING && mStatus != PENDING && mStatus != INTERRUPT;
    }

    public void publishEvent(Object event) {
        if (mPublisher != null) {
            mPublisher.onPublishEvent(event);
        }
    }

    public<T> Builder backgroundJob(ActionJob<T> job) {
        Builder builder = new Builder();
        builder.mAction = this;
        return builder.addJob(job);
    }

    public Builder backgroundJobBuilder() {
        Builder builder = new Builder();
        builder.mAction = this;
        return builder;
    }

    protected abstract T onExecute(R requestValue) throws Exception;
    protected void onStart(R requestValue) throws Exception {}
    protected void onStop() throws Exception {}

    protected String createID() {
        //return getClass().getName();
        return getIDGenerator().generate(this);
    }

    protected IIDGenerator getIDGenerator() {
        if (mIDGenerator == null) {
            mIDGenerator = createIDGenerator();
        }
        return mIDGenerator;
    }

    protected IIDGenerator createIDGenerator() {
        return new IDGenerator();
    }


    /**********************************************************************************************/

    public interface RequestValue{}

    public static class VoidRequest implements RequestValue{}

    public interface ActionCallback<T> {
        void onStart();
        void onStartError(ActionException e);
        void onPublishProgress(Object event);
        void onSuccess(T responseValue);
        void onError(@NonNull ActionException e);
        void onInterrupt();
    }

    public static class SimpleActionCallback<T> implements ActionCallback<T> {

        @Override
        public void onStart() {

        }

        @Override
        public void onStartError(ActionException e) {

        }

        @Override
        public void onPublishProgress(Object event) {

        }

        @Override
        public void onSuccess(T responseValue) {

        }

        @Override
        public void onError(@NonNull ActionException e) {

        }

        @Override
        public void onInterrupt() {

        }
    }

    public interface IOnEventPublisher {
        void onPublishEvent(Object event);
    }

    /***********************************************************************************************/

    public static class Builder {

        private List<ActionJob> mActionJobList = new ArrayList<>();
        private ActionWorker.IOnException mExceptionHandler;
        private Action mAction;

        public Builder(){}

        public Builder addJob(ActionJob job) {
            mActionJobList.add(job);
            return this;
        }

        public Builder exceptionHandler(ActionWorker.IOnException exceptionHandler) {
            mExceptionHandler = exceptionHandler;
            return this;
        }

        public void execute(long timeoutMillis) {
            ActionWorker worker = new ActionWorker();
            worker.setExceptionHandler(mExceptionHandler);
            for (ActionJob job : mActionJobList) {
                worker.submitJob(job);
            }
            mAction.mWorker = worker;
            worker.execute(timeoutMillis);
        }
    }
}
