package com.quangph.base.dispatcher;

/**
 * Created by Pham Hai Quang on 9/23/2019.
 */
public interface IDispatcher<E> {
    void start();
    void stop();
    int size();
    void dispatch(E event);
    void dispatchAtFrontOfQueue(E event);
    void dispatchToEndOfQueue(E event);
}
