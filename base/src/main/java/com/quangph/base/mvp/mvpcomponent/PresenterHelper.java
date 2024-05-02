package com.quangph.base.mvp.mvpcomponent;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quangph.base.mvp.IChildPresenter;
import com.quangph.base.mvp.ICommand;
import com.quangph.base.mvp.IParentCommand;
import com.quangph.base.mvp.IParentPresenter;
import com.quangph.base.mvp.IView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PresenterHelper {

    private final String mTag;
    private final List<IChildPresenter> mChildrenPresenter = new ArrayList<>();

    public PresenterHelper(String tag) {
        mTag = tag;
    }

    public List<IChildPresenter> getChildren() {
        return mChildrenPresenter;
    }

    public void release() {
        for (IChildPresenter child : mChildrenPresenter) {
            child.presenterRelease();
        }
        mChildrenPresenter.clear();
    }

    public void removeChildPresenter(IChildPresenter childPresenter) {
        mChildrenPresenter.remove(childPresenter);
    }

    public void removeChildPresenter(@NonNull String tag) {
        if (tag == null) {
            Log.e(mTag, "Can not remove presenter with tag = null");
        }

        Iterator<IChildPresenter> itr = mChildrenPresenter.iterator();
        while (itr.hasNext()) {
            IChildPresenter next = itr.next();
            if (tag.equals(next.getTagName())) {
                itr.remove();
                break;
            }
        }
    }

    public void addChildPresenter(IChildPresenter childPresenter, @Nullable String tag) {
        if (tag != null) {
            childPresenter.setTagName(tag);
        }
        if (!mChildrenPresenter.contains(childPresenter)) {
            mChildrenPresenter.add(childPresenter);
        }
    }

    public void addChildPresenter(@NonNull IChildPresenter presenter, @NonNull IView view) {
        addChildPresenter(presenter, view, null);
    }

    public void addChildPresenter(@NonNull IChildPresenter presenter, @NonNull IView view, @Nullable String tag) {
        addChildPresenter(presenter, tag);
        presenter.setMVPView(view);
        //view.attachPresenter(presenter);
        //presenter.presenterReady();
    }

    public void dispatchToChildren(ICommand command, @Nullable String[] childrenTag) {
        if (childrenTag == null || childrenTag.length == 0) {
            for (IChildPresenter child : mChildrenPresenter) {
                child.executeCommand(command);
            }
        } else {
            for (String tag : childrenTag) {
                dispatchToChild(command, tag);
            }
        }
    }

    public IChildPresenter findPresenterByTag(@NonNull String tag) {
        IChildPresenter presenter = null;
        for (IChildPresenter child : mChildrenPresenter) {
            if (tag.equals(child.getTagName())) {
                presenter = child;
                break;
            }
        }

        if (presenter == null) {
            Log.e(mTag, "Can not find presenter with tag: " + tag);
        }
        return presenter;
    }

    public void readyChild(@NonNull String childTag) {
        IChildPresenter child = null;
        for (IChildPresenter itm : mChildrenPresenter) {
            if (childTag.equals(itm.getTagName())) {
                child = itm;
                break;
            }
        }
        if (child != null) {
            child.presenterReady();
        }
    }

    private void dispatchToChild(ICommand command, @NonNull String childTag) {
        IChildPresenter childHasTag = findPresenterByTag(childTag);
        if (childHasTag != null) {
            childHasTag.executeCommand(command);
        }
    }
}
