package com.quangph.jetpack.autobg;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

/**
 * Auto add a grey background to the front for press state
 * Created by Pham Hai QUANG on 6/27/2017.
 */

public class AutoBackgroundDrawable extends LayerDrawable {
    private ColorFilter mPressedFilter = new LightingColorFilter(Color.LTGRAY, 1);

    public AutoBackgroundDrawable(Drawable d) {
        super(new Drawable[]{d});
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean enabled = false;
        boolean pressed = false;

        for (int state : states) {
            if (state == android.R.attr.state_enabled)
                enabled = true;
            else if (state == android.R.attr.state_pressed)
                pressed = true;
        }

        //mutate();
        if (enabled && pressed) {
            setColorFilter(mPressedFilter);
        } else {
            setColorFilter(null);
            setAlpha(enabled ? 255 : 100);//disabledAlpha = 100, fullAlpha = 255
        }

        invalidateSelf();

        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }
}
