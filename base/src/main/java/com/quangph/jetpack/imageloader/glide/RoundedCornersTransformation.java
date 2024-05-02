package com.quangph.jetpack.imageloader.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

import java.security.MessageDigest;

/**
 * Created by QuangPH on 2020-12-04.
 */
public class RoundedCornersTransformation extends BitmapTransformation {
    private static final int VERSION = 1;
    private static final String ID = "jp.wasabeef.glide.transformations.RoundedCornersTransformation.1";
    private int radius;
    private int diameter;
    private int margin;
    private CornerType cornerType;

    public RoundedCornersTransformation(int radius, int margin) {
        this(radius, margin, CornerType.ALL);
    }

    public RoundedCornersTransformation(int radius, int margin, CornerType cornerType) {
        this.radius = radius;
        this.diameter = this.radius * 2;
        this.margin = margin;
        this.cornerType = cornerType;
    }

    protected Bitmap transform(@NonNull Context context, @NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        Bitmap bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setHasAlpha(true);
        this.setCanvasBitmapDensity(toTransform, bitmap);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        this.drawRoundRect(canvas, paint, (float)width, (float)height);
        return bitmap;
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - (float)this.margin;
        float bottom = height - (float)this.margin;
        switch(this.cornerType) {
            case ALL:
                canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, right, bottom), (float)this.radius, (float)this.radius, paint);
                break;
            case TOP_LEFT:
                this.drawTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case TOP_RIGHT:
                this.drawTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_LEFT:
                this.drawBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM_RIGHT:
                this.drawBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case TOP:
                this.drawTopRoundRect(canvas, paint, right, bottom);
                break;
            case BOTTOM:
                this.drawBottomRoundRect(canvas, paint, right, bottom);
                break;
            case LEFT:
                this.drawLeftRoundRect(canvas, paint, right, bottom);
                break;
            case RIGHT:
                this.drawRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_LEFT:
                this.drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_TOP_RIGHT:
                this.drawOtherTopRightRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_LEFT:
                this.drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
                break;
            case OTHER_BOTTOM_RIGHT:
                this.drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_LEFT:
                this.drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
                break;
            case DIAGONAL_FROM_TOP_RIGHT:
                this.drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
                break;
            default:
                canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, right, bottom), (float)this.radius, (float)this.radius, paint);
        }

    }

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, (float)(this.margin + this.diameter), (float)(this.margin + this.diameter)), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)(this.margin + this.radius), (float)(this.margin + this.radius), bottom), paint);
        canvas.drawRect(new RectF((float)(this.margin + this.radius), (float)this.margin, right, bottom), paint);
    }

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - (float)this.diameter, (float)this.margin, right, (float)(this.margin + this.diameter)), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)this.margin, right - (float)this.radius, bottom), paint);
        canvas.drawRect(new RectF(right - (float)this.radius, (float)(this.margin + this.radius), right, bottom), paint);
    }

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, bottom - (float)this.diameter, (float)(this.margin + this.diameter), bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)this.margin, (float)(this.margin + this.diameter), bottom - (float)this.radius), paint);
        canvas.drawRect(new RectF((float)(this.margin + this.radius), (float)this.margin, right, bottom), paint);
    }

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - (float)this.diameter, bottom - (float)this.diameter, right, bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)this.margin, right - (float)this.radius, bottom), paint);
        canvas.drawRect(new RectF(right - (float)this.radius, (float)this.margin, right, bottom - (float)this.radius), paint);
    }

    private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, right, (float)(this.margin + this.diameter)), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)(this.margin + this.radius), right, bottom), paint);
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, bottom - (float)this.diameter, right, bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)this.margin, right, bottom - (float)this.radius), paint);
    }

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, (float)(this.margin + this.diameter), bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)(this.margin + this.radius), (float)this.margin, right, bottom), paint);
    }

    private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - (float)this.diameter, (float)this.margin, right, bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)this.margin, right - (float)this.radius, bottom), paint);
    }

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, bottom - (float)this.diameter, right, bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRoundRect(new RectF(right - (float)this.diameter, (float)this.margin, right, bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)this.margin, right - (float)this.radius, bottom - (float)this.radius), paint);
    }

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, (float)(this.margin + this.diameter), bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRoundRect(new RectF((float)this.margin, bottom - (float)this.diameter, right, bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)(this.margin + this.radius), (float)this.margin, right, bottom - (float)this.radius), paint);
    }

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, right, (float)(this.margin + this.diameter)), (float)this.radius, (float)this.radius, paint);
        canvas.drawRoundRect(new RectF(right - (float)this.diameter, (float)this.margin, right, bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)(this.margin + this.radius), right - (float)this.radius, bottom), paint);
    }

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, right, (float)(this.margin + this.diameter)), (float)this.radius, (float)this.radius, paint);
        canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, (float)(this.margin + this.diameter), bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)(this.margin + this.radius), (float)(this.margin + this.radius), right, bottom), paint);
    }

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF((float)this.margin, (float)this.margin, (float)(this.margin + this.diameter), (float)(this.margin + this.diameter)), (float)this.radius, (float)this.radius, paint);
        canvas.drawRoundRect(new RectF(right - (float)this.diameter, bottom - (float)this.diameter, right, bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)(this.margin + this.radius), right - (float)this.diameter, bottom), paint);
        canvas.drawRect(new RectF((float)(this.margin + this.diameter), (float)this.margin, right, bottom - (float)this.radius), paint);
    }

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - (float)this.diameter, (float)this.margin, right, (float)(this.margin + this.diameter)), (float)this.radius, (float)this.radius, paint);
        canvas.drawRoundRect(new RectF((float)this.margin, bottom - (float)this.diameter, (float)(this.margin + this.diameter), bottom), (float)this.radius, (float)this.radius, paint);
        canvas.drawRect(new RectF((float)this.margin, (float)this.margin, right - (float)this.radius, bottom - (float)this.radius), paint);
        canvas.drawRect(new RectF((float)(this.margin + this.radius), (float)(this.margin + this.radius), right, bottom), paint);
    }

    public String toString() {
        return "RoundedTransformation(radius=" + this.radius + ", margin=" + this.margin + ", diameter=" + this.diameter + ", cornerType=" + this.cornerType.name() + ")";
    }

    public boolean equals(Object o) {
        return o instanceof RoundedCornersTransformation && ((RoundedCornersTransformation)o).radius == this.radius && ((RoundedCornersTransformation)o).diameter == this.diameter && ((RoundedCornersTransformation)o).margin == this.margin && ((RoundedCornersTransformation)o).cornerType == this.cornerType;
    }

    public int hashCode() {
        return "jp.wasabeef.glide.transformations.RoundedCornersTransformation.1".hashCode() + this.radius * 10000 + this.diameter * 1000 + this.margin * 100 + this.cornerType.ordinal() * 10;
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(("jp.wasabeef.glide.transformations.RoundedCornersTransformation.1" + this.radius + this.diameter + this.margin + this.cornerType).getBytes(CHARSET));
    }

    public static enum CornerType {
        ALL,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        OTHER_TOP_LEFT,
        OTHER_TOP_RIGHT,
        OTHER_BOTTOM_LEFT,
        OTHER_BOTTOM_RIGHT,
        DIAGONAL_FROM_TOP_LEFT,
        DIAGONAL_FROM_TOP_RIGHT;

        private CornerType() {
        }
    }
}
