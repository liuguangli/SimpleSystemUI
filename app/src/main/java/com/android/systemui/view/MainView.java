package com.android.systemui.view;

import android.app.StatusBarManager;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.android.systemui.R;
import com.android.systemui.StatusBar;

/**
 * @author liuguangli
 * @date 2018/12/6
 */
public class MainView extends FrameLayout {

    private static final String TAG = MainView.class.getSimpleName();
    private PanelView mPanelView;
    private int mDisable;

    public MainView(Context context) {
        super(context);
        mPanelView = new PanelView(context);
        addView(mPanelView);
        mPanelView.setVisibility(GONE);
    }


    public void logView(String msg) {

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!canExpand()) {
            return false;
        }

        mPanelView.handleTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private boolean canExpand() {

        return (mDisable & StatusBarManager.DISABLE_EXPAND) == 0 ;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onNotificationPost(StatusBarNotification sbn, View view) {
        mPanelView.onNotificationPost(sbn, view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onNotificationRemoved(StatusBarNotification sbn) {
        mPanelView.onNotificationRemoved(sbn);
    }



    public void disable(int arg1) {
       if (!canExpand()) {
           mPanelView.animClose();
       }
       mDisable = arg1;
    }
}
