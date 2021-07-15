package com.sahil.workmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.state.State;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public  static final String KEY_TASK_DESC = "key_task_desc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //for sending data to the work manager class
        Data data = new Data.Builder()
                .putString(KEY_TASK_DESC,"Hii..I am sending the work  data..okk")
                .build();


        Constraints constraints = new Constraints.Builder()   // setting constraint for work to be done
                .setRequiresCharging(true)
                .build();


        //creating one time work request direct because they are abstract class and can be used directly subclass

//        final OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class).build();//paasing the MyWorker class and calling a build method

        final OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setConstraints(constraints)
                .setInputData(data)   // attached data object to work request and now we can access on MY Worker class
                .build();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to perform the work we need work manager
                WorkManager.getInstance().enqueue(oneTimeWorkRequest);
            }
        });

        final TextView textView = findViewById(R.id.textView);
        //to get the work status we use workmanager class
        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) { //when work info is changed this method is called

                        if(workInfo!=null) {

                            if(workInfo.getState().isFinished()){
                                Data data1 = workInfo.getOutputData();
                                String output = data1.getString(MyWorker.KEY_TASK_OUTPUT);
                                textView.append(output + "\n");
                            }
                            String staus = workInfo.getState().name();
                            textView.append(staus + "\n");
                        }
                    }
                });
    }
}