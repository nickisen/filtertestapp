package com.gif.filtertestapp;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public class FilterUtils {

    /**
     * Applies a grayscale filter to a bitmap.
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
     */
    public static Bitmap applyInvert(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Applies a vintage/cool filter to a bitmap.
     */
    public static Bitmap applyVintage(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.8f);
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(1f, 0.95f, 1.1f, 1f);
        colorMatrix.postConcat(colorScale);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Increases the brightness of a bitmap.
     */
    public static Bitmap applyBrightness(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, 50,
                0, 1, 0, 0, 50,
                0, 0, 1, 0, 50,
                0, 0, 0, 1, 0
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Increases the contrast of a bitmap.
     */
    public static Bitmap applyContrast(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        float contrast = 1.5f;
        float translation = 128f * (1.0f - contrast);
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
     */
    public static Bitmap applyWinter(Bitmap sourceBitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight(), sourceBitmap.getConfig());
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.6f);
        ColorMatrix colorScale = new ColorMatrix();
        colorScale.setScale(0.9f, 1f, 1.2f, 1f);
        colorMatrix.postConcat(colorScale);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(sourceBitmap, 0, 0, paint);
        return resultBitmap;
    }

    /**
     * Applies a solarize effect to a bitmap.
     */
    public static Bitmap applySolarize(Bitmap sourceBitmap) {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        int width = resultBitmap.getWidth();
        int height = resultBitmap.getHeight();
        int[] pixels = new int[width * height];
        resultBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int threshold = 128;
        for (int i = 0; i < pixels.length; i++) {
            int p = pixels[i];
            int r = Color.red(p);
            int g = Color.green(p);
            int b = Color.blue(p);
            if (r > threshold) r = 255 - r;
            if (g > threshold) g = 255 - g;
            if (b > threshold) b = 255 - b;
            pixels[i] = Color.argb(Color.alpha(p), r, g, b);
        }
        resultBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return resultBitmap;
    }

    /**
     * Applies a posterize effect, reducing the number of color levels.
     */
    public static Bitmap applyPosterize(Bitmap sourceBitmap) {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        int width = resultBitmap.getWidth();
        int height = resultBitmap.getHeight();
        int[] pixels = new int[width * height];
        resultBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int numLevels = 4;
        int levelSize = 256 / numLevels;
        for (int i = 0; i < pixels.length; i++) {
            int p = pixels[i];
            int r = Color.red(p);
            int g = Color.green(p);
            int b = Color.blue(p);
            r = (r / levelSize) * levelSize + levelSize / 2;
            g = (g / levelSize) * levelSize + levelSize / 2;
            b = (b / levelSize) * levelSize + levelSize / 2;
            pixels[i] = Color.argb(Color.alpha(p), r, g, b);
        }
        resultBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return resultBitmap;
    }

    /**
     * Adds a vignette effect, darkening the corners of the image.
     */
    public static Bitmap applyVignette(Bitmap sourceBitmap) {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        Canvas canvas = new Canvas(resultBitmap);
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        float radius = (float) (width * 0.7);
        RadialGradient gradient = new RadialGradient(
                width / 2f, height / 2f, radius,
                new int[]{0, 0, 0xAA000000},
                new float[]{0.0f, 0.5f, 1.0f},
                Shader.TileMode.CLAMP
        );
        Paint paint = new Paint();
        paint.setShader(gradient);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        canvas.drawRect(0, 0, width, height, paint);
        return resultBitmap;
    }

    /**
     * Applies a duotone heatmap effect.
     */
    public static Bitmap applyHeatmap(Bitmap sourceBitmap) {
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        int width = resultBitmap.getWidth();
        int height = resultBitmap.getHeight();
        int[] pixels = new int[width * height];
        resultBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] gradientColors = {Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW, Color.RED};
        for (int i = 0; i < pixels.length; i++) {
            int p = pixels[i];
            int luminance = (int) (0.299 * Color.red(p) + 0.587 * Color.green(p) + 0.114 * Color.blue(p));
            float position = luminance / 255f;
            int colorIndex = (int) (position * (gradientColors.length - 1));
            pixels[i] = gradientColors[colorIndex];
        }
        resultBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return resultBitmap;
    }

    /**
     * Applies a sharpening effect using a convolution matrix.
     */
    public static Bitmap applySharpen(Bitmap sourceBitmap) {
        float[] kernel = {
                -1, -1, -1,
                -1,  9, -1,
                -1, -1, -1
        };
        Bitmap resultBitmap = sourceBitmap.copy(sourceBitmap.getConfig(), true);
        int width = sourceBitmap.getWidth();
        int height = sourceBitmap.getHeight();
        int[] srcPixels = new int[width * height];
        int[] dstPixels = new int[width * height];
        sourceBitmap.getPixels(srcPixels, 0, width, 0, 0, width, height);
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                float sumR = 0, sumG = 0, sumB = 0;
                int kernelIndex = 0;
                for (int ky = -1; ky <= 1; ky++) {
                    for (int kx = -1; kx <= 1; kx++) {
                        int pixel = srcPixels[(y + ky) * width + (x + kx)];
                        float weight = kernel[kernelIndex++];
                        sumR += Color.red(pixel) * weight;
                        sumG += Color.green(pixel) * weight;
                        sumB += Color.blue(pixel) * weight;
                    }
                }
                int r = Math.min(255, Math.max(0, (int) sumR));
                int g = Math.min(255, Math.max(0, (int) sumG));
                int b = Math.min(255, Math.max(0, (int) sumB));
                dstPixels[y * width + x] = Color.argb(255, r, g, b);
            }
        }
        resultBitmap.setPixels(dstPixels, 0, width, 0, 0, width, height);
        return resultBitmap;
    }
}