package com.quangph.base.mvp.mvpcomponent;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by QuangPH on 2020-11-24.
 */
public class ViewModelFinderReflection implements IViewModelFinder {
    @Nullable
    @Override
    public ViewModel findViewModel(FragmentActivity activity) {
        Package currPackage = activity.getClass().getPackage();
        if (currPackage == null) {
            return null;
        } else {
            String pkgName = currPackage.getName();
            String actName = activity.getClass().getSimpleName();
            try {
                Class vmClass = Class.forName(pkgName + "." + generateViewModelName(actName));
                if (ViewModel.class.isAssignableFrom(vmClass)) {
                    return new ViewModelProvider(activity).get(vmClass);
                } else {
                    return null;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                Log.e("KMVPActivity", "ViewModel for activity: " + actName + " must be: " + pkgName + "." + generateViewModelName(actName));
                return null;
            }
        }
    }

    private String generateViewModelName(String clazzSimpleName) {
        return clazzSimpleName.replace("Activity", "") + "ViewModel";
    }
}
