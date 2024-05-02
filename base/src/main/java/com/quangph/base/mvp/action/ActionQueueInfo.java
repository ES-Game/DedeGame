package com.quangph.base.mvp.action;

import com.quangph.base.mvp.action.scheduler.IActionScheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Pham Hai Quang on 6/21/2019.
 */
public class ActionQueueInfo {
    public String actionManagerId;
    public Action action;
    public List<ActionCallbackInfo> callbackList = new ArrayList<>();
    public IActionScheduler scheduler;

    public void removeCallback(IActionManager actionManager) {
        Iterator<ActionCallbackInfo> itr = callbackList.iterator();
        while (itr.hasNext()) {
            ActionCallbackInfo next = itr.next();
            if (next.actionManager.equals(actionManager)) {
                itr.remove();
            }
        }
    }

    public boolean hasCallbackRelatedToActionManager(IActionManager actionManager) {
        boolean hasCallback = false;
        for (ActionCallbackInfo aci : callbackList) {
            if (aci.isSameActionManager(actionManager)) {
                hasCallback = true;
                break;
            }
        }
        return hasCallback;
    }

    public boolean isOwner(IActionManager actionManager) {
        /*boolean isOwner = false;
        for (ActionCallbackInfo aci : callbackList) {
            if (aci.isSameActionManager(actionManager)) {
                isOwner = aci.managerOwnAction;
                if (isOwner) {
                    break;
                }
            }
        }
        return isOwner;*/
        return actionManager.getID().equals(actionManagerId);
    }
}
