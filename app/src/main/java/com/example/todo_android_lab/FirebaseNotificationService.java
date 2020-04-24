package com.example.todo_android_lab;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FirebaseNotificationService extends Service {
    public FirebaseNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
