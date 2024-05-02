package com.quangph.base.mvp.action.interuptsupport;

import androidx.annotation.IntRange;

import com.quangph.base.mvp.action.CompoundCallback;
import com.quangph.pattern.node.SimpleNode;
import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.scheduler.IActionScheduler;

/**
 * Created by QuangPH on 2020-09-26.
 */
public class ActionNode extends SimpleNode<Action> {

    public static final int NONE = -1;
    public static final int AND = 1;
    public static final int ADD = 2;

    public  @IntRange(from = NONE, to = ADD) int buildType;
    public CompoundCallback parallelCallback;
    public Action.ActionCallback callback;
    public IActionScheduler scheduler;

    /**
     * Root node will call startFunction
     */
    public boolean isRoot = false;

    public ActionNode(Action data) {
        super(data);
    }

    public void addToEnd(ActionNode node) {
        ActionNode next = this;
        while (next != null) {
            if (next.getChildCount() == 0) {
                node.parallelCallback = next.parallelCallback;
                next.addChild(node);
                next = null;
            } else {
                next = (ActionNode) next.getChildren().get(0);
            }
        }
    }

    public void setParallelCallback(CompoundCallback callback) {
        ActionNode next = this;
        while (next != null) {
            next.parallelCallback = callback;
            if (next.getChildCount() == 0) {
                next = null;
            } else {
                next = (ActionNode) next.getChildren().get(0);
            }
        }
    }

    synchronized public boolean isFinishedAll() {
        boolean hasFinished = true;
        ActionNode node = findRoot();
        while (node != null) {
            if (node.getData().getStatus() == Action.START || node.getData().getStatus() == Action.RUNNING) {
                hasFinished = false;
                break;
            } else {
                if (node.getChildCount() == 0) {
                    node = null;
                } else {
                    node = (ActionNode) node.getChildren().get(0);
                }
            }
        }
        return hasFinished;
    }

    synchronized public ActionNode getRoot() {
        return findRoot();
    }

    synchronized public boolean hasError() {
        boolean hasError = false;
        ActionNode node = findRoot();
        while (node != null) {
            if (node.getData().isError()) {
                hasError = true;
                break;
            } else {
                if (node.getChildCount() == 0) {
                    node = null;
                } else {
                    node = (ActionNode) node.getChildren().get(0);
                }
            }
        }
        return hasError;
    }

    synchronized public boolean isRunning() {
        boolean hasRunning = true;
        ActionNode node = findRoot();
        while (node != null) {
            if (node.getData().getStatus() != Action.START && node.getData().getStatus() != Action.RUNNING) {
                hasRunning = false;
                break;
            } else {
                if (node.getChildCount() == 0) {
                    node = null;
                } else {
                    node = (ActionNode) node.getChildren().get(0);
                }
            }
        }
        return hasRunning;
    }

    synchronized public void setActionStatus(int status) {
        ActionNode node = findRoot();
        while (node != null) {
            node.getData().setStatus(status);
            if (node.getChildCount() == 0) {
                node = null;
            } else {
                node = (ActionNode) node.getChildren().get(0);
            }
        }
    }

    synchronized public void resetActionError() {
        ActionNode node = findRoot();
        while (node != null) {
            node.getData().setError(null);
            if (node.getChildCount() == 0) {
                node = null;
            } else {
                node = (ActionNode) node.getChildren().get(0);
            }
        }
    }

    public ActionNode findRoot() {
        ActionNode parent = this;
        while (parent.getParent() != null) {
            parent = (ActionNode) parent.getParent();
        }
        return parent;
    }

    public void release() {
        parallelCallback = null;
        callback = null;
        scheduler = null;
    }
}
