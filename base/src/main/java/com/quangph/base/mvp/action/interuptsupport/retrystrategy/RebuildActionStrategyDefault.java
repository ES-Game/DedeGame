package com.quangph.base.mvp.action.interuptsupport.retrystrategy;

import com.quangph.pattern.node.INode;
import com.quangph.pattern.node.traverse.DepthTraverser;
import com.quangph.pattern.node.traverse.INodeTraverser;
import com.quangph.pattern.node.traverse.INodeVisitor;
import com.quangph.base.mvp.action.Action;
import com.quangph.base.mvp.action.interuptsupport.ActionNode;
import com.quangph.base.mvp.action.interuptsupport.RetryActionBuilder;
import com.quangph.base.mvp.action.interuptsupport.RetryActionManager;

/**
 * Created by QuangPH on 2020-10-01.
 */
public class RebuildActionStrategyDefault implements IRebuildActionStrategy {

    @Override
    public RetryActionBuilder build(RetryActionManager manager, ActionNode root) {
        final RetryActionBuilder builder = new RetryActionBuilder(manager);
        INodeTraverser<Action> traverser = new DepthTraverser<Action>(new INodeVisitor<Action>() {
            @Override
            public boolean visitor(INode<Action> node) {
                ActionNode actionNode = (ActionNode) node;
                if (actionNode.getData().isError()) {
                    builder.add(actionNode.getData(), actionNode.getData().getRequestVal(), actionNode.callback, actionNode.scheduler);
                }
                return false;
            }
        });
        traverser.traverse(root);

        builder.onCompound(root.parallelCallback);
        builder.getRoot().isRoot = true;
        return builder;
    }
}
