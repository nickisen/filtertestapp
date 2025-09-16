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

    // View Binding to access UI elements safely
    private ActivityMainBinding binding;

    // Bitmaps to hold the original and filtered images
    private Bitmap originalBitmap;
    private Bitmap filteredBitmap;

    // Modern way to handle activity results (e.g., picking an image)
    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    try {
                        // Load and downsample the image to prevent OutOfMemoryError
                        originalBitmap = loadBitmapFromUri(uri);
                        binding.imageViewPreview.setImageBitmap(originalBitmap);
                        // Reset filtered bitmap and disable save button when new image is selected
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

        // Revert to original image
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

        binding.btnSave.setOnClickListener(v -> {
            if (filteredBitmap != null) {
                saveImageToGallery(filteredBitmap);
            } else {
                Toast.makeText(this, "Apply a filter before saving.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Enum to manage different filter types
    private enum FilterType {
        GRAYSCALE, SEPIA, INVERT, VINTAGE, BRIGHTNESS, CONTRAST, WINTER
    }

    // Helper method to apply filters and reduce code repetition
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
        }

        binding.imageViewPreview.setImageBitmap(filteredBitmap);
        binding.btnSave.setEnabled(true);
    }

    /**
     * Loads a bitmap from a Uri, downsampling it to prevent OutOfMemoryErrors.
     *
     * @param uri The Uri of the image to load.
     * @return The loaded and downsampled bitmap.
     * @throws FileNotFoundException If the Uri cannot be opened.
     */
    private Bitmap loadBitmapFromUri(Uri uri) throws FileNotFoundException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        // Options to check image dimensions without loading it into memory
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        try {
            if (inputStream != null) inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calculate inSampleSize to downscale the image
        options.inSampleSize = calculateInSampleSize(options, 800, 800);

        // Decode bitmap with inSampleSize set
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

    /**
     * Saves a bitmap to the device's public gallery.
     * Uses MediaStore, which is the recommended way to save shared media.
     *
     * @param bitmap The bitmap to save.
     */
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