package com.quangph.jetpack.utils;

import android.graphics.Color;

/**
 * Created by quangph on 3/8/2016.
 */
public class ColorUtil {
    public static int darken(int color, int percent) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (float)(percent / 100);
        if (hsv[2] > 1) {
            hsv[2] = 1;
        }
        return Color.HSVToColor(hsv);
    }

    public static int translateColor(int color, float distance) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[0] += distance;
        if (hsv[0] < 0) {
            hsv[0] = 0;
        }

        if (hsv[0] > 360) {
            hsv[0] = 360;
        }

        return Color.HSVToColor(hsv);
    }

    public static int translateColorByPercent(int color, int percent) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        float distance = hsv[0] * percent / 100;
        return translateColor(color, distance);
    }
}
