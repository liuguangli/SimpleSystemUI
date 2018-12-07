package com.android.systemui.window;

import android.app.Application;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.android.systemui.SystemUIApplication;

/**
 * @author liuguangli
 * @date 2018/12/5
 */
public class SystemUIWindowManager {
    private static SystemUIWindowManager instance;
    private static final String TAG = "SystemUIWindowManager";
    private final Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLp;
    private View mStatusBarView;
    private int mBarHeight;
    private WindowManager.LayoutParams mLpChanged;

    public static SystemUIWindowManager getInstance() {
        if (instance == null) {
            instance = new SystemUIWindowManager();
        }
        return instance;
    }

    private SystemUIWindowManager() {
        mContext = SystemUIApplication.getInstance().getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public void addWindowView(View statusBarView,  int barHeight) {
        mLp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                barHeight,
                WindowManager.LayoutParams.TYPE_STATUS_BAR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING
                        | WindowManager.LayoutParams.FLAG_SPLIT_TOUCH
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                        | WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
                PixelFormat.TRANSPARENT);
        mLp.token = new Binder();
        mLp.gravity = Gravity.TOP;
        mLp.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        mLp.setTitle("StatusBar");
        mLp.packageName = mContext.getPackageName();
        mStatusBarView = statusBarView;
        mBarHeight = barHeight;
        mWindowManager.addView(mStatusBarView, mLp);
        mLpChanged = new WindowManager.LayoutParams();
        mLpChanged.copyFrom(mLp);
        Log.d(TAG, "add statusBarView height = " + barHeight);
    }

    public void fullWindow() {
        mLp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        mWindowManager.updateViewLayout(mStatusBarView, mLp);
    }

    public void minWindow() {
        mLp.height = mBarHeight;
        mWindowManager.updateViewLayout(mStatusBarView, mLp);
    }
}
