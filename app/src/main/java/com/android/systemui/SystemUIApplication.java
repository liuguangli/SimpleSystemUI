package com.android.systemui;

import android.app.Application;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * @author liuguangli
 * @date 2018/12/5
 */
public class SystemUIApplication extends Application {
    private static SystemUIApplication instance;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        StatusBar statusBar = StatusBar.getInstance();
        statusBar.start();
    }

    public static SystemUIApplication getInstance() {
        return instance;
    }
}
