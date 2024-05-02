package com.quangph.base.statemachine;

import android.os.Message;

import androidx.annotation.Nullable;

import com.quangph.base.statemachine.core.State;

/**
 * Created by QuangPH on 2020-05-20.
 */
public class UIState extends State {

    private String mName;
    private boolean isNeedTransitToParent = false;
    private TransitionInfo mTransitionInfo;

    @Override
    public void enter() {
        super.enter();
//        if (isNeedTransitToParent) {
//            isNeedTransitToParent = false;
//            handleTransitFrom();
//            return;
//        }
        if (checkTransitFromChild()) {
            handleTransitFrom();
        } else {
            onEnter();
            if (mTransitionInfo != null) {
                handleTransitFrom();
            }
        }
        mTransitionInfo = null;
    }

    @Override
    public void exit() {
        super.exit();
//        if (isNeedTransitToParent) {
//            return;
//        }

        if (checkTransitFromChild()) {
            return;
        }

        if (mTransitionInfo != null) {
            transitTo(mTransitionInfo.toState.getName());
        }
        onExit();
    }

    public boolean processMessage(Message msg) {
        if (msg.what == UIStateMachine.TRANSIT_STATE_CMD) {
            TransitionInfo transitionInfo = (TransitionInfo) msg.obj;
            if (transitionInfo.transitToChild && transitionInfo.fromStateName.equals(getName())) {
                transitTo(transitionInfo.toState.getName());
                //isNeedTransitToParent = true;
            }
            return true;
        }

        return super.processMessage(msg);
    }

//    @Override
//    public boolean processMessage(Message msg) {
//        if (msg.what == UIStateMachine.TRANSIT_TO_PARENT_STATE_CMD) {
//            /*String name = (String) msg.obj;
//            if (name.equals(getClass().getName())) {
//                isNeedTransitToParent = true;
//                return true;
//            }*/
//
//            /**
//             * Current state call transitTo, then this message is dispatch to parent
//             */
//            TransitionInfo transitionInfo = (TransitionInfo) msg.obj;
//            if (transitionInfo.fromStateName.equals(getName())) {
//                handleTransit(transitionInfo);
//            }
//
//            /**
//             * This message was dispatched from child, so the parent do not need call onEnter
//             */
//            if (transitionInfo.toState.getName().equals(getName())) {
//                isNeedTransitToParent = true;
//                return true;
//            }
//        }
//
//        if (msg.what == UIStateMachine.TRANSIT_STATE_CMD) {
//            TransitionInfo transitionInfo = (TransitionInfo) msg.obj;
//            handleTransit(transitionInfo);
//            return true;
//        }
//        return super.processMessage(msg);
//    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void transitTo(String nextStateName) {}
    public void transitFrom(@Nullable String prevStateName) {}

    public void onEnter() {}
    public void onExit() {}

    /**
     * This fun is called by the previous state
     * @param info
     */
    public void setTransitionInfo(TransitionInfo info) {
        mTransitionInfo = info;
    }

    private void handleTransit(TransitionInfo transitionInfo) {
        transitTo(transitionInfo.toState.getName());
        transitionInfo.toState.setTransitionInfo(transitionInfo);
    }

    private void handleTransitFrom() {
        if (mTransitionInfo == null) {
            transitFrom(null);
        } else {
            transitFrom(mTransitionInfo.fromStateName);
        }
    }

    private boolean checkTransitFromChild() {
        if (mTransitionInfo == null) {
            return false;
        }

        return mTransitionInfo.transitToParent && mTransitionInfo.toState.getName().equals(getName());
    }
}
