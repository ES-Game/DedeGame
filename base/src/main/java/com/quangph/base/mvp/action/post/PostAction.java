package com.quangph.base.mvp.action.post;

import androidx.annotation.Nullable;

import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionException;

public class PostAction<R extends Action.RequestValue, T> extends Action<R, T> {
    private Action<R, T> mOriginAction;
    private String mId;

    private int mStatus;

    public PostAction(Action<R, T> action, String id) {
        mOriginAction = action;
        mId = id;
        mStatus = action.getStatus();
    }

    @Override
    public void start() throws ActionException {
        mOriginAction.start();
    }

    @Override
    protected T onExecute(R requestValue) throws Exception {
        return null;
    }

    @Override
    public T execute() throws ActionException {
        return mOriginAction.execute();
    }

    @Override
    public void stop() throws ActionException {
        mOriginAction.stop();
    }

    @Override
    public void setRequestValue(R requestValue) {
        mOriginAction.setRequestValue(requestValue);
    }

    @Override
    public void setError(ActionException error) {
        mOriginAction.setError(error);
    }

    @Override
    public void setResponse(T response) {
        mOriginAction.setResponse(response);
    }

    @Override
    public void setCurrentEvent(Object event) {
        mOriginAction.setCurrentEvent(event);
    }

    @Override
    public void setStatus(int status) {
        mStatus = status;
    }

    @Override
    public void setEventPublisher(IOnEventPublisher publisher) {
        mOriginAction.setEventPublisher(publisher);
    }

    @Override
    public void canExecute(boolean canExecute) {
        mOriginAction.canExecute(canExecute);
    }

    @Override
    public int getStatus() {
        return mStatus;
    }

    @Override
    public @Nullable ActionException getError() {
        return mOriginAction.getError();
    }

    @Override
    public T getResponseVal() {
        return mOriginAction.getResponseVal();
    }

    @Override
    public R getRequestVal() {
        return mOriginAction.getRequestVal();
    }

    @Override
    public String getID() {
        return mId;
    }

    @Override
    public Object getCurrentEvent() {
        return mOriginAction.getCurrentEvent();
    }

    @Override
    public boolean isSuccessful() {
        return mStatus == SUCCESS;
    }

    @Override
    public boolean isError() {
        return mStatus == ERROR;
    }

    @Override
    public boolean isInterrupt() {
        return mStatus == INTERRUPT;
    }

    @Override
    public boolean isFinished() {
        return mStatus != RUNNING && mStatus != PENDING && mStatus != INTERRUPT;
    }

    @Override
    public void publishEvent(Object event) {
        mOriginAction.publishEvent(event);
    }

    public Action<R,T> getOriginAction() {
        return mOriginAction;
    }
}
