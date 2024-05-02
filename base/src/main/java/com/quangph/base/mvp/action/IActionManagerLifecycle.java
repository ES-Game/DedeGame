package com.quangph.base.mvp.action;

public interface IActionManagerLifecycle {
    void pause(IActionManager manager);
    void resume(IActionManager manager);
    void release(IActionManager manager);
    void releaseAndStopAllAction(IActionManager manager);
}
