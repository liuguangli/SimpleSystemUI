package com.android.systemui.view;

import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.SparseArray;

import android.view.View;

import android.widget.LinearLayout;


/**
 * @author liuguangli
 * @date 2018/12/7
 */
public class NotificationView extends LinearLayout {
    private static final String TAG = "NotificationView";
    private SparseArray<StatusBarNotification> mNotifications = new SparseArray();
    public NotificationView(@NonNull Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onNotificationPost(StatusBarNotification sbn, View view) {
        String pkg = sbn.getPackageName();
        int hascode = pkg.hashCode();
        Log.d(TAG, "收到 " + pkg + " post 的通知 " + sbn);
        StatusBarNotification old = mNotifications.get(hascode);
        View hasAddChild = null;
        if (old != null) {

           int count =  getChildCount();
           for (int i = 0; i < count; i++) {
               View child = getChildAt(i);
               if (pkg.equals(child.getTag())) {
                   hasAddChild = child;
                   break;
               }
           }
        }
        if (hasAddChild != null) {
            Log.d(TAG, "已经存在， 先删除");
            removeView(hasAddChild);
            mNotifications.remove(hascode);
        }
        addView(view);
        view.setTag(pkg);
        mNotifications.append(hascode, sbn);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onNotificationRemoved(StatusBarNotification sbn) {
        String pkg = sbn.getPackageName();
        int hascode = pkg.hashCode();
        Log.d(TAG, "收到 " + pkg + " 删除的通知 " + sbn);
        mNotifications.remove(hascode);
        int count =  getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (pkg.equals(child.getTag())) {
                removeView(child);
                break;
            }
        }
    }
}
