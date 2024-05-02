package com.quangph.pattern;

import com.quangph.pattern.node.LineNode;
import com.quangph.pattern.node.SimpleNode;

/**
 * Chain of Responsibility pattern helper
 * Created by QuangPH on 2020-09-24.
 */
public abstract class CORBase<T> extends LineNode<Void> {

    public CORBase() {
        super(null);
    }

    abstract boolean onExecute(T data);

    public void execute(T data) {
        CORBase<T> root = (CORBase<T>) getRoot();
        if (root != null) {
            root.executeAndNext(data);
        }
    }

    public CORBase<T> then(CORBase<T> next) {
        addChild(next);
        return next;
    }

    public boolean executeAndNext(T data) {
        boolean result = onExecute(data);
        if (result) {
            return true;
        } else {
            if (getChildCount() > 0) {
                return ((CORBase<T>)getNext()).executeAndNext(data);
            } else {
                return false;
            }
        }
    }
}
