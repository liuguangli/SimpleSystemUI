package com.android.systemui;

import android.content.Context;
import android.os.Build;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManagerGlobal;
import android.widget.TextView;

import com.android.systemui.servicecallback.NotificationReceiver;
import com.android.systemui.servicecallback.StatusServiceCallback;
import com.android.systemui.view.MainView;
import com.android.systemui.window.SystemUIWindowManager;

/**
 * @author liuguangli
 * @date 2018/12/5
 */
public class StatusBar {
    private static final String TAG = "StatusBar";
    public static final int STATUS_BAR_UNHIDE = 0x10000000;
    public static final int NAVIGATION_BAR_UNHIDE = 0x20000000;
    private SystemUIWindowManager mWindowManager;
    private static StatusBar instance = new StatusBar();
    // 主窗口
    private MainView mWindView;
    private int BAR_HEIGHT = 24;
    private Context mContext;
    private int mSystemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE;
    private int mLastDispatchedSystemUiVisibility = ~View.SYSTEM_UI_FLAG_VISIBLE;
    protected IWindowManager mWindowManagerService;

    public static StatusBar getInstance() {
        return instance;
    }

    private StatusBar(){};

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void start() {


        mWindowManager = SystemUIWindowManager.getInstance();
        mContext = SystemUIApplication.getInstance().getApplicationContext();
        mWindowManagerService = WindowManagerGlobal.getWindowManagerService();
        addWindowView();
        registerServiceCallback();
        setSystemUiVisibility(0, 0xffffffff);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void registerServiceCallback() {
        new NotificationReceiver().registger(mContext);
        new StatusServiceCallback().register(mContext);
    }



    private void addWindowView() {
        mWindView = new MainView(mContext);
        mWindowManager.addWindowView(mWindView, BAR_HEIGHT);
    }

    public void logView(String msg) {
        mWindView.logView(msg);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onNotificationPost(StatusBarNotification sbn, View view) {
        mWindView.onNotificationPost(sbn, view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onNotificationRemoved(StatusBarNotification sbn) {
        mWindView.onNotificationRemoved(sbn);
    }

    public void fullWindow() {
        mWindowManager.fullWindow();
    }

    public void minWindow() {

        mWindowManager.minWindow();
    }


    public void setSystemUiVisibility(int vis, int mask) {
        // todo setSystemUiVisibility flag 的实现。

        final int oldVal = mSystemUiVisibility;
        final int newVal = (oldVal&~mask) | (vis&mask);
        final int diff = newVal ^ oldVal;
       /* if (DEBUG) Log.d(TAG, String.format(
                "setSystemUiVisibility Hexvis=%s mask=%s oldVal=%s newVal=%s diff=%s",
                Integer.toHexString(vis), Integer.toHexString(mask),
                Integer.toHexString(oldVal), Integer.toHexString(newVal),
                Integer.toHexString(diff)));*/

        Log.d(TAG,"setSystemUiVisibility 16 进制 vis=" + Integer.toHexString(vis));
        /*if (1560 == vis) {
            mCanExpendable = false;
        } else {
            mCanExpendable = true;
        }*/
        if (diff != 0) {
            mSystemUiVisibility = newVal;



            // ready to unhide
            if ((vis & STATUS_BAR_UNHIDE) != 0) {
                mSystemUiVisibility &= ~STATUS_BAR_UNHIDE;

            }



            if ((vis & NAVIGATION_BAR_UNHIDE) != 0) {
                mSystemUiVisibility &= ~NAVIGATION_BAR_UNHIDE;
            }

            // send updated sysui visibility to window manager
            notifyUiVisibilityChanged(mSystemUiVisibility);
        }
    }

    private void notifyUiVisibilityChanged(int vis) {
        try {
            if (mLastDispatchedSystemUiVisibility != vis) {
                mWindowManagerService.statusBarVisibilityChanged(vis);
                mLastDispatchedSystemUiVisibility = vis;
            }
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public void disable(int arg1) {
        mWindView.disable(arg1);
    }

    public void topAppWindowChanged(boolean b) {
        if (b) {
            setSystemUiVisibility(0,  View.SYSTEM_UI_FLAG_LOW_PROFILE);
        } else {
            setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE, View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }
}
