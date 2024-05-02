package com.quangph.base.viewmodel.livedata.collection;

import androidx.lifecycle.Observer;

import java.util.Collection;

/**
 * Created by QuangPH on 2020-11-09.
 */
public interface ICollectionLiveDataObserver<E, COL extends Collection<E>> extends Observer<COL> {

    void onAdd(E elm, int index);

    void onAddAll(COL elmCol, int index);

    void onRemove(E elm, int index);

    void onRemoveAll(COL elmCol);

    void onReplace(E old, E newElm, int index);
}
