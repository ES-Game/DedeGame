package com.quangph.jetpack;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.quangph.base.R;


/**
 * Created by Pham Hai Quang on 10/13/2019.
 */
public class JetBottomSheetDialog extends BottomSheetDialogFragment {

    public IOnDismissListener mListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onDismiss(this);
        }
    }

    public void show(AppCompatActivity activity, String tag) {
        show(activity.getSupportFragmentManager(), tag);
    }

    public void setOnDismissListener(IOnDismissListener listener) {
        mListener = listener;
    }

    public interface IOnDismissListener {
        void onDismiss(JetBottomSheetDialog frag);
    }
}
