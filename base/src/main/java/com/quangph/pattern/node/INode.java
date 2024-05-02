package com.quangph.pattern.node;

import com.quangph.pattern.spec.ISpecification;

import java.util.List;

/**
 * Created by QuangPH on 7/13/2016.
 */
public interface INode<T> {
    void addChild(INode<T> childNode);
    void removeChild(INode<T> child, boolean autoRebuild);
    void removeNode(INode<T> child, boolean autoRebuild);
    void setParent(INode<T> parent);
    List<INode<T>> getChildren();
    INode<T> getParent();
    T getData();
    int getChildCount();
    INode<T> getRoot();
    List<INode<T>> getPathToRoot(ISpecification<INode<T>> spec);
}
