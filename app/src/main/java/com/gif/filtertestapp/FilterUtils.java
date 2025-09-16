package com.gif.filtertestapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

public class FilterUtils {

    /**
     * Applies a grayscale filter to a bitmap.
     * This is done efficiently using a ColorMatrix.
     *
     * @param sourceBitmap The source bitmap to apply the filter to.
     * @return A new bitmap with the grayscale filter applied.
     */
    public static Bitmap applyGrayscale(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Applies a sepia filter to a bitmap.
     * This is done efficiently using a custom ColorMatrix.
     *
     * @param sourceBitmap The source bitmap to apply the filter to.
     * @return A new bitmap with the sepia filter applied.
     */
    public static Bitmap applySepia(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0);
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1f, 1f, 0.8f, 1f);
        colorMatrix.postConcat(colorScale);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Applies an invert filter to a bitmap.
     * This is done by inverting the RGB channels.
     *
     * @param sourceBitmap The source bitmap to apply the filter to.
     * @return A new bitmap with the invert filter applied.
     */
    public static Bitmap applyInvert(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        // Matrix to invert colors: -1*C + 255
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        });
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Applies a vintage/cool filter to a bitmap.
     *
     * @param sourceBitmap The source bitmap to apply the filter to.
     * @return A new bitmap with the vintage filter applied.
     */
    public static Bitmap applyVintage(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.8f); // Slightly desaturate

        // Scale colors to give a cool, faded look (boost blue, slightly reduce green)
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1f, 0.95f, 1.1f, 1f);

        colorMatrix.postConcat(colorScale);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        paint.setColorFilter(colorFilter);
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Increases the brightness of a bitmap.
     * @param sourceBitmap The source bitmap.
     * @return A new, brighter bitmap.
     */
    public static Bitmap applyBrightness(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        // Matrix to increase brightness by adding a value (e.g., 50) to RGB channels
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, 50,  // Red
                0, 1, 0, 0, 50,  // Green
                0, 0, 1, 0, 50,  // Blue
                0, 0, 0, 1, 0    // Alpha
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Increases the contrast of a bitmap.
     * @param sourceBitmap The source bitmap.
     * @return A new bitmap with higher contrast.
     */
    public static Bitmap applyContrast(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        float contrast = 1.5f; // > 1 increases contrast, < 1 reduces it
        float translation = 128f * (1.0f - contrast);
        // Matrix to increase contrast
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                contrast, 0, 0, 0, translation,
                0, contrast, 0, 0, translation,
                0, 0, contrast, 0, translation,
                0, 0, 0, 1, 0
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Applies a cool, "winter" themed filter to a bitmap.
     * @param sourceBitmap The source bitmap.
     * @return A new bitmap with the winter filter.
     */
    public static Bitmap applyWinter(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.6f); // Desaturate to give a faded, cold look

        // Scale colors to give a cool, blueish tint
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(0.9f, 1f, 1.2f, 1f); // Reduce red, keep green, boost blue

        colorMatrix.postConcat(colorScale);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }
}