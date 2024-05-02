package com.quangph.pattern.node;

import androidx.annotation.NonNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by QuangPH on 7/15/2016.
 */
public class TreeBuilder<T> {
    public interface IChildrenCreator<T> {
        List<INode<T>> createChildren(INode<T> parent);
    }

    private IChildrenCreator<T> mChildrenCreator;
    private Queue<INode<T>> mQueue = new LinkedList<>();

    public TreeBuilder(IChildrenCreator<T> childrenCreator) {
        mChildrenCreator = childrenCreator;
    }

    public void createTree(@NonNull INode<T> root) {
        mQueue.add(root);
        while (!mQueue.isEmpty()) {
            INode<T> element = mQueue.poll();
            List<INode<T>> children = mChildrenCreator.createChildren(element);
            if (children != null && !children.isEmpty()) {
                for (INode<T> child : children) {
                    element.addChild(child);
                    mQueue.offer(child);
                }
            }
        }
    }
}
