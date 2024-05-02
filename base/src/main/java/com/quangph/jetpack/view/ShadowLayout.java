package com.quangph.jetpack.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.quangph.base.R;


/**
 * ShadowLayout.java
 * Created by quangph on 23/7/2021.
 */

public class ShadowLayout extends RelativeLayout {

    public static final int ALL = 0x1111;
    public static final int LEFT = 0x0001;
    public static final int TOP = 0x0010;
    public static final int RIGHT = 0x0100;
    public static final int BOTTOM = 0x1000;
    public static final int SHAPE_RECTANGLE = 0x0001;
    public static final int SHAPE_OVAL = 0x0010;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mRectF = new RectF();
    private final RectF mContentRect = new RectF();

    //Path around content of shadowview
    private final Path mContentPath = new Path();
    private final float[] mRadiusArray = new float[8];

    private int mShadowColor = Color.TRANSPARENT;
    private float mShadowRadius = 0;
    private int mShadowSide = ALL;
    private int mShadowShape = SHAPE_RECTANGLE;

    /**
     * True: Content view will be remained its dimen, Shadow view dimen = content view + padding
     * False: Content view will be smaller because content view dimen = shadow view - padding
     */
    private boolean isPaddingExclude = true;
    private boolean isDimenDetected = false;

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setupBackgroundPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float effect = mShadowRadius;
        float rectLeft = 0;
        float rectTop = 0;
        float rectRight = 0;
        float rectBottom = 0;
        int paddingLeft = 0;
        int paddingTop = 0;
        int paddingRight = 0;
        int paddingBottom = 0;
        if ((mShadowSide & LEFT) == LEFT) {
            rectLeft = effect;
            paddingLeft = (int) effect;
        }

        if ((mShadowSide & TOP) == TOP) {
            //Less shadow effect than left/right
            rectTop = effect * 1.5f;
            paddingTop = (int) effect;
        } else {
            rectTop -= 2 * effect;
        }

        if ((mShadowSide & RIGHT) == RIGHT) {
            rectRight = this.getMeasuredWidth() - effect;
            paddingRight = (int) effect;
        }

        if ((mShadowSide & BOTTOM) == BOTTOM) {
            //More shadow effect at bottom than left/right
            rectBottom  = getMeasuredHeight() - effect;// - 0.5f * effect;
            paddingBottom = (int) effect;
        } else {
            rectBottom = getMeasuredHeight() + 2 * effect;
        }

        if (isPaddingExclude && !isDimenDetected) {
            isDimenDetected = true;
            int newW = getMeasuredWidth();
            int wSpec = getLayoutParams().width;
            if (wSpec != LayoutParams.MATCH_PARENT) {
                newW += paddingLeft + paddingRight;
            }
            int newH = getMeasuredHeight();
            int hSpec = getLayoutParams().height;
            if (hSpec != LayoutParams.MATCH_PARENT) {
                newH += paddingTop + paddingBottom;
            }
            setMeasuredDimension(newW, newH);

            //Recalculate right/bottom shadow rect
            if ((mShadowSide & RIGHT) == RIGHT) {
                rectRight = this.getMeasuredWidth() - effect;
            }
            if ((mShadowSide & BOTTOM) == BOTTOM) {
                //More shadow effect at bottom than left/right
                rectBottom = this.getMeasuredHeight() - effect;// - 0.5f * effect;
            } else {
                rectBottom = this.getMeasuredHeight() + 2 * effect;
            }
        }

        mRectF.left = rectLeft;
        mRectF.top = rectTop;
        mRectF.right = rectRight;
        mRectF.bottom = rectBottom;
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        mContentRect.left = paddingLeft;
        mContentRect.top = paddingTop;
        mContentRect.right = getMeasuredWidth() - paddingRight;
        mContentRect.bottom = getMeasuredHeight() - paddingBottom;
        mContentPath.addRoundRect(mContentRect, mRadiusArray, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setupShadowPaint();
        if (mShadowShape == SHAPE_RECTANGLE) {
            canvas.drawRect(mRectF, mPaint);
        } else if (mShadowShape == SHAPE_OVAL) {
            canvas.drawCircle(mRectF.centerX(), mRectF.centerY(),
                    Math.min(mRectF.width(), mRectF.height()) / 2,
                    mPaint);
        }

        canvas.drawPath(mContentPath, mBgPaint);
    }

    public void setShadowColor(int shadowColor) {
        mShadowColor = shadowColor;
        requestLayout();
        postInvalidate();
    }

    public void setShadowRadius(float shadowRadius) {
        mShadowRadius = shadowRadius;
        requestLayout();
        postInvalidate();
    }

    private void init(AttributeSet attrs) {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.setWillNotDraw(false);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        if (typedArray != null) {
            mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadow_layout_color,
                    Color.parseColor("#33000000"));
            mShadowRadius = typedArray.getDimensionPixelSize(R.styleable.ShadowLayout_shadow_layout_radius, 0);
            mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_shadow_layout_side, ALL);
            mShadowShape = typedArray.getInt(R.styleable.ShadowLayout_shadow_layout_shape, SHAPE_RECTANGLE);
            isPaddingExclude = typedArray.getBoolean(R.styleable.ShadowLayout_shadow_layout_padding_exclude, true);
            typedArray.recycle();
        }
        setupShadowPaint();
        initContentPath();
    }

    private void setupShadowPaint() {
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setShadowLayer(mShadowRadius, 0, 0, mShadowColor);
    }

    private void setupBackgroundPaint() {
        mBgPaint.reset();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(Color.WHITE);
    }

    private void initContentPath() {
        if ((mShadowSide & TOP) == TOP) {
            mRadiusArray[0] = mShadowRadius;
            mRadiusArray[1] = mShadowRadius;
            mRadiusArray[2] = mShadowRadius;
            mRadiusArray[3] = mShadowRadius;
        }

        if ((mShadowSide & BOTTOM) == BOTTOM) {
            mRadiusArray[4] = mShadowRadius;
            mRadiusArray[5] = mShadowRadius;
            mRadiusArray[6] = mShadowRadius;
            mRadiusArray[7] = mShadowRadius;
        }
    }
}
