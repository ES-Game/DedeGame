package com.quangph.pattern.node;

import com.quangph.pattern.node.finder.DepthFinder;
import com.quangph.pattern.node.finder.IPathFinder;
import com.quangph.pattern.spec.ISpecification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QuangPH on 7/15/2016.
 */
public class SimpleNode<T> implements INode<T> {
    private T mData;
    private List<INode<T>> mChildren = new ArrayList<>();
    private INode<T> mParent;

    public SimpleNode(){}

    public SimpleNode(T data) {
        mData = data;
    }

    @Override
    public void addChild(INode<T> childNode) {
        childNode.setParent(this);
        mChildren.add(childNode);
    }

    @Override
    public void removeChild(INode<T> child, boolean autoRebuild) {
        mChildren.remove(child);
        if (autoRebuild) {
            for (INode<T> node : child.getChildren()) {
                this.addChild(node);
            }
        }
    }

    @Override
    public void removeNode(INode<T> node, boolean autoRebuild) {
        INode<T> root = getRoot();
        INode<T> removalNode = new DepthFinder<T>().findFirst(new ISpecification<INode<T>>() {
            @Override
            public boolean isSatisfiedBy(INode<T> tiNode) {
                return tiNode.getData().equals(node.getData());
            }
        }, root);
        if (removalNode != null) {
            if (removalNode.getParent() == null) {
                if (getChildCount() == 1) {
                    getChildren().get(0).setParent(null);
                } else if (getChildCount() == 0) {
                    throw new IllegalArgumentException("I can not remove myself");
                } else {
                    throw new IllegalArgumentException("You can not remove root of children");
                }
            } else {
                removalNode.getParent().removeChild(node, autoRebuild);
            }
        }

    }

    @Override
    public void setParent(INode<T> parent) {
        mParent = parent;
    }

    @Override
    public List<INode<T>> getChildren() {
        return mChildren;
    }

    @Override
    public INode<T> getParent() {
        return mParent;
    }

    @Override
    public T getData() {
        return mData;
    }

    @Override
    public int getChildCount() {
        return mChildren.size();
    }

    @Override
    public INode<T> getRoot() {
        if (mParent == null) {
            return this;
        } else {
            return mParent.getRoot();
        }
    }

    @Override
    public List<INode<T>> getPathToRoot(ISpecification<INode<T>> spec) {
        List<INode<T>> nodeList = new ArrayList<>();
        INode<T> node = this;
        boolean isMatch = false;
        while (node != null) {
            nodeList.add(node);
            if (spec.isSatisfiedBy(node)) {
                isMatch = true;
                break;
            }
            node = node.getParent();
        }

        if (!isMatch) {
            nodeList.clear();
        }
        return nodeList;
    }
}
