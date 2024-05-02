package com.quangph.base.mvp.action.interuptsupport.retrystrategy;

import com.quangph.base.mvp.action.interuptsupport.ActionNode;
import com.quangph.base.mvp.action.interuptsupport.RetryActionBuilder;
import com.quangph.base.mvp.action.interuptsupport.RetryActionManager;

/**
 * Created by QuangPH on 2020-09-29.
 */
public interface IRebuildActionStrategy {
    RetryActionBuilder build(RetryActionManager manager, ActionNode root);
}
