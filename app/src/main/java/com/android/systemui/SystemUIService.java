package com.android.systemui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SystemUIService extends Service {
    public SystemUIService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return null;
    }
}
