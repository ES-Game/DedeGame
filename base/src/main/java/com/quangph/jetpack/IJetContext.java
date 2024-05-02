package com.quangph.jetpack;

import android.app.Activity;
import android.content.Context;

import androidx.activity.result.ActivityResult;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.quangph.base.common.ActivityNavi;

/**
 * Created by Pham Hai Quang on 10/16/2019.
 */
public interface IJetContext {
    @Nullable Activity getActivityContext();
    String getScreenName();
    void showLoading();
    void showLoading(String tag);
    void hideLoading();
    void hideLoading(String tag);
    void showAlert(String msg, String type);
    FragmentManager requestFragmentManager();
    <ACTIVITY extends Activity> void navigate(Class<ACTIVITY> clazz,
                                              @Nullable IScreenData data,
                                              @Nullable ActivityNavi.OnActivityResult<ActivityResult> callback);
}
