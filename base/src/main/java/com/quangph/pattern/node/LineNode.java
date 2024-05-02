package com.quangph.pattern.node;

/**
 * The node has only one child
 * Created by QuangPH on 2020-12-01.
 */
public class LineNode<T> extends SimpleNode<T> {

    public LineNode(T data) {
        super(data);
    }

    @Override
    public void addChild(INode<T> childNode) {
        if (childNode instanceof LineNode) {
            getChildren().clear();
            super.addChild(childNode);
        } else {
            throw new IllegalArgumentException("child node must be LineNode");
        }

    }

    public LineNode<T> getLatest() {
        if (getChildCount() == 0) {
            return this;
        } else {
            return ((LineNode<T>)getChildren().get(0)).getLatest();
        }
    }

    public LineNode<T> getNext() {
        if (getChildCount() == 0) {
            return null;
        }
        return (LineNode<T>) getChildren().get(0);
    }
}
