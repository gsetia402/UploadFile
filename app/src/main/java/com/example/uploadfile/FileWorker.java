package com.example.uploadfile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Base64;

import retrofit2.Call;
import retrofit2.Response;

public class FileWorker extends Worker {

    private static final String CHANNEL_ID = "file_upload_channel";
    private static final int NOTIFICATION_ID = 1;

    public FileWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String fileUri = getInputData().getString("fileUri");

        String fileUriString = getInputData().getString("fileUri");

        // Convert the file to base64
        String base64File = FileUtil.convertFileToBase64(getApplicationContext(), Uri.parse(fileUri));

        // Initialize Retrofit service
        FileUploadService service = RetrofitClient.getClient().create(FileUploadService.class);

        // Create the request body
        FileUploadRequest request = new FileUploadRequest(base64File);

        // Perform file upload
        Call<Void> call = service.uploadFile(request);

        try {
            Response<Void> response = call.execute();

            if (response.isSuccessful()) {
                // File upload successful
            } else {
                Log.e("error",response.message());
                // Handle error
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exception
        }


        // Perform the file upload and update progress
        int progress = 0;
        while (progress < 100) {
            // Update the progress (replace this with your actual upload progress logic)
            progress += 10;

            // Show progress in the notification
            showProgressNotification(progress);

            // Simulate file upload progress
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // File upload completed, hide the notification
        hideProgressNotification();

        return Result.success();
    }

    private void showProgressNotification(int progress) {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "File Upload Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("File Upload Progress")
                .setContentText("Uploading...")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setProgress(100, progress, false);

        Notification notification = builder.build();

        // Update the notification
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private void hideProgressNotification() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // Remove the notification when the upload is completed
        notificationManager.cancel(NOTIFICATION_ID);
    }

}
