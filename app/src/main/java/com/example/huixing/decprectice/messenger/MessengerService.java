package com.example.huixing.decprectice.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MessengerService extends Service {
    public MessengerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
