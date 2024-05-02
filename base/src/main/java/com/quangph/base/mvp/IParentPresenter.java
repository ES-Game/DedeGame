package com.quangph.base.mvp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * Created by QuangPH on 2020-03-13.
 */
public interface IParentPresenter extends IPresenter {
    List<IChildPresenter> getChildrenPresenter();
    void addChildPresenter(IChildPresenter childPresenter, @Nullable String tag);
    void removeChildPresenter(IChildPresenter childPresenter);
    void removeChildPresenter(@NonNull String tag);
    void dispatchToChildren(ICommand command, @Nullable String... childrenTag);

    /**
     * Intercept dispatch a command to children
     * @param command
     * @return true command is not dispatched to children, false otherwise
     */
    boolean onInterceptDispatchToChildren(ICommand command);
    IChildPresenter findPresenterByTag(@NonNull String tag);
    void readyChild(@NonNull String childTag);
}
