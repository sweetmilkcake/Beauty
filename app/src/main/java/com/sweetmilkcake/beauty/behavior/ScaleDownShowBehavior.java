package com.sweetmilkcake.beauty.behavior;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.AttributeSet;
import android.view.View;

import com.sweetmilkcake.beauty.util.AnimatorUtil;

public class ScaleDownShowBehavior extends FloatingActionButton.Behavior {

    private boolean mIsAnimating = false; // 是否正在动画
    private boolean mIsShowed = true; // 是否已经显示

    public ScaleDownShowBehavior(Context context, AttributeSet attributeSet) {
        super();
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {

        if (type == ViewCompat.TYPE_TOUCH) {
            if (axes == ViewCompat.SCROLL_AXIS_VERTICAL) {
                return true;
            }
        }
        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull FloatingActionButton child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        if (type == ViewCompat.TYPE_TOUCH) {
            // 手指上滑，隐藏FAB
            if ((dyConsumed > 0 || dyUnconsumed > 0) && !mIsAnimating && mIsShowed) {
                AnimatorUtil.translateHide(child, new StateListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        super.onAnimationStart(view);
                        mIsShowed = false;
                    }
                });
            } else if (dyConsumed < 0 || dyUnconsumed < 0 && !mIsAnimating && !mIsShowed) { // 手指下滑，显示FAB
                AnimatorUtil.translateShow(child, new StateListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        super.onAnimationStart(view);
                        mIsShowed = true;
                    }
                });
            }
        }
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    class StateListener implements ViewPropertyAnimatorListener {
        @Override
        public void onAnimationStart(View view) {
            mIsAnimating = true;
        }

        @Override
        public void onAnimationEnd(View view) {
            mIsAnimating = false;
        }

        @Override
        public void onAnimationCancel(View view) {
            mIsAnimating = false;
        }
    }
}
