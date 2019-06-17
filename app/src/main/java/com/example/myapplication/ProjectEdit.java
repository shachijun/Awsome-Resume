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


import com.example.myapplication.model.Project;

import java.util.Arrays;

public class ProjectEdit extends AppCompatActivity {
    private Project project;
    public static final String KEY_PROJECT_EDIT = "Work_Project";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_edit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//返回符号
        project = getIntent().getParcelableExtra(KEY_PROJECT_EDIT);
        if(project != null){
            setupUI();
        }
        setTitle(project == null ? "New Project" : "Edit Project");
    }

    private void setupUI() {
        ((EditText)findViewById(R.id.Project)).setText(project.title);
        ((EditText)findViewById(R.id.project_language)).setText(project.language);
        ((EditText)findViewById(R.id.project_des)).setText(TextUtils.join("\n", project.description));
        findViewById(R.id.delete).setVisibility(View.VISIBLE);
        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ProjectEdit.this);
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
                    resultIntent.putExtra(KEY_PROJECT_EDIT, project.id);
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
        if(project == null)
            project = new Project();
        project.title = ((EditText)findViewById(R.id.Project)).getText().toString();
        project.language = ((EditText)findViewById(R.id.project_language)).getText().toString();
        project.description = Arrays.asList(TextUtils.split(
                ((EditText)findViewById(R.id.project_des)).getText().toString(), "\n"));

        //date picker for picking the date

        Intent resultIntent = new Intent();//intend as hashmap -> key,value
        resultIntent.putExtra(KEY_PROJECT_EDIT, project);//putExtra 放的是基本的信息，不能放自己建立的Object, serialize and deserialize (加密解密)
        setResult(Activity.RESULT_OK, resultIntent);

        finish();
    }
}
