package com.quangph.base.common;

import android.content.Intent;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivityNavi<Input, Result> {

    public interface OnActivityResult<O> {
        void onActivityResult(O result);
    }

    public static<Input, Result> ActivityNavi<Input, Result> registerActivityForResult(
            @NonNull ActivityResultCaller caller,
            @NonNull ActivityResultContract<Input, Result> contract) {
        return new ActivityNavi<>(caller, contract);
    }

    public static ActivityNavi<Intent, ActivityResult> registerActivityForResult(
            @NonNull ActivityResultCaller caller) {
        return registerActivityForResult(caller, new ActivityResultContracts.StartActivityForResult());
    }

    private final ActivityResultLauncher<Input> mLauncher;
    private OnActivityResult<Result> mOnActivityResult;

    private ActivityNavi(@NonNull ActivityResultCaller caller,
                         @NonNull ActivityResultContract<Input, Result> contract) {
        this.mLauncher = caller.registerForActivityResult(contract, this::callOnActivityResult);
    }

    public void launch(Input input, @Nullable OnActivityResult<Result> onActivityResult) {
        if (onActivityResult != null) {
            this.mOnActivityResult = onActivityResult;
        }
        mLauncher.launch(input);
    }

    private void callOnActivityResult(Result result) {
        if (mOnActivityResult != null) {
            mOnActivityResult.onActivityResult(result);
            mOnActivityResult = null;
        }
    }

}
