package com.android.systemui.servicecallback;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;

import com.android.systemui.SystemUIApplication;
import com.android.systemui.StatusBar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author liuguangli
 * @date 2018/12/5
 */
@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationReceiver  {


    NotificationListenerService mNotificationListener = new NotificationListenerService() {
        @Override
        @SuppressLint("LongLogTag")
        public void onNotificationPosted(StatusBarNotification sbn) {
            super.onNotificationPosted(sbn);
            Log.d("NotificationListenerService", "onNotificationPosted" + sbn);
            if (sbn.getNotification().contentView != null) {
                View view =  sbn.getNotification().contentView.apply(SystemUIApplication.getInstance(), null);
                view.setVisibility(View.VISIBLE);
                Log.d("NotificationListenerService", "add contentView");
                StatusBar.getInstance().onNotificationPost(sbn, view);
            }

            if (sbn.getNotification().bigContentView != null) {
                View view =  sbn.getNotification().bigContentView.apply(SystemUIApplication.getInstance(), null);
                view.setVisibility(View.VISIBLE);
                Log.d("NotificationListenerService", "add bigContentView");
                StatusBar.getInstance().onNotificationPost(sbn, view);
            }



        }
        @SuppressLint("LongLogTag")
        @Override
        public void onNotificationRemoved(StatusBarNotification sbn) {
            super.onNotificationRemoved(sbn);
            StatusBar.getInstance().onNotificationRemoved(sbn);
            Log.d("NotificationListenerService", "onNotificationRemoved" + sbn);
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onListenerConnected() {
            super.onListenerConnected();
            Log.d("NotificationListenerService", "onNotificationRemoved");
        }

        @Override
        public void onListenerDisconnected() {
            super.onListenerDisconnected();
        };
    };



    public void registger(Context context){
        try {

            Method method =
                    NotificationListenerService.class.getMethod("registerAsSystemService", Context.class, ComponentName.class, int.class);

            method.setAccessible(true);
            method.invoke(mNotificationListener, context,
                    new ComponentName(context.getPackageName(), getClass().getCanonicalName()),
                    -1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
