package com.gogotalk.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by fucc
 * Date: 2019-07-13 20:31
 */
public class AnimatorUtils {
    public static AnimatorSet animOwnSet;
    public static AnimatorSet animOtherSet;

    //属性动画
    public static void showOwnJiangbei(View xing, View jiangbei, final View yi, View mude, Animator.AnimatorListener listener) {
        xing.setVisibility(View.VISIBLE);
        jiangbei.setVisibility(View.VISIBLE);
        if (animOwnSet != null) {
            animOwnSet.start();
            return;
        }

        //奖杯放大缩小效果
        ObjectAnimator xingAlpha = ObjectAnimator.ofFloat(xing, "alpha", 0f, 1f, 0f, 1f, 0f);
        ObjectAnimator jianbeiScaleX = ObjectAnimator.ofFloat(jiangbei, "scaleX", 0f, 1.2f, 1f, 1.2f, 1f);
        ObjectAnimator jianbeiScaleY = ObjectAnimator.ofFloat(jiangbei, "scaleY", 0f, 1.2f, 1f, 1.2f, 1f);
        xingAlpha.setDuration(1500);
        jianbeiScaleX.setDuration(1500);
        jianbeiScaleY.setDuration(1500);

        // 移动时效果
        int[] mudeLocation = new int[2];
        mude.getLocationInWindow(mudeLocation);
        int[] jiangbeiLocation = new int[2];
        jiangbei.getLocationInWindow(jiangbeiLocation);
        ObjectAnimator jBtranslationX = ObjectAnimator.ofFloat(jiangbei, "translationX", 0, mudeLocation[0] - mude.getWidth() / 2 - jiangbeiLocation[0] - jiangbei.getMeasuredWidth() / 2);
        ObjectAnimator jBtranslationY = ObjectAnimator.ofFloat(jiangbei, "translationY", 0, mudeLocation[1] - mude.getHeight() / 2 - jiangbeiLocation[1] - jiangbei.getMeasuredHeight() / 2);
        jBtranslationX.setInterpolator(new AccelerateInterpolator());
        jBtranslationX.setDuration(800);
        jBtranslationY.setDuration(800);
        ObjectAnimator jBScaleX = ObjectAnimator.ofFloat(jiangbei, "scaleX", 1, 0f);
        ObjectAnimator jBScaleY = ObjectAnimator.ofFloat(jiangbei, "scaleY", 1, 0f);
        jBScaleX.setDuration(800);
        jBScaleY.setDuration(800);
        ObjectAnimator jBAlpha = ObjectAnimator.ofFloat(jiangbei, "alpha", 1f, 0.5f);
        jBAlpha.setInterpolator(new AccelerateInterpolator());
        jBAlpha.setDuration(800);

        //数字动画
        ObjectAnimator yiTransY = ObjectAnimator.ofFloat(yi, "translationY", 0, -150);
        yiTransY.setDuration(600);
        ObjectAnimator yiAlpha = ObjectAnimator.ofFloat(yi, "alpha", 0f, 0.8f, 0.2f, 0.2f, 0.8f, 0);
        yiAlpha.setDuration(1300);
        yiTransY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                yi.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //数字动画
        ObjectAnimator yiTransY2 = ObjectAnimator.ofFloat(yi, "translationY", 0, -150);
        yiTransY2.setDuration(600);
        yiTransY2.setStartDelay(610);

        animOwnSet = new AnimatorSet();
        animOwnSet.setInterpolator(new LinearInterpolator());
        animOwnSet.play(jBtranslationY).with(jBtranslationX).with(jBScaleX).with(jBScaleY)
                .after(xingAlpha).after(jianbeiScaleX).after(jianbeiScaleY)
                .before(yiTransY).before(yiAlpha).before(yiTransY2);
        animOwnSet.start();
        animOwnSet.addListener(listener);
    }

    public static void showOtherJiangbei(View xing, View jiangbei, final View yi, View mude, Animator.AnimatorListener listener) {
        xing.setVisibility(View.VISIBLE);
        jiangbei.setVisibility(View.VISIBLE);

        if (animOtherSet != null) {
            animOtherSet.start();
            return;
        }

        //奖杯放大缩小效果
        ObjectAnimator xingAlpha = ObjectAnimator.ofFloat(xing, "alpha", 0f, 1f, 0f, 1f, 0f);
        ObjectAnimator jianbeiScaleX = ObjectAnimator.ofFloat(jiangbei, "scaleX", 0f, 1.2f, 1f, 1.2f, 1f);
        ObjectAnimator jianbeiScaleY = ObjectAnimator.ofFloat(jiangbei, "scaleY", 0f, 1.2f, 1f, 1.2f, 1f);
        xingAlpha.setDuration(1500);
        jianbeiScaleX.setDuration(1500);
        jianbeiScaleY.setDuration(1500);

        // 移动时效果
        int[] mudeLocation = new int[2];
        mude.getLocationInWindow(mudeLocation);
        int[] jiangbeiLocation = new int[2];
        jiangbei.getLocationInWindow(jiangbeiLocation);

        ObjectAnimator jBtranslationX = ObjectAnimator.ofFloat(jiangbei, "translationX", 0, mudeLocation[0] - mude.getWidth() / 2 - jiangbeiLocation[0] - jiangbei.getMeasuredWidth() / 2);
        ObjectAnimator jBtranslationY = ObjectAnimator.ofFloat(jiangbei, "translationY", 0, mudeLocation[1] - mude.getHeight() / 2 - jiangbeiLocation[1] - jiangbei.getMeasuredHeight() / 2);

        jBtranslationX.setInterpolator(new AccelerateInterpolator());
        jBtranslationX.setDuration(800);
        jBtranslationY.setDuration(800);
        ObjectAnimator jBScaleX = ObjectAnimator.ofFloat(jiangbei, "scaleX", 1, 0f);
        ObjectAnimator jBScaleY = ObjectAnimator.ofFloat(jiangbei, "scaleY", 1, 0f);
        jBScaleX.setDuration(800);
        jBScaleY.setDuration(800);
        ObjectAnimator jBAlpha = ObjectAnimator.ofFloat(jiangbei, "alpha", 1f, 0.5f);
        jBAlpha.setInterpolator(new AccelerateInterpolator());
        jBAlpha.setDuration(800);

        //数字动画
        ObjectAnimator yiTransY = ObjectAnimator.ofFloat(yi, "translationY", 0, -150);
        yiTransY.setDuration(600);
        ObjectAnimator yiAlpha = ObjectAnimator.ofFloat(yi, "alpha", 0f, 0.8f, 0.2f, 0.2f, 0.8f, 0);
        yiAlpha.setDuration(1300);
        yiTransY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                yi.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        //数字动画
        ObjectAnimator yiTransY2 = ObjectAnimator.ofFloat(yi, "translationY", 0, -150);
        yiTransY2.setDuration(600);
        yiTransY2.setStartDelay(610);

        animOtherSet = new AnimatorSet();
        animOtherSet.setInterpolator(new LinearInterpolator());
        animOtherSet.play(jBtranslationY).with(jBtranslationX).with(jBScaleX).with(jBScaleY)
                .after(xingAlpha).after(jianbeiScaleX).after(jianbeiScaleY)
                .before(yiTransY).before(yiAlpha).before(yiTransY2);
        animOtherSet.start();
        animOtherSet.addListener(listener);
    }

    public static void destory() {
        if (animOtherSet != null) {
            animOtherSet.cancel();
            animOtherSet = null;
        }
        if (animOwnSet != null) {
            animOwnSet.cancel();
            animOwnSet = null;
        }
    }

}
