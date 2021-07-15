package com.sahil.workmanagerdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {
    public static final String KEY_TASK_OUTPUT = "key_task_output";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {  // inside this method we need to perform methods which we need to execute in background

        Data data = getInputData(); // getting input data by this method

        String input = data.getString(MainActivity.KEY_TASK_DESC); //

        displayNotification("Hey.. this is work",input);


        Data data1 = new Data.Builder()   // for output data
                .putString(KEY_TASK_OUTPUT, "task is finished successfully!!!")
                .build(); // build the output

//          setOutputData(data1);

        return Result.success(data1);
    }

    private void displayNotification(String task ,String desc){
        //to create notification channel we need notification manager
        // ans we will get by get system service and passing the predifened constant
        NotificationManager manager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){  // if sdk version is greater than oreo we have to create notification channel
           // creating a object of notification channel and creating a channel
            NotificationChannel channel = new NotificationChannel("sahil", "sahil", NotificationManager.IMPORTANCE_DEFAULT);

            manager.createNotificationChannel(channel);// channel is created
        }

        // creating notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "sahil")
                .setContentTitle(task)
                .setContentText(desc)
                .setSmallIcon(R.mipmap.ic_launcher_round);
         // using manager object to display the notification
        manager.notify(1, builder.build());
    }
}
