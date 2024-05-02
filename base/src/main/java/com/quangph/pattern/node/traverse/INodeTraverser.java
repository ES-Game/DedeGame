package com.quangph.pattern.node.traverse;

import com.quangph.pattern.node.INode;

/**
 * Created by quangph on 3/12/2016.
 */
public interface INodeTraverser<T> {
    boolean traverse(INode<T> root);
}
