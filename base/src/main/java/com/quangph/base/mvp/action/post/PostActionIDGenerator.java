package com.quangph.base.mvp.action.post;

import com.quangph.base.mvp.action.Action;

import java.util.HashMap;
import java.util.Map;

public class PostActionIDGenerator implements IPostIDGenerator {

    private final Map<String, ActionInfo> mActionInfoCache = new HashMap<>();

    @Override
    public String generate(Action action) {
        synchronized (PostActionIDGenerator.class) {
            ActionInfo info = mActionInfoCache.get(action.getID());
            if (info == null) {
                info = new ActionInfo();
                info.originalID = action.getID();
                mActionInfoCache.put(action.getID(), info);
            }
            return info.generate();
        }
    }

    @Override
    public void remove(String actionID) {
        mActionInfoCache.remove(actionID);
    }

    @Override
    public String getCurrentID(String originalActionID) {
        ActionInfo info = mActionInfoCache.get(originalActionID);
        if (info != null) {
            return info.getCurrent();
        }
        return null;
    }

    private class ActionInfo {
        String originalID;
        int version;

        String generate() {
            version++;
            return originalID + "_PostActionIDGenerator_" + version;
        }

        String getCurrent() {
            return originalID + "_PostActionIDGenerator_" + version;
        }
    }
}
