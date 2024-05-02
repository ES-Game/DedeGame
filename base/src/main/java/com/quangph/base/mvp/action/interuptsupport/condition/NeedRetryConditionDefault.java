package com.quangph.base.mvp.action.interuptsupport.condition;

import com.quangph.base.mvp.action.interuptsupport.ActionNode;

/**
 * Created by QuangPH on 2020-10-01.
 */
public class NeedRetryConditionDefault implements INeedRetryCondition {
    @Override
    public boolean needRetry(ActionNode root) {
        return root.hasError();
    }
}
