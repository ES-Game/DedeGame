package com.quangph.base.mvp.action.dispatcher;


import com.quangph.base.mvp.action.queue.ActionEnqueue;

/**
 * Created by Pham Hai Quang on 6/21/2019.
 */
public interface IEnqueueDispatcher {
    void dispatch(ActionEnqueue actionEnqueue);
}
