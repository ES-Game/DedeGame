package com.quangph.base.mvp.action;

import com.quangph.base.mvp.action.scheduler.IActionScheduler;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Pham Hai Quang on 6/21/2019.
 */
class ActionInfo {
    Action action;
    IActionScheduler scheduler;
    Map<ActionManager, Action.ActionCallback> callbackCache = new HashMap<>();
}
