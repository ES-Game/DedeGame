package com.quangph.jetpack.autobg;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


/**
 * Auto grey for press state
 * Created by Pham Hai QUANG on 6/26/2017.
 */

public class AutoBgImageView extends AppCompatImageView {

    public AutoBgImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        AutoBackgroundDrawable layer = new AutoBackgroundDrawable(drawable);
        super.setImageDrawable(layer);
    }
}
