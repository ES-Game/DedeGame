package com.quangph.base.viewmodel.livedata.collection;

import java.util.Collection;

/**
 * Created by QuangPH on 2020-11-16.
 */
public class CollectionLiveDataObserver<E, COL extends Collection<E>> implements ICollectionLiveDataObserver<E, COL> {
    @Override
    public void onAdd(E elm, int index) {

    }

    @Override
    public void onAddAll(COL elmCol, int index) {

    }

    @Override
    public void onRemove(E elm, int index) {

    }

    @Override
    public void onRemoveAll(COL elmCol) {

    }

    @Override
    public void onReplace(E old, E newElm, int index) {

    }

    @Override
    public void onChanged(COL col) {

    }
}
