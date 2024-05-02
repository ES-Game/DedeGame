package com.quangph.base.mvp.mvpcomponent;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

/**
 * Created by QuangPH on 2020-11-24.
 */
public interface IViewModelFinder {
    @Nullable ViewModel findViewModel(FragmentActivity activity);
}
