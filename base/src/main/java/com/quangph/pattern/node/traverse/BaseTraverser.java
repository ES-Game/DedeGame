package com.quangph.pattern.node.traverse;

/**
 * Created by quangph on 3/12/2016.
 */
public abstract class BaseTraverser<T> implements INodeTraverser<T> {

    public interface Processor<T> {
        boolean process(T node);
    }

    protected int getChildCount(T node) {
        throw new IllegalStateException("Must be override getChildCount()");
    }

    protected T getChildAt(T parent, int index) {
        throw new IllegalStateException("Must be override getChildAt()");
    }
}
