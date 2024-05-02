package com.quangph.pattern.node.traverse;

import com.quangph.pattern.node.INode;

import java.util.List;

/**
 * Created by quangph on 3/11/2016.
 */
public class DepthTraverser<T> implements INodeTraverser<T> {
    private final INodeVisitor<T> mVisitor;

    public DepthTraverser(INodeVisitor<T> visitor) {
        this.mVisitor = visitor;
    }

    @Override
    public boolean traverse(INode<T> root) {
        if (root == null) {
            return false;
        }

        if (mVisitor.visitor(root)) {
            return true;
        }

        List<INode<T>> children = root.getChildren();
        if (children != null) {
            for (INode<T> child : children) {
                boolean result = traverse(child);
                if (result) {
                    return true;
                }
            }
        }
        return false;
    }
}
