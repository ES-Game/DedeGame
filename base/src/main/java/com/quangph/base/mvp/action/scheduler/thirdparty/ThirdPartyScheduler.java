package com.quangph.base.mvp.action.scheduler.thirdparty;


import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.ActionException;
import com.quangph.base.mvp.action.scheduler.IActionScheduler;

/**
 * Created by Pham Hai Quang on 1/11/2019.
 */
public class ThirdPartyScheduler implements IActionScheduler {
    @Override
    public void execute(Action action) {
        action.setStatus(Action.RUNNING);
        try {
            action.execute();
        } catch (ActionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop(Action action) {
        try {
            action.stop();
        } catch (ActionException e) {
            e.printStackTrace();
        }
    }
}
