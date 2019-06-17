package com.example.myapplication.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ImageLoad {




    public static void loadImage(@NonNull Context context,
                                 @NonNull String path,
                                 @NonNull ImageView imageView) {

//            //这句话不懂
            File imgFile = new  File(path);
            if(imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(myBitmap);
            }else {
                Toast.makeText(context, "File Did Not Exists", Toast.LENGTH_LONG);
            }

    }
}
