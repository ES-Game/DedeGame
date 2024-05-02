package com.quangph.base.mvp.action.interuptsupport.condition;

import com.quangph.base.mvp.action.interuptsupport.ActionNode;

/**
 * Created by QuangPH on 2020-10-01.
 */
public interface INeedRetryCondition {
    boolean needRetry(ActionNode root);
}
