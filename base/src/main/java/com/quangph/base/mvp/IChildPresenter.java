package com.quangph.base.mvp;

/**
 * Created by QuangPH on 2020-03-13.
 */
public interface IChildPresenter extends IPresenter {
    IParentPresenter getParentPresenter();
    void setParentPresenter(IParentPresenter parent);
    void dispatchToParent(IParentCommand command);
    String getTagName();
    void setTagName(String tag);
}
