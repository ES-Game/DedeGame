package com.quangph.base.mvp.action.uid;

import com.quangph.base.mvp.action.Action;

public class IDGenerator implements IIDGenerator {

    @Override
    public String generate(Action action) {
        return action.getClass().getName();
    }
}
