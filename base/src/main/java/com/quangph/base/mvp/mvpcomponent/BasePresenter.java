package com.quangph.base.mvp.mvpcomponent;

import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IPresenter;
import com.quangph.base.mvp.IView;

/**
 * Created by QuangPH on 2021-01-07.
 */
public class BasePresenter<V extends IView> implements IPresenter {
    protected V mView;

    @Override
    public void presenterReady() {

    }

    @Override
    public void presenterRelease() {

    }

    @Override
    public boolean onInterceptCommand(ICommand command) {
        return false;
    }

    @Override
    public void executeCommand(ICommand command) { }

    @Override
    public void setMVPView(IView view) {
        mView = (V) view;
    }
}
