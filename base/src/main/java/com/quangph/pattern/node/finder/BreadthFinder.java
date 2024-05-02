package com.quangph.pattern.node.finder;

import com.quangph.pattern.node.INode;
import com.quangph.pattern.spec.ISpecification;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFinder<T> implements IPathFinder<T> {

    private final Queue<INode<T>> mQueue = new LinkedList<INode<T>>();

    @Override
    public INode<T> findFirst(ISpecification<INode<T>> spec, INode<T> fromNode) {
        mQueue.offer(fromNode);
        INode<T> result = null;
        while (!mQueue.isEmpty()) {
            INode<T> first = mQueue.poll();
            if (first == null) {
                continue;
            }

            if (spec.isSatisfiedBy(first)) {
                result = first;
                break;
            }

            if (first.getChildCount() > 0) {
                for (INode<T> child : first.getChildren()) {
                    mQueue.offer(child);
                }
            }
        }
        return result;
    }

    @Override
    public List<INode<T>> findPath(ISpecification<INode<T>> spec, INode<T> fromNode) {
        INode<T> target = findFirst(spec, fromNode);
        if (target == null) {
            return new ArrayList<>();
        }
        ISpecification<INode<T>> specToFromNode = new ISpecification<INode<T>>() {
            @Override
            public boolean isSatisfiedBy(INode<T> node) {
                return node.equals(fromNode);
            }
        };
        return target.getPathToRoot(specToFromNode);
    }
}
