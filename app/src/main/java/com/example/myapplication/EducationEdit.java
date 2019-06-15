package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;


import com.example.myapplication.model.Education;
import com.example.myapplication.util.DateUtils;

import java.util.Arrays;

public class EducationEdit extends AppCompatActivity {
    //用全局变量来传数据是错误的，外界是可以随意修改的，如果是多线程的情况是有风险的 unpredictable

    public static final String KEY_EDUCATION = "education";
    private Education education;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//返回符号
        education = getIntent().getParcelableExtra(KEY_EDUCATION);
        if(education != null){
            setupUI();
        }
        setTitle(education == null ? "New education" : "Edit education");
    }

    private void setupUI() {
        ((EditText)findViewById(R.id.School_Name)).setText(education.school);
        ((EditText)findViewById(R.id.School_Major)).setText(education.major);
        ((EditText)findViewById(R.id.School_Courses)).setText(TextUtils.join("\n", education.courses));
        ((EditText)findViewById(R.id.School_startDate)).setText(DateUtils.dateToString(education.startDate));
        ((EditText)findViewById(R.id.School_endDate)).setText(DateUtils.dateToString(education.endDate));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_education_edit, menu);
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
        if(education == null)
            education = new Education();
        education.school = ((EditText)findViewById(R.id.School_Name)).getText().toString();
        education.major = ((EditText)findViewById(R.id.School_Major)).getText().toString();
        education.startDate = DateUtils.stringToDate(
                ((TextView)findViewById(R.id.School_startDate)).getText().toString());
        education.endDate =  DateUtils.stringToDate(
                ((TextView)findViewById(R.id.School_endDate)).getText().toString());
        education.courses = Arrays.asList(TextUtils.split(
                ((EditText)findViewById(R.id.School_Courses)).getText().toString(), "\n"));

        //date picker for picking the date

        Intent resultIntent = new Intent();//intend as hashmap -> key,value
        resultIntent.putExtra(KEY_EDUCATION, education);//putExtra 放的是基本的信息，不能放自己建立的Object, serialize and deserialize (加密解密)
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }


}
