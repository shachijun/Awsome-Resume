package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.model.BasicInfo;
import com.example.myapplication.model.Education;
import com.example.myapplication.model.ID;
import com.example.myapplication.model.Project;
import com.example.myapplication.model.WorkExperience;
import com.example.myapplication.util.Data;
import com.example.myapplication.util.DateUtils;
import com.example.myapplication.util.ImageLoad;
import com.example.myapplication.util.PermissionUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int Request_Code_Education_Edit = 100;
    private static final int Request_Code_Basic_Info_Edit = 101;
    private static final int Request_Code_Work_Experience_Edit = 102;
    private static final int Request_Code_Project_Edit = 103;

    private BasicInfo basicInfo;
    private List<Education> educations = new ArrayList<>();
    private List<WorkExperience> workExperiences = new ArrayList<>();
    private List<Project> projects = new ArrayList<>();

    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "work_experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFO = "basic_info";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PermissionUtils.checkPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            PermissionUtils.requestReadExternalStoragePermission(MainActivity.this);
        }
        InitializeData();
        setupUI();
    }


    //返回到主程序 从上一个程序
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionUtils.REQ_CODE_WRITE_EXTERNAL_STORAGE
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }else{
            Toast.makeText(this, "Did not have permission", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case Request_Code_Basic_Info_Edit:
                    basicInfo = data.getParcelableExtra(BasicInfoChange.KEY_BASIC_INFO);
                    UpdateBasicInfo();
                    setupBasicInfoUI();
                    break;
                case Request_Code_Education_Edit:
                    String EducationID = data.getStringExtra(EducationEdit.KEY_EDUCATION);
                    if(EducationID != null){
                        DeleteEducation(EducationID);
                    }else{
                        Education education = data.getParcelableExtra(EducationEdit.KEY_EDUCATION);
                        UpdateEducation(education);
                    }
                    setupEducationsUI();
                    break;
                case Request_Code_Work_Experience_Edit:
                    String WorkID = data.getStringExtra(WorkExperienceEdit.KEY_WORK_EXPERIENCE);
                    if(WorkID != null){
                        DeleteWorkExperience(WorkID);
                    }else {
                        WorkExperience work = data.getParcelableExtra(WorkExperienceEdit.KEY_WORK_EXPERIENCE);
                        UpdateWorkExperience(work);
                    }
                    setupWorkssUI();
                    break;
                case Request_Code_Project_Edit:
                    String ProjectID = data.getStringExtra(ProjectEdit.KEY_PROJECT_EDIT);
                    if(ProjectID != null){
                        DeleteProject(ProjectID);
                    }else {
                        Project project = data.getParcelableExtra(ProjectEdit.KEY_PROJECT_EDIT);
                        UpdateProject(project);
                    }
                    setupProjectsUI();
                    break;
            }
        }
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        setupBasicInfoUI();
        setupEducationsUI();
        setupWorkssUI();
        setupProjectsUI();
    }
    /*
    * edit different UI
    * ****************************************************************************************************/
    //basic info edit
    private void setupBasicInfoUI() {
        ((TextView) findViewById(R.id.name)).setText(TextUtils.isEmpty(basicInfo.name) ? "Your name" : basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(TextUtils.isEmpty(basicInfo.email) ? "Your email" : basicInfo.email);
        ImageView userPicture = findViewById(R.id.user_pic);
        if (basicInfo.photo != null) {
            ImageLoad.loadImage(this, basicInfo.photo, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.noimage);
        }

        findViewById(R.id.NameChange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasicInfoChange.class);
                intent.putExtra(BasicInfoChange.KEY_BASIC_INFO, basicInfo);
                startActivityForResult(intent, Request_Code_Basic_Info_Edit);
            }
        });
    }

    //education get and edit
    private View getEducationView(final Education e){
        View view = getLayoutInflater().inflate(R.layout.education_item,null);
        ((TextView) view.findViewById(R.id.EducationName)).setText(
                e.school + "(" + DateUtils.dateToString(e.startDate) + " ~ " +
                        DateUtils.dateToString(e.endDate) + ")");
        ((TextView) view.findViewById(R.id.major)).setText(e.major);
        ((TextView) view.findViewById(R.id.courses)).setText(formatCourses(e.courses));
        //click modify button
        view.findViewById(R.id.EduactionChange).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationEdit.class);
                intent.putExtra(EducationEdit.KEY_EDUCATION, e);
                startActivityForResult(intent, Request_Code_Education_Edit);
            }
        });
        return view;
    }
    private void setupEducationsUI() {
        LinearLayout educationContainer = (LinearLayout) findViewById(R.id.EducationContainer);
        educationContainer.removeAllViews();
        for (ID i : educations) {
            Education e = (Education) i;
            View view = getEducationView(e);//数据转界面
            educationContainer.addView(view);
        }
        findViewById(R.id.add_education).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EducationEdit.class);
                startActivityForResult(intent, Request_Code_Education_Edit);
            }
        });
    }

    //Work Experience get and edit
    private View getWorkView(final WorkExperience e){
        View view = getLayoutInflater().inflate(R.layout.work_item,null);
        ((TextView) view.findViewById(R.id.Work_title)).setText(
                 e.title+ "(" + DateUtils.dateToString(e.startDate) + " ~ " +
                        DateUtils.dateToString(e.endDate) + ")");
        ((TextView) view.findViewById(R.id.Work_Desc)).setText(formatCourses(e.description));
        //click modify button
        view.findViewById(R.id.WorkExperienceChange).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WorkExperienceEdit.class);
                intent.putExtra(WorkExperienceEdit.KEY_WORK_EXPERIENCE, e);
                startActivityForResult(intent, Request_Code_Work_Experience_Edit);
            }
        });
        return view;
    }

    private void setupWorkssUI() {
        LinearLayout educationContainer = (LinearLayout) findViewById(R.id.WorkExperienceContainer);
        educationContainer.removeAllViews();
        for (ID i : workExperiences) {
            WorkExperience e = (WorkExperience) i;
            View view = getWorkView(e);//数据转界面
            educationContainer.addView(view);
        }
        findViewById(R.id.add_work_experience).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WorkExperienceEdit.class);
                startActivityForResult(intent, Request_Code_Work_Experience_Edit);
            }
        });
    }

    //Project get and edit
    private View getProjectView(final Project e){
        View view = getLayoutInflater().inflate(R.layout.project_item,null);
        ((TextView) view.findViewById(R.id.Project_title)).setText(e.title);
        ((TextView) view.findViewById(R.id.project_language)).setText(e.language);
        ((TextView) view.findViewById(R.id.project_des)).setText(formatCourses(e.description));
        //click modify button
        view.findViewById(R.id.Project_Change).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProjectEdit.class);
                intent.putExtra(ProjectEdit.KEY_PROJECT_EDIT, e);
                startActivityForResult(intent, Request_Code_Project_Edit);
            }
        });
        return view;
    }

    private void setupProjectsUI() {
        LinearLayout educationContainer = (LinearLayout) findViewById(R.id.ProjectContainer);
        educationContainer.removeAllViews();
        for (ID i : projects) {
            Project e = (Project) i;
            View view = getProjectView(e);//数据转界面
            educationContainer.addView(view);
        }
        findViewById(R.id.add_project).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProjectEdit.class);
                startActivityForResult(intent, Request_Code_Project_Edit);
            }
        });
    }


    /*
     * Initialize Data
     * ****************************************************************************************************/
    private void InitializeData() {
        BasicInfo savedBasicInfo = Data.read(this,
                MODEL_BASIC_INFO,
                new TypeToken<BasicInfo>(){});
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = Data.read(this,
                MODEL_EDUCATIONS,
                new TypeToken<List<Education>>(){});

        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

        List<WorkExperience> savedExperience = Data.read(this,
                MODEL_EXPERIENCES,
                new TypeToken<List<WorkExperience>>(){});
        workExperiences = savedExperience == null ? new ArrayList<WorkExperience>() : savedExperience;

        List<Project> savedProjects = Data.read(this,
                MODEL_PROJECTS,
                new TypeToken<List<Project>>(){});
        projects = savedProjects == null ? new ArrayList<Project>() : savedProjects;
    }

    public static String formatCourses(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /*
    * Update data:
    * ************************************************************/
    private void UpdateBasicInfo(){
        Data.save(this, MODEL_BASIC_INFO, basicInfo);
    }
    private void UpdateEducation(Education e){
        boolean found = false;
        for (int i = 0; i < educations.size(); ++i) {
            if (educations.get(i).id.equals(e.id)) {
                found = true;
                educations.set(i, e);
                break;
            }
        }
        if (!found) {
            educations.add(e);
        }
        Data.save(this, MODEL_EDUCATIONS,educations);
    }
    private void UpdateWorkExperience(WorkExperience w){
        boolean found = false;
        for (int i = 0; i < workExperiences.size(); ++i) {
            if (workExperiences.get(i).id.equals(w.id)) {
                found = true;
                workExperiences.set(i, w);
                break;
            }
        }
        if (!found) {
            workExperiences.add(w);
        }
        Data.save(this, MODEL_EXPERIENCES, workExperiences);
    }
    private void UpdateProject(Project p){
        boolean found = false;
        for (int i = 0; i < projects.size(); ++i) {
            if (projects.get(i).id.equals(p.id)) {
                found = true;
                projects.set(i, p);
                break;
            }
        }
        if (!found) {
            projects.add(p);
        }
        Data.save(this, MODEL_PROJECTS, projects);
    }
    /*
     * Delete data:
     * ************************************************************/
    private void DeleteEducation(String id){
        for (int i = 0; i < educations.size(); ++i) {
            if (educations.get(i).id.equals(id)) {
                educations.remove(i);
                break;
            }
        }
        Data.save(this, MODEL_EDUCATIONS,educations);
    }
    private void DeleteWorkExperience(String id){
        for (int i = 0; i < workExperiences.size(); ++i) {
            if (workExperiences.get(i).id.equals(id)) {
                workExperiences.remove(i);
                break;
            }
        }
        Data.save(this, MODEL_EXPERIENCES, workExperiences);
    }
    private void DeleteProject(String id){
        for (int i = 0; i < projects.size(); ++i) {
            if (projects.get(i).id.equals(id)) {
                projects.remove(i);
                break;
            }
        }
        Data.save(this, MODEL_PROJECTS, projects);
    }
}
