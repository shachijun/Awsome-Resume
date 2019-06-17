package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.myapplication.model.WorkExperience;
import com.example.myapplication.util.DateUtils;

import java.util.Arrays;

public class WorkExperienceEdit extends AppCompatActivity {
    //用全局变量来传数据是错误的，外界是可以随意修改的，如果是多线程的情况是有风险的 unpredictable

    public static final String KEY_WORK_EXPERIENCE = "Work_Experience";
    private WorkExperience workExperience;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//返回符号
        workExperience = getIntent().getParcelableExtra(KEY_WORK_EXPERIENCE);
        if(workExperience != null){
            setupUI();
        }
        setTitle(workExperience == null ? "New Work Experience" : "Edit Work Experience");
    }

    private void setupUI() {
        ((EditText)findViewById(R.id.Work_Title)).setText(workExperience.title);
        ((EditText)findViewById(R.id.Work_Des)).setText(TextUtils.join("\n", workExperience.description));
        ((EditText)findViewById(R.id.Work_startDate)).setText(DateUtils.dateToString(workExperience.startDate));
        ((EditText)findViewById(R.id.Work_endDate)).setText(DateUtils.dateToString(workExperience.endDate));
        findViewById(R.id.delete).setVisibility(View.VISIBLE);
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(WorkExperienceEdit.this);
                ab.setMessage("Are you sure to delete?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
    }
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KEY_WORK_EXPERIENCE, workExperience.id);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

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
        if(workExperience == null)
            workExperience = new WorkExperience();
        workExperience.title = ((EditText)findViewById(R.id.Work_Title)).getText().toString();
        workExperience.startDate = DateUtils.stringToDate(
                ((TextView)findViewById(R.id.Work_startDate)).getText().toString());
        workExperience.endDate =  DateUtils.stringToDate(
                ((TextView)findViewById(R.id.Work_endDate)).getText().toString());
        workExperience.description = Arrays.asList(TextUtils.split(
                ((EditText)findViewById(R.id.Work_Des)).getText().toString(), "\n"));

        //date picker for picking the date

        Intent resultIntent = new Intent();//intend as hashmap -> key,value
        resultIntent.putExtra(KEY_WORK_EXPERIENCE, workExperience);//putExtra 放的是基本的信息，不能放自己建立的Object, serialize and deserialize (加密解密)
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }


}
