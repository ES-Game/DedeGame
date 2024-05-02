package com.quangph.pattern.node.finder;

import com.quangph.pattern.node.INode;
import com.quangph.pattern.spec.ISpecification;

import java.util.ArrayList;
import java.util.List;

public class DepthFinder<T> implements IPathFinder<T> {
    @Override
    public INode<T> findFirst(ISpecification<INode<T>> spec, INode<T> fromNode) {
        if (spec.isSatisfiedBy(fromNode)) {
            return fromNode;
        }

        INode<T> result = null;
        if (fromNode.getChildCount() > 0) {
            for (INode<T> child : fromNode.getChildren()) {
                result = findFirst(spec, child);
                if (result != null) {
                    break;
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
