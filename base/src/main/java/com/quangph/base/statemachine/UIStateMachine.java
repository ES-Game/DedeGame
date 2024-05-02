package com.quangph.base.statemachine;

import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.Nullable;

import com.quangph.base.statemachine.core.IState;
import com.quangph.base.statemachine.core.StateMachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Created by Pham Hai Quang on 8/23/2019.
 */
public class UIStateMachine {

    static int TRANSIT_TO_PARENT_STATE_CMD = -9998;
    static int TRANSIT_STATE_CMD = -9999;

    private StateMachine mStateMachine;
    private boolean isQuit;
    private boolean isStarted = false;
    private Map<String, UIState> mStateCached = new HashMap<>();
    private Stack<String> mStateStack = new Stack<>();
    private Map<UIState, StateInfo> mStateInfoMap = new HashMap<>();

    public UIStateMachine(String name) {
        mStateMachine = new StateMachine(name, Looper.getMainLooper()) {
            @Override
            public void unhandledMessage(Message msg) {
                super.unhandledMessage(msg);
                handleMessage(msg);
            }
        };
    }

    public void addState(String name, UIState state) {
        if (!isQuit) {
            state.setName(name);
            mStateCached.put(name, state);
            cacheStateInfo(state, null);

            mStateMachine.addState(state);
        }
    }

    public void addState(String name, UIState state, String parentState) {
        if (!isQuit) {
            state.setName(name);
            mStateCached.put(name, state);
            cacheStateInfo(state, parentState);

            mStateMachine.addState(state, mStateCached.get(parentState));
        }
    }

    public UIState getState(String name) {
        return mStateCached.get(name);
    }

    public void initState(String name) {
        if (!isQuit) {
            mStateMachine.setInitialState(mStateCached.get(name));
            mStateStack.push(name);
        }
    }

    public void start() {
        isQuit = false;
        isStarted = true;
        mStateMachine.start();
    }

    public void quit() {
        isQuit = true;
        mStateCached.clear();
        mStateStack.clear();
        mStateInfoMap.clear();
        if (isStarted) {
            isStarted = false;
            mStateMachine.quit();
        }
    }

    /**
     * Transit from A to B: A.transitTo -> A.exit -> B.enter -> B.transitFrom
     * @param stateName
     */
    public void transitTo(String stateName) {
        if (!isQuit) {
            if (!mStateCached.containsKey(stateName)) {
                log("Cannot find state for name: " + stateName);
                return;
            }

            if (stateName.equals(getCurrentStateName())) {
                log("You are already here: " + stateName);
                return;
            }

            UIState destState = mStateCached.get(stateName);

            UIState currentState = (UIState) mStateMachine.getCurrentState();
            StateInfo info = mStateInfoMap.get(currentState);
            TransitionInfo transitionInfo = new TransitionInfo();
            transitionInfo.fromStateName = info.state.getName();
            transitionInfo.toState = destState;
            transitionInfo.transitToParent = findAncestorNames(currentState).contains(stateName);
            transitionInfo.transitToChild = findAncestorNames(destState).contains(currentState.getName());

            currentState.setTransitionInfo(transitionInfo);
            destState.setTransitionInfo(transitionInfo);
            mStateMachine.transitionTo(destState);
            mStateMachine.sendMessage(
                    mStateMachine.obtainMessage(TRANSIT_STATE_CMD, transitionInfo));

//            if (stateName.equals(info.parentName)) {
//                mStateMachine.transitionTo(destState);
//                /*mStateMachine.sendMessage(
//                        mStateMachine.obtainMessage(TRANSIT_TO_PARENT_STATE_CMD, destState.getClass().getName()));*/
//                mStateMachine.sendMessage(
//                        mStateMachine.obtainMessage(TRANSIT_TO_PARENT_STATE_CMD, transitionInfo));
//            } else {
//                mStateMachine.transitionTo(destState);
//                mStateMachine.sendMessage(
//                        mStateMachine.obtainMessage(TRANSIT_STATE_CMD, transitionInfo));
//                //mStateMachine.sendMessage(TRANSIT_STATE_CMD);
//            }

            reorderStateStack(stateName);
        } else {
            log("StateMachine has quited. Cannot transit to state: " + stateName);
        }
    }

    public boolean back() {
        if (!isQuit) {
            if (mStateStack.size() <= 1) {
                return false;
            } else {
                String top = mStateStack.pop();
                String prev = mStateStack.peek();
                mStateStack.push(top);
                transitTo(prev);
                mStateStack.remove(top);
                return true;
            }
        }
        return false;
    }

    public @Nullable String getCurrentStateName() {
        if (mStateStack.isEmpty()) {
            return null;
        }
        return mStateStack.peek();
    }

    public final void sendMessage(int what) {
        if (!isQuit) {
            mStateMachine.sendMessage(what);
        }
    }

    public final void sendMessage(int what, Object obj) {
        if (!isQuit) {
            mStateMachine.sendMessage(what, obj);
        }
    }

    public void sendMessage(Message msg) {
        if (!isQuit) {
            mStateMachine.sendMessage(msg);
        }
    }

    public void handleMessage(Message msg) {}

    public final Message obtainMessage(int what, int arg1, int arg2, Object obj) {
        return mStateMachine.obtainMessage(what, arg1, arg2, obj);
    }

    public boolean isEmpty() {
        return mStateCached.isEmpty();
    }

    public boolean exits(String name) {
        return mStateCached.containsKey(name);
    }

    private void reorderStateStack(String top) {
        if (mStateStack.contains(top)) {
            mStateStack.remove(top);
            mStateStack.push(top);
        } else {
            mStateStack.push(top);
        }
    }

    private void cacheStateInfo(UIState state, String parentName) {
        StateInfo info = new StateInfo();
        info.state = state;
        info.parentName = parentName;
        mStateInfoMap.put(state, info);
    }

    private List<String> findAncestorNames(IState state) {
        List<String> ancestorList = new ArrayList<>();
        IState parent = mStateMachine.getParent(state);
        while (parent != null) {
            ancestorList.add(parent.getName());
            parent = mStateMachine.getParent(parent);
        }
        return ancestorList;
    }

    private void log(String msg) {
        Log.e("StateMachine", msg);
    }


    private static class StateInfo {
        UIState state;
        String parentName;
    }
}