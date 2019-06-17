package com.example.myapplication.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import java.io.IOException;

public class ImageLoad {
    public static void loadImage(@NonNull Context context,
                                 @NonNull Uri uri,
                                 @NonNull ImageView imageView) {
        try {
            //这句话不懂
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
