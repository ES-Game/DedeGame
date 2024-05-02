package com.quangph.base.statemachine;

/**
 * Created by QuangPH on 2020-10-03.
 */
public class TransitionInfo {
    public String fromStateName;
    public UIState toState;

    public boolean transitToParent = false;
    public boolean transitToChild = false;
}
