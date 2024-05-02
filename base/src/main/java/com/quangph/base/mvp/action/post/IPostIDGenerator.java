package com.quangph.base.mvp.action.post;

import com.quangph.base.mvp.action.uid.IIDGenerator;

public interface IPostIDGenerator extends IIDGenerator {
    void remove(String actionID);
    String getCurrentID(String originalActionID);
}
