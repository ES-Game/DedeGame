package com.quangph.pattern.node.traverse;

import com.quangph.pattern.node.INode;

/**
 * Created by QuangPH on 7/13/2016.
 */
public interface INodeVisitor<T> {
    boolean visitor(INode<T> node);
}
