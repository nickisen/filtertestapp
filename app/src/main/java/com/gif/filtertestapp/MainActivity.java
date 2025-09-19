package com.gif.filtertestapp;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.gif.filtertestapp.databinding.ActivityMainBinding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private Bitmap originalBitmap;
    private Bitmap filteredBitmap;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    try {
                        originalBitmap = loadBitmapFromUri(uri);
                        binding.imageViewPreview.setImageBitmap(originalBitmap);
                        filteredBitmap = null;
                        binding.btnSave.setEnabled(false);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, "File not found", e);
                        Toast.makeText(this, "Image not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        binding.btnSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        binding.btnRemoveFilter.setOnClickListener(v -> {
            if (originalBitmap != null) {
                binding.imageViewPreview.setImageBitmap(originalBitmap);
                filteredBitmap = null;
                binding.btnSave.setEnabled(false);
            }
        });

        // Filter listeners
        binding.btnGrayscale.setOnClickListener(v -> applyFilter(FilterType.GRAYSCALE));
        binding.btnSepia.setOnClickListener(v -> applyFilter(FilterType.SEPIA));
        binding.btnInvert.setOnClickListener(v -> applyFilter(FilterType.INVERT));
        binding.btnVintage.setOnClickListener(v -> applyFilter(FilterType.VINTAGE));
        binding.btnBrightness.setOnClickListener(v -> applyFilter(FilterType.BRIGHTNESS));
        binding.btnContrast.setOnClickListener(v -> applyFilter(FilterType.CONTRAST));
        binding.btnWinter.setOnClickListener(v -> applyFilter(FilterType.WINTER));
        binding.btnSolarize.setOnClickListener(v -> applyFilter(FilterType.SOLARIZE));
        binding.btnPosterize.setOnClickListener(v -> applyFilter(FilterType.POSTERIZE));
        binding.btnVignette.setOnClickListener(v -> applyFilter(FilterType.VIGNETTE));
        binding.btnHeatmap.setOnClickListener(v -> applyFilter(FilterType.HEATMAP));
        binding.btnSharpen.setOnClickListener(v -> applyFilter(FilterType.SHARPEN));

        binding.btnSave.setOnClickListener(v -> {
            if (filteredBitmap != null) {
                saveImageToGallery(filteredBitmap);
            } else {
                Toast.makeText(this, "Apply a filter before saving.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private enum FilterType {
        GRAYSCALE, SEPIA, INVERT, VINTAGE, BRIGHTNESS, CONTRAST, WINTER,
        SOLARIZE, POSTERIZE, VIGNETTE, HEATMAP, SHARPEN
    }

    private void applyFilter(FilterType filterType) {
        if (originalBitmap == null) {
            Toast.makeText(this, "Please select an image first.", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (filterType) {
            case GRAYSCALE:
                filteredBitmap = FilterUtils.applyGrayscale(originalBitmap);
                break;
            case SEPIA:
                filteredBitmap = FilterUtils.applySepia(originalBitmap);
                break;
            case INVERT:
                filteredBitmap = FilterUtils.applyInvert(originalBitmap);
                break;
            case VINTAGE:
                filteredBitmap = FilterUtils.applyVintage(originalBitmap);
                break;
            case BRIGHTNESS:
                filteredBitmap = FilterUtils.applyBrightness(originalBitmap);
                break;
            case CONTRAST:
                filteredBitmap = FilterUtils.applyContrast(originalBitmap);
                break;
            case WINTER:
                filteredBitmap = FilterUtils.applyWinter(originalBitmap);
                break;
            case SOLARIZE:
                filteredBitmap = FilterUtils.applySolarize(originalBitmap);
                break;
            case POSTERIZE:
                filteredBitmap = FilterUtils.applyPosterize(originalBitmap);
                break;
            case VIGNETTE:
                filteredBitmap = FilterUtils.applyVignette(originalBitmap);
                break;
            case HEATMAP:
                filteredBitmap = FilterUtils.applyHeatmap(originalBitmap);
                break;
            case SHARPEN:
                filteredBitmap = FilterUtils.applySharpen(originalBitmap);
                break;
        }

        binding.imageViewPreview.setImageBitmap(filteredBitmap);
        binding.btnSave.setEnabled(true);
    }

    private Bitmap loadBitmapFromUri(Uri uri) throws FileNotFoundException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        try {
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        options.inJustDecodeBounds = false;
        inputStream = getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
        try {
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void saveImageToGallery(Bitmap bitmap) {
        String fileName = "FilteredImage_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilterTestApp");
        }
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (uri != null) {
            try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                    Toast.makeText(this, "Image saved to Gallery!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error saving image to gallery", e);
                Toast.makeText(this, "Failed to save image.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Failed to create new image file.", Toast.LENGTH_SHORT).show();
        }
    }
}