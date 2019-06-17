package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.model.BasicInfo;
import com.example.myapplication.util.ImageLoad;
import com.example.myapplication.util.PermissionUtils;

public class BasicInfoChange extends AppCompatActivity {
    public static final String KEY_BASIC_INFO = "Personal_Info_Change";
    private static final int REQ_CODE_PICK_IMAGE = 100;

    private BasicInfo basicInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinfo_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//返回符号
        basicInfo = getIntent().getParcelableExtra(KEY_BASIC_INFO);
        setupUI();
        setTitle("Edit Information");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                basicInfo.photo = imageUri;
                showImage(imageUri);
            }
        }
    }


    /*完全不懂
    ******************************************************************************************************************************
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        }
    }
    /******************************************************************************************************************************/
    private void setupUI() {
        ((EditText)findViewById(R.id.Name)).setText(basicInfo.name);
        ((EditText)findViewById(R.id.email)).setText(basicInfo.email);
        if (basicInfo.photo != null) {
            showImage(basicInfo.photo);
        }
        findViewById(R.id.basic_info_edit_image_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*******************************************************************************************/

                if (!PermissionUtils.checkPermission(BasicInfoChange.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    PermissionUtils.requestReadExternalStoragePermission(BasicInfoChange.this);
                } else {
                   /******************************************************************************************************************************/

                    pickImage();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }else if(item.getItemId() == R.id.save){
            saveAndExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndExit() {
        basicInfo.name = ((EditText)findViewById(R.id.Name)).getText().toString();
        basicInfo.email = ((EditText)findViewById(R.id.email)).getText().toString();

        //传数据
        Intent resultIntent = new Intent();//intend as hashmap -> key,value
        resultIntent.putExtra(KEY_BASIC_INFO, basicInfo);//putExtra 放的是基本的信息，不能放自己建立的Object, serialize and deserialize (加密解密)
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }

    private void showImage(@NonNull Uri imageUri) {
        ImageView imageView = findViewById(R.id.basic_info_edit_image);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setTag(imageUri);
        ImageLoad.loadImage(this, imageUri, imageView);
    }
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "Select picture"),
                REQ_CODE_PICK_IMAGE);
    }


}
