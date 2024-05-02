package com.quangph.base.mvp.action;

import androidx.annotation.NonNull;

/**
 * Created by Pham Hai Quang on 6/22/2019.
 */

@SuppressWarnings("rawtypes")
public class ActionCallbackInfo {
    public IActionManager actionManager;
    public Action.ActionCallback callback;
    //public boolean managerOwnAction;

    public boolean isSameActionManager(@NonNull IActionManager manager) {
        return manager.equals(this.actionManager);
    }
}
