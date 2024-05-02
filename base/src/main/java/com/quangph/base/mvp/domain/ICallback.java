package com.quangph.base.mvp.domain;

/**
 * Created by Pham Hai Quang on 1/11/2019.
 */
public interface ICallback<T> {
    void onStart();
    void onSuccess(T response);
    void onError(Exception error);
    void onPublishEvent(Object event);
}
