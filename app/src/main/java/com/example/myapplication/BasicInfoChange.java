package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.myapplication.model.BasicInfo;

public class BasicInfoChange extends AppCompatActivity {
    public static final String KEY_BASIC_INFO = "Personal_Info_Change";
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

    private void setupUI() {
        ((EditText)findViewById(R.id.Name)).setText(basicInfo.name);
        ((EditText)findViewById(R.id.email)).setText(basicInfo.email);
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

}
