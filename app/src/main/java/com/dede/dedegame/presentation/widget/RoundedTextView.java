package com.dede.dedegame.presentation.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.dede.dedegame.R;

public class RoundedTextView extends AppCompatTextView {
    @ColorInt
    private static final int RIPPLE_COLOR = Color.parseColor("#1F000000");
    public static final int NO_INDEX = -1;
    private GradientDrawable gradientDrawable;

    public RoundedTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedTextView);

        int cornerRadius = typedArray.getDimensionPixelSize(R.styleable.RoundedTextView_cornerRadius, 0);
        int cornerRadiusTopLeft = typedArray.getDimensionPixelSize(R.styleable.RoundedTextView_cornerRadiusTopLeft, cornerRadius);
        int cornerRadiusTopRight = typedArray.getDimensionPixelSize(R.styleable.RoundedTextView_cornerRadiusTopRight, cornerRadius);
        int cornerRadiusBottomRight = typedArray.getDimensionPixelSize(R.styleable.RoundedTextView_cornerRadiusBottomRight, cornerRadius);
        int cornerRadiusBottomLeft = typedArray.getDimensionPixelSize(R.styleable.RoundedTextView_cornerRadiusBottomLeft, cornerRadius);
        int solidColor = typedArray.getColor(R.styleable.RoundedTextView_solidColor, NO_INDEX);
        int borderColor = typedArray.getColor(R.styleable.RoundedTextView_borderColor, NO_INDEX);
        int borderWidth = typedArray.getDimensionPixelSize(R.styleable.RoundedTextView_borderWidth, NO_INDEX);
        int dashWidth = typedArray.getDimensionPixelSize(R.styleable.RoundedTextView_dashWidth, 0);
        int dashGap = typedArray.getDimensionPixelSize(R.styleable.RoundedTextView_dashGap, 0);
        boolean isEnableRipple = typedArray.getBoolean(R.styleable.RoundedTextView_enabledRipple, false);
        int rippleColor = typedArray.getColor(R.styleable.RoundedTextView_rippleColor, RIPPLE_COLOR);

        typedArray.recycle();

        setCornerRadius(cornerRadiusTopLeft, cornerRadiusTopRight, cornerRadiusBottomRight, cornerRadiusBottomLeft);
        setBorderBase(borderWidth, borderColor, dashWidth, dashGap);
        setSolidColorInt(solidColor);

        if (isEnableRipple && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setRipple(rippleColor, cornerRadiusTopLeft, cornerRadiusTopRight,
                    cornerRadiusBottomRight, cornerRadiusBottomLeft);
        }

        if (gradientDrawable != null) {
            setBackground(gradientDrawable);
        }
    }

    /**
     * Sets the text color for all the states (normal, selected, focused)
     * from a resource ID
     *
     * @param colorRes A color value from a resource ID
     */
    public void setTextColorRes(@ColorRes int colorRes) {
        setTextColor(getColor(colorRes));
    }

    /**
     * Sets the text color for all the states (normal, selected, focused)
     * from a attribute
     *
     * @param attrRes A attribute value
     */
    public void setTextColorAttr(@AttrRes int attrRes) {
        setTextColor(getColorByAttr(attrRes));
    }

    /**
     * Set the ripple effect for the view
     *
     * @param radius Radius of the ripple effect border
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setRipple(@Px int radius) {
        setRipple(radius, radius, radius, radius);
    }

    /**
     * Set the ripple effect for the view
     *
     * @param rippleColor Color of the ripple effect
     * @param radius      Radius of the ripple effect border
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setRipple(@ColorInt int rippleColor, @Px int radius) {
        setRipple(rippleColor, radius, radius, radius, radius);
    }

    /**
     * Set the ripple effect for the view
     *
     * @param radiusTopLeft     Top left radius of the ripple effect border
     * @param radiusTopRight    Top right radius of the ripple effect border
     * @param radiusBottomRight Bottom right radius of the ripple effect border
     * @param radiusBottomLeft  Bottom left radius of the ripple effect border
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setRipple(@Px int radiusTopLeft,
                          @Px int radiusTopRight,
                          @Px int radiusBottomRight,
                          @Px int radiusBottomLeft) {
        setRipple(RIPPLE_COLOR, radiusTopLeft, radiusTopRight, radiusBottomRight, radiusBottomLeft);
    }

    /**
     * Set the ripple effect for the view
     *
     * @param rippleColor       Color of the ripple effect
     * @param radiusTopLeft     Top left radius of the ripple effect border
     * @param radiusTopRight    Top right radius of the ripple effect border
     * @param radiusBottomRight Bottom right radius of the ripple effect border
     * @param radiusBottomLeft  Bottom left radius of the ripple effect border
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setRipple(@ColorInt int rippleColor,
                          @Px int radiusTopLeft,
                          @Px int radiusTopRight,
                          @Px int radiusBottomRight,
                          @Px int radiusBottomLeft) {
        ColorStateList rippleColorStateList = ColorStateList.valueOf(rippleColor);
        int maskColor = getColor(android.R.color.white);
        float[] outerRadii = new float[]{
                radiusTopLeft, radiusTopLeft,
                radiusTopRight, radiusTopRight,
                radiusBottomRight, radiusBottomRight,
                radiusBottomLeft, radiusBottomLeft
        };

        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(maskColor);
        setForeground(new RippleDrawable(rippleColorStateList, null, shapeDrawable));
    }

    /**
     * Set a single color for the view from a attribute.
     *
     * @param colorAttrRes A attribute value
     */
    public void setSolidColorAttr(@AttrRes int colorAttrRes) {
        setSolidColorInt(getColorByAttr(colorAttrRes));
    }

    /**
     * Set a single color for the view from a resource ID.
     *
     * @param color A color value from a resource ID
     */
    public void setSolidColor(@ColorRes int color) {
        setSolidColorInt(getColor(color));
    }


    public void setSolidColorInt(@ColorInt int color) {
        if (isNotEmpty(color)) {
            getGradientDrawable().setColor(color);
        }
    }

    /**
     * Specifies radii for all corners.
     *
     * @param radius A radii for all corners, specified in pixels
     */
    public void setCornerRadius(@Px int radius) {
        setCornerRadius(radius, radius, radius, radius);
    }

    /**
     * Specifies radii for each of the 4 corners.
     *
     * @param radiusTopLeft     Top left radius, specified in pixels
     * @param radiusTopRight    Top right radius, specified in pixels
     * @param radiusBottomRight Bottom right radius, specified in pixels
     * @param radiusBottomLeft  Bottom left radius, specified in pixels
     */
    public void setCornerRadius(@Px int radiusTopLeft, @Px int radiusTopRight,
                                @Px int radiusBottomRight, @Px int radiusBottomLeft) {
        getGradientDrawable().setCornerRadii(new float[]{
                radiusTopLeft, radiusTopLeft,
                radiusTopRight, radiusTopRight,
                radiusBottomRight, radiusBottomRight,
                radiusBottomLeft, radiusBottomLeft
        });
    }

    /**
     * Set the stroke width and color for the drawable. If width
     * is zero, then no stroke is drawn. This method can also be used to dash the stroke.
     *
     * @param width     The width in pixels of the stroke
     * @param color     The color of the stroke from a resource ID
     * @param dashWidth The length in pixels of the dashes, set to 0 to disable dashes
     * @param dashGap   The gap in pixels between dashes
     */
    public void setBorder(@Px int width, @ColorRes int color, @Px int dashWidth, @Px int dashGap) {
        setBorderBase(width, getColor(color), dashWidth, dashGap);
    }

    /**
     * Set the stroke width and color for the drawable. If width
     * is zero, then no stroke is drawn.
     *
     * @param width The width in pixels of the stroke
     * @param color The color of the stroke from a resource ID
     */
    public void setBorder(@Px int width, @ColorRes int color) {
        setBorderBase(width, getColor(color), 0, 0);
    }

    /**
     * Set the stroke width and color for the drawable. If width
     * is zero, then no stroke is drawn. This method can also be used to dash the stroke.
     *
     * @param width        The width in pixels of the stroke
     * @param colorAttrRes The color of the stroke from a attribute
     * @param dashWidth    The length in pixels of the dashes, set to 0 to disable dashes
     * @param dashGap      The gap in pixels between dashes
     */
    public void setBorderAttr(@Px int width, @AttrRes int colorAttrRes, @Px int dashWidth, @Px int dashGap) {
        setBorderBase(width, getColorByAttr(colorAttrRes), dashWidth, dashGap);
    }

    /**
     * Set the stroke width and color for the drawable. If width
     * is zero, then no stroke is drawn.
     *
     * @param width        The width in pixels of the stroke
     * @param colorAttrRes The color of the stroke from a attribute
     */
    public void setBorderAttr(@Px int width, @AttrRes int colorAttrRes) {
        setBorderBase(width, getColorByAttr(colorAttrRes), 0, 0);
    }

    /**
     * Set the stroke width and color for the drawable. If width
     * is zero, then no stroke is drawn. This method can also be used to dash the stroke.
     *
     * @param width     The width in pixels of the stroke
     * @param color     The color of the stroke
     * @param dashWidth The length in pixels of the dashes, set to 0 to disable dashes
     * @param dashGap   The gap in pixels between dashes
     */
    public void setBorderBase(@Px int width, @ColorInt int color, @Px int dashWidth, @Px int dashGap) {
        if (isNotEmpty(width) && isNotEmpty(color)) {
            getGradientDrawable().setStroke(width, color, dashWidth, dashGap);
        }
    }

    /**
     * Set a color tint for Drawable
     *
     * @param color A color tint for Drawable from a resource ID
     */
    public void setDrawableTint(@ColorRes int color) {
        setDrawableTintInt(getColor(color));
    }

    /**
     * Set a color tint for Drawable
     *
     * @param color A color tint for Drawable
     */
    public void setDrawableTintInt(@ColorInt int color) {
        if (isNotEmpty(color)) {
            Drawable[] drawables = getCompoundDrawablesRelative();
            for (Drawable drawable : drawables) {
                if (drawable != null) {
                    setDrawableTint(drawable, color);
                }
            }
        }
    }

    @ColorInt
    private int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    @ColorInt
    private int getColorByAttr(@AttrRes int colorAttrRes) {
        TypedArray typedArray = getContext().obtainStyledAttributes(new TypedValue().data, new int[]{colorAttrRes});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        return color;
    }

    private void setDrawableTint(Drawable drawable, @ColorInt int color) {
        DrawableCompat.setTint(DrawableCompat.wrap(drawable).mutate(), color);
    }

    private GradientDrawable getGradientDrawable() {
        if (gradientDrawable == null) {
            gradientDrawable = new GradientDrawable();
        }
        return gradientDrawable;
    }

    private boolean isNotEmpty(float index) {
        return index != NO_INDEX;
    }
}
