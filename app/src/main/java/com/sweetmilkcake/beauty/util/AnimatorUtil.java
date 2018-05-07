package com.sweetmilkcake.beauty.util;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

public class AnimatorUtil {

    private static LinearOutSlowInInterpolator sFastOutSlowInInterpolator =
            new LinearOutSlowInInterpolator();
    private static AccelerateDecelerateInterpolator sLinerInterpolator =
            new AccelerateDecelerateInterpolator();

    public static void translateShow(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .translationY(0)
                .setDuration(400)
                .setListener(viewPropertyAnimatorListener)
                .setInterpolator(sFastOutSlowInInterpolator)
                .start();
    }

    public static void translateHide(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        view.setVisibility(View.VISIBLE);
        ViewCompat.animate(view)
                .translationY(260)
                .setDuration(400)
                .setInterpolator(sFastOutSlowInInterpolator)
                .setListener(viewPropertyAnimatorListener)
                .start();
    }

}
