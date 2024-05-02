package com.quangph.pattern.node.traverse;

import com.quangph.pattern.node.INode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by quangph on 3/12/2016.
 */
public class BreadthTraverser<T> implements INodeTraverser<T> {

    private Queue<INode<T>> mQueue = new LinkedList<INode<T>>();

    private final INodeVisitor<T> mVisitor;

    public BreadthTraverser(INodeVisitor<T> visitor) {
        this.mVisitor = visitor;
    }

    @Override
    public boolean traverse(INode<T> root) {
        if (root == null) {
            return false;
        }

        mQueue.add(root);
        while (!mQueue.isEmpty()) {
            INode<T> element = mQueue.poll();
            boolean result = mVisitor.visitor(element);
            if (result) {
                return true;
            }

            mQueue.add(root);
            List<INode<T>> children = root.getChildren();
            if (children != null) {
                for (INode<T> child : children) {
                    mQueue.offer(child);
                }
            }
        }
        return false;
    }
}
