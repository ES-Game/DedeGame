package com.quangph.base.mvp;

/**
 * Created by Pham Hai Quang on 1/8/2019.
 */
public interface IView extends ICommandExecutor {
    interface Status {
        int VIEW_CREATE = 0;
        int VIEW_INITING = 1;
        int VIEW_INITED = 3;
        int VIEW_READY = 4;
        int VIEW_DESTROY = 5;
    }

    void setStatus(int status);
    int getStatus();
    void attachPresenter(IPresenter presenter);
    boolean hasMVPChildren();
    void onInitView();
    void onPortrait();
    void onLandscape();
    boolean isPortrait();
    void setMVPViewParent(IView parent);
}
