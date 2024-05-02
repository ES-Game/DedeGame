package com.quangph.jetpack;


import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IParentPresenter;
import com.quangph.base.mvp.IView;

/**
 *
 * Created by quangph on 1/3/2020
 */
public class JetTabFragment<V extends IView> extends JetFragment<V> {

    private Config mConfig;
    private boolean isShowing = false;

    @Override
    protected void onShow(boolean isShowing, int flag) {
        super.onShow(isShowing, flag);
        this.isShowing = isShowing;
        if (isShowing) {
            IParentPresenter presenter = getParentPresenter();
            if (presenter != null) {
                presenter.addChildPresenter(this, getScreenName());
            }
        } else {
            IParentPresenter presenter = getParentPresenter();
            if (presenter != null) {
                presenter.removeChildPresenter(this);
            }
        }

        OnShowFragmentCmd cmd = new OnShowFragmentCmd();
        cmd.isShow = isShowing;
        cmd.fragment = this;
        getParentPresenter().executeCommand(cmd);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    public boolean isShowing() {
        return isShowing;
    }

    public final Config getConfig() {
        if (mConfig == null) {
            mConfig = onCreateConfig();
        }
        return mConfig;
    }

    public Config onCreateConfig() {
        return null;
    }


    public static class OnShowFragmentCmd implements ICommand {
        public boolean isShow;
        public JetTabFragment fragment;
    }

    public static class Config {}
}
