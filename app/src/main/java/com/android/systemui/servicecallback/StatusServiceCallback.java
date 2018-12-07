package com.android.systemui.servicecallback;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;

import com.android.internal.statusbar.IStatusBar;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.StatusBar;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author liuguangli
 * @date 2018/12/5
 */
public class StatusServiceCallback extends IStatusBar.Stub{

    private static final int MSG_LOG = 1;
    private static final int MSG_SYSTEM_UI_VISIBILITY = 2;
    private static final int MSG_DISABLE = 3;
    private static final int MSG_TOP_APP_WINDOW_CHANGED = 4;
    protected IStatusBarService mBarService;
    private Handler mHandler;



    public StatusServiceCallback() {
        mHandler = new Handler(Looper.getMainLooper()){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_LOG:
                        StatusBar.getInstance().logView(msg.obj.toString());
                        break;

                    case MSG_SYSTEM_UI_VISIBILITY:
                        StatusBar.getInstance().setSystemUiVisibility(msg.arg1, msg.arg2);
                        break;
                    case MSG_DISABLE:
                        StatusBar.getInstance().disable(msg.arg1);
                        break;
                    case MSG_TOP_APP_WINDOW_CHANGED:
                        StatusBar.getInstance().topAppWindowChanged(msg.arg1 != 0);
                        break;
                }
            }
        };
    }

    public void register(Context context) {
        Class clazz = Context.class;
        Field field = null;
        try {
            field = clazz.getField("STATUS_BAR_SERVICE");
            mBarService =  IStatusBarService.Stub.asInterface(
                    ServiceManager.getService(field.get(clazz).toString()));
            int[] switches = new int[9];
            ArrayList<IBinder> binders = new ArrayList<>();
            ArrayList<String> iconSlots = new ArrayList<>();
            ArrayList<StatusBarIcon> icons = new ArrayList<>();
            Rect fullscreenStackBounds = new Rect();
            Rect dockedStackBounds = new Rect();
            mBarService.registerStatusBar(this, iconSlots, icons, switches, binders,
                    fullscreenStackBounds, dockedStackBounds);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void setIcon(String s, StatusBarIcon statusBarIcon) throws RemoteException {
        log("setIcon s=" + s);
    }

    private void log(String s) {
        Log.d("StatusBarService", s + ", currentThread = " + Thread.currentThread().getId());
        Message msg = mHandler.obtainMessage();
        msg.obj = s;
        msg.what = MSG_LOG;
        mHandler.sendMessage(msg);
    }

    @Override
    public void removeIcon(String s) throws RemoteException {
        log("removeIcon s=" + s);
    }

    @Override
    public void disable(int i, int i1) throws RemoteException {

        log("disable arg1=" + i + ", arg2=" + i1);

        Message msg = mHandler.obtainMessage();
        msg.what = MSG_DISABLE;
        msg.arg1 = i;
        mHandler.sendMessage(msg);
    }

    @Override
    public void animateExpandNotificationsPanel() throws RemoteException {
        log("animateExpandNotificationsPanel");
    }

    @Override
    public void animateExpandSettingsPanel(String s) throws RemoteException {
        log("animateExpandNotificationsPanel s=" +s);
    }

    @Override
    public void animateCollapsePanels() throws RemoteException {
        log("animateExpandNotificationsPanel");
    }

    @Override
    public void togglePanel() throws RemoteException {
        log("togglePanel");
    }

    @Override
    public void setSystemUiVisibility(int i, int i1, int i2, int mask, Rect rect, Rect rect1) throws RemoteException {
        log("setSystemUiVisibility arg1=" + i + ", arg2=" + i1 + ", arg3=" + i2 + ", arg4=" + mask);
        Message msg = mHandler.obtainMessage();
        msg.what = MSG_SYSTEM_UI_VISIBILITY;
        msg.arg1 = i;
        msg.arg2 = mask;
        mHandler.sendMessage(msg);
    }

    @Override
    public void topAppWindowChanged(boolean menuVisible) throws RemoteException {
        log("topAppWindowChanged boolean = " +menuVisible);
        mHandler.removeMessages(MSG_TOP_APP_WINDOW_CHANGED);
        mHandler.obtainMessage(MSG_TOP_APP_WINDOW_CHANGED, menuVisible ? 1 : 0, 0,
                null).sendToTarget();
    }

    @Override
    public void setImeWindowStatus(IBinder iBinder, int i, int i1, boolean b) throws RemoteException {

    }

    @Override
    public void setWindowState(int i, int i1) throws RemoteException {
        log("setWindowState arg1 = " + i + ", arg2=" + i1);
    }

    @Override
    public void showRecentApps(boolean b, boolean b1) throws RemoteException {

    }

    @Override
    public void hideRecentApps(boolean b, boolean b1) throws RemoteException {

    }

    @Override
    public void toggleRecentApps() throws RemoteException {

    }

    @Override
    public void toggleSplitScreen() throws RemoteException {

    }

    @Override
    public void preloadRecentApps() throws RemoteException {

    }

    @Override
    public void cancelPreloadRecentApps() throws RemoteException {

    }

    @Override
    public void showScreenPinningRequest(int i) throws RemoteException {

    }

    @Override
    public void dismissKeyboardShortcutsMenu() throws RemoteException {

    }

    @Override
    public void toggleKeyboardShortcutsMenu(int i) throws RemoteException {

    }

    @Override
    public void appTransitionPending() throws RemoteException {
        log("appTransitionPending " );
    }

    @Override
    public void appTransitionCancelled() throws RemoteException {
        log("appTransitionCancelled " );
    }

    @Override
    public void appTransitionStarting(long l, long l1) throws RemoteException {
        log("appTransitionStarting arg1 = " + l + ", arg2 = " + l1);
    }

    @Override
    public void appTransitionFinished() throws RemoteException {
        log("appTransitionFinished " );
    }

    @Override
    public void showAssistDisclosure() throws RemoteException {
        log("showAssistDisclosure " );
    }

    @Override
    public void startAssist(Bundle bundle) throws RemoteException {
        log("startAssist " );
    }

    @Override
    public void onCameraLaunchGestureDetected(int i) throws RemoteException {
        log("onCameraLaunchGestureDetected " );
    }

    @Override
    public void showPictureInPictureMenu() throws RemoteException {
        log("showPictureInPictureMenu " );
    }

    @Override
    public void showGlobalActionsMenu() throws RemoteException {
        log("showGlobalActionsMenu " );
    }

    @Override
    public void setTopAppHidesStatusBar(boolean b) throws RemoteException {
        log("setTopAppHidesStatusBar  " + b );
    }

    @Override
    public void addQsTile(ComponentName componentName) throws RemoteException {

    }

    @Override
    public void remQsTile(ComponentName componentName) throws RemoteException {

    }

    @Override
    public void clickQsTile(ComponentName componentName) throws RemoteException {

    }

    @Override
    public void handleSystemKey(int i) throws RemoteException {
        log("handleSystemKey  " + i );
    }

    @Override
    public void showShutdownUi(boolean b, String s) throws RemoteException {
        log("showShutdownUi  " + b  + " , " + s);

    }
}
