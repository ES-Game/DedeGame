package com.quangph.base.mvp.action.scheduler;

import com.quangph.base.mvp.action.Action;

/**
 * Created by Pham Hai Quang on 1/4/2019.
 */
public interface IActionScheduler {
    void execute(Action action);
    void stop(Action action);
}
