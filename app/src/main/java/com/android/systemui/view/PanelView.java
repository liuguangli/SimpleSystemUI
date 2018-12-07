package com.android.systemui.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.systemui.R;
import com.android.systemui.StatusBar;


/**
 * @author liuguangli
 * @date 2018/12/6
 */
public class PanelView extends FrameLayout {
    private float mUpX;
    private float mUpY;
    private float mDownX;
    private float mDownY;
    private boolean expended;
    private ObjectAnimator mAnimator;
    private boolean mAnimRunning;
    private float SCREEN_HEIGHT = 600f;
    private float mExpendHeight = 0;
    private float mLastTouchY;
    private NotificationView mNotificationView;

    public PanelView(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_panel, this);
        setTranslationY(-SCREEN_HEIGHT);
        mNotificationView = new NotificationView(getContext());
        ViewGroup notificationWrapper = findViewById(R.id.notification_wrapper);
        notificationWrapper.addView(mNotificationView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void onNotificationPost(StatusBarNotification sbn, View view) {
        mNotificationView.onNotificationPost(sbn, view);
    }
    public void onNotificationRemoved(StatusBarNotification sbn) {
        mNotificationView.onNotificationRemoved(sbn);
    }
    public void handleTouchEvent(MotionEvent event) {
        if (mAnimRunning) {
            return;
        }
        switch (event.getAction()) {

            case  MotionEvent.ACTION_DOWN:
                mDownX = event.getRawX();
                mDownY = event.getRawY();
                mLastTouchY = mDownY;
                setVisibility(VISIBLE);
                StatusBar.getInstance().fullWindow();
                break;

            case MotionEvent.ACTION_MOVE:


                 float y = event.getRawY();
                 float diff = y - mLastTouchY;
                 mExpendHeight += diff;
                 setTranslationY(mExpendHeight - SCREEN_HEIGHT);
                mLastTouchY = y;
                 break;
            case MotionEvent.ACTION_UP:
                mUpX = event.getRawX();
                mUpY = event.getY();
                startIfAnimation();
                break;

        }
    }

    private void startIfAnimation() {

       float diff = mUpY - mDownY;

       if (diff > 60) {

           animExpend();
       } else if (diff <= - 60) {
           animClose();
       } else {
           if (expended) {
               animExpend();
           } else {
              animClose();
           }
       }

    }

    public void animExpend() {
        if (expended) {
            return;
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mAnimator =  ObjectAnimator//
                .ofFloat(this, "translationY", mUpY - SCREEN_HEIGHT , 0);


        mAnimator.setDuration(500)
                .addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mAnimRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mAnimRunning = false;
                        finishExpended();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mAnimRunning = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        mAnimator.start();

    }

    private void finishExpended() {
        expended = true;
        mExpendHeight = SCREEN_HEIGHT;
    }

    public void animClose() {

        if (!expended) {
            return;
        }
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        mAnimator =  ObjectAnimator//
                .ofFloat(this, "translationY", mExpendHeight - SCREEN_HEIGHT, -SCREEN_HEIGHT);


        mAnimator.setDuration(500)
                .addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mAnimRunning = true;

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mAnimRunning = false;
                        finishClose();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mAnimRunning = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
        mAnimator.start();
    }

    private void finishClose() {

        setVisibility(GONE);
        StatusBar.getInstance().minWindow();
        expended = false;
        mExpendHeight = 0;
    }


}
