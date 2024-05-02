package com.quangph.pattern.node.finder;

import com.quangph.pattern.node.INode;
import com.quangph.pattern.spec.ISpecification;

import java.util.List;

public interface IPathFinder<T> {
    INode<T> findFirst(ISpecification<INode<T>> spec, INode<T> fromNode);
    List<INode<T>> findPath(ISpecification<INode<T>> spec, INode<T> fromNode);
}
