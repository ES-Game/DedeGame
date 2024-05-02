package com.quangph.base.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.quangph.base.common.bus.AppBus;
import com.quangph.base.common.bus.IAppEvent;
import com.quangph.base.common.bus.IAppEventHandler;
import com.quangph.base.viewbinder.ViewBinder;

import java.util.Map;


/**
 * Created by Pham Hai QUANG on 6/2/2017.
 */

public class BaseFragment extends Fragment implements IAppEventHandler {

    public static final int FLAG_FIRST_SIGHT = 1;
    public static final int FLAG_SHOW_AGAIN = 2;
    public static final int FLAG_RESUME = 3;
    public static final int FLAG_HIDE = 4;

    private static final int SHOWING = 1;
    private static final int HAS_SHOW_TO_HIDDEN = 2;
    private static final int FIRST_SIGHT = 3;
    private static final int RESUME_STATE = 4;
    private static final int HIDDEN_TO_HIDDEN = 5;
    private static final int HIDDEN = 6;

    private boolean isUserSeen = false;
    private boolean isDisplayed = false;
    private boolean isViewCreated = false;
    private boolean isUsedInViewPager = false;

    private int mShowingStatus = 0;
    private Handler mHandler = new Handler();
    private Runnable mResumeRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isUsedInViewPager) {
                // If we have not call #Fragment.show/hide, it mean Fragment.onHiddenChanged is not called,
                // so this fragment is showing;
                // otherwise just call onShow on showing fragment
                if (mShowingStatus == RESUME_STATE) {
                    userFirstSight();
                } else if (mShowingStatus == HIDDEN){
                    mShowingStatus = SHOWING;
                    onShow(true, FLAG_RESUME);
                }
            } else {
                if (mShowingStatus == HIDDEN) {
                    mShowingStatus = SHOWING;
                    onShow(true, FLAG_RESUME);
                }
            }
        }
    };

    private boolean isAvailable;
    private ActivityNavi<Intent, ActivityResult> mActivityLauncher =
            ActivityNavi.registerActivityForResult(this);
    private ActivityNavi<String, Boolean> mPermissionLauncher =
            ActivityNavi.registerActivityForResult(this,
                    new ActivityResultContracts.RequestPermission());
    private ActivityNavi<String[], Map<String, Boolean>> mMultiPermissionLauncher =
            ActivityNavi.registerActivityForResult(this,
                    new ActivityResultContracts.RequestMultiplePermissions());

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onViewWillCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        int layout = onGetLayoutId();
        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewCreated = true;
        isAvailable = true;
        onBindView(view);
        try {
            onViewDidCreated(view, savedInstanceState);
            AppBus.getInstance().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        isAvailable = false;
    }

    protected int onGetLayoutId() {
        return ViewBinder.getViewLayout(this);
    }

    protected void onBindView(View view) {
        ViewBinder.bind(view, this);
    }

    public String getName() {
        return null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mShowingStatus = RESUME_STATE;
        if (isUsedInViewPager) {
            if (isUserSeen && !isDisplayed) {
                isDisplayed = true;
                userFirstSight();
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //if fragment is used in viewpager
        isUsedInViewPager = true;
        //first time to show
        if (getActivity() == null && isVisibleToUser) {
            isUserSeen = true;
        }

        if (getActivity() != null) {
            if (isVisibleToUser) {
                isUserSeen = true;
                if (isViewCreated && !isDisplayed) {
                    isDisplayed = true;
                    userFirstSight();
                    return;
                }

                if (isDisplayed) {
                    userVisibleChanged(true);
                }
            } else {
                isUserSeen = false;
                userVisibleChanged(false);
                /*if (isDisplayed) {
                    userVisibleChanged(false);
                }*/
            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!isUsedInViewPager) { //!isUsedInViewPager && isViewCreated
            if (hidden) {
                userVisibleChanged(false);
            } else {
                if (mShowingStatus == HIDDEN_TO_HIDDEN) {
                    userFirstSight();
                } else {
                    userVisibleChanged(true);
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        /*if (!isUsedInViewPager) {
            userVisibleChanged(false);
        }*/
        userVisibleChanged(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mShowingStatus == HIDDEN) {
            mShowingStatus = SHOWING;
            onShow(true, FLAG_RESUME);
        } else {
            mHandler.post(mResumeRunnable);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isDisplayed = false;
        isViewCreated = false;
        isAvailable = false;
        AppBus.getInstance().unregister(this);
    }

    @Override
    public void handle(IAppEvent event) {

    }

    public void launch(Intent itn, @Nullable ActivityNavi.OnActivityResult<ActivityResult> onResult) {
        if (onResult == null) {
            startActivity(itn);
        } else {
            mActivityLauncher.launch(itn, onResult);
        }
    }

    public void permission(ActivityNavi.OnActivityResult<Boolean> onResult, String perm) {
        mPermissionLauncher.launch(perm, onResult);
    }

    public void permissions(ActivityNavi.OnActivityResult<Map<String, Boolean>> onResult, String... perms) {
        mMultiPermissionLauncher.launch(perms, onResult);
    }

    private void userFirstSight() {
        if (mShowingStatus != SHOWING && mShowingStatus != FIRST_SIGHT) {
            mShowingStatus = FIRST_SIGHT;
            onUserFirstSight();
            onShow(true, FLAG_FIRST_SIGHT);
        }
    }

    private void userVisibleChanged(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (mShowingStatus != SHOWING) {
                mShowingStatus = SHOWING;
                onUserVisibleChanged(true);
                onShow(true, FLAG_SHOW_AGAIN);
            }
        } else {
            if (mShowingStatus == SHOWING || mShowingStatus == FIRST_SIGHT) {
                mShowingStatus = HIDDEN;
                //onUserVisibleChanged(false);
                //onShow(false, false, false);
            } else if (mShowingStatus == RESUME_STATE ){
                mShowingStatus = HIDDEN_TO_HIDDEN;
            } else if (mShowingStatus == HIDDEN) {
                mShowingStatus = HAS_SHOW_TO_HIDDEN;
            }
            onUserVisibleChanged(false);
            onShow(false, FLAG_HIDE);
        }
    }

    protected void onUserVisibleChanged(boolean isVisibleToUser) {

    }
    protected void onUserFirstSight() {

    }

    protected void onShow(boolean isShowing, int flag){}

    protected void onViewWillCreate() {}
    protected void onViewDidCreated(View view, @Nullable Bundle savedInstanceState){}
    public boolean isUserFirstSight() {
        return isUserSeen;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
