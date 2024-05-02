package com.quangph.base.mvp;

/**
 * Created by Pham Hai Quang on 1/8/2019.
 */
public interface IPresenter extends ICommandExecutor {
    void presenterReady();
    void presenterRelease();

    /**
     * Check to intercept command.
     * @param command
     * @return true if command is not executed, false otherwise
     */
    boolean onInterceptCommand(ICommand command);
    void setMVPView(IView view);
}
