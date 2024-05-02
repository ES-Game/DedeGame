package com.quangph.base.mvp.action.actionhandler;


import com.quangph.base.dispatcher.IDispatcher;

/**
 * Created by Pham Hai Quang on 9/23/2019.
 */
public interface IActionHandler extends IDispatcher<IActionHandler.ActionDispatcherInfo> {
    int RESUME = 1;
    int EXECUTE = 3;
    int START_ACTION = 4;
    int SHOW_RESULT = 5;
    int SHOW_ERROR = 6;
    int SHOW_PROGRESS = 7;
    int STOP_ACTION = 8;
    int STOP_ACTION_BY_ID = 10;
    int STOP_ACTION_BY_TYPE = 13;
    int STOP_ACTIONS = 14;
    int REGISTER_CALLBACK = 9;
    int FORCE_STOP_ACTION = 11;
    int POST_RUNNABLE = 12;
    int STOP_ALL_ACTION = 15;
    int RELEASE_IMMEDIATE = 16;

    class ActionDispatcherInfo {
        public int what;
        public Object obj;
    }
}
