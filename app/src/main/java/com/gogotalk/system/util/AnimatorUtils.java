package com.gogotalk.system.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * Created by fucc
 * Date: 2019-07-13 20:31
 */
public class AnimatorUtils {
    public static int tOwnX;
    public static int tOwnY;
    public static int tOtherX;
    public static int tOtherY;

    /**
     * @param xing     背景星星
     * @param jiangbei 奖杯
     * @param addJbNum 奖杯增加的图片
     * @param jbNumTxt 存放奖杯的数目
     * @param jbNum    奖杯的数目
     */
    //属性动画
    public static void showOwnJiangbei(View xing, View jiangbei, final View addJbNum, TextView jbNumTxt, int jbNum) {
        xing.setVisibility(View.VISIBLE);
        jiangbei.setVisibility(View.VISIBLE);
        addJbNum.setVisibility(View.VISIBLE);
        addJbNum.setAlpha(0);

        //奖杯放大缩小效果
        ObjectAnimator xingAlpha = ObjectAnimator.ofFloat(xing, "alpha", 0f, 1f, 0f, 1f, 0f);
        ObjectAnimator jianbeiScaleX = ObjectAnimator.ofFloat(jiangbei, "scaleX", 0f, 1.2f, 1f, 1.2f, 1f);
        ObjectAnimator jianbeiScaleY = ObjectAnimator.ofFloat(jiangbei, "scaleY", 0f, 1.2f, 1f, 1.2f, 1f);
        xingAlpha.setDuration(1500);
        jianbeiScaleX.setDuration(1500);
        jianbeiScaleY.setDuration(1500);

        // 移动时效果
        if (tOwnX <= 0) {
            int[] mudeLocation = new int[2];
            jbNumTxt.getLocationInWindow(mudeLocation);
            int[] jiangbeiLocation = new int[2];
            jiangbei.getLocationInWindow(jiangbeiLocation);
            tOwnX = mudeLocation[0] - jbNumTxt.getWidth() / 2 - jiangbeiLocation[0] - jiangbei.getMeasuredWidth() / 2;
            tOwnY = mudeLocation[1] - jbNumTxt.getHeight() / 2 - jiangbeiLocation[1] - jiangbei.getMeasuredHeight() / 2;
        }
        ObjectAnimator jBtranslationX = ObjectAnimator.ofFloat(jiangbei, "translationX", 0, tOwnX);
        ObjectAnimator jBtranslationY = ObjectAnimator.ofFloat(jiangbei, "translationY", 0, tOwnY);
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
        ObjectAnimator yiTransY = ObjectAnimator.ofFloat(addJbNum, "translationY", 0, -150);
        yiTransY.setDuration(600);
        ObjectAnimator yiAlpha = ObjectAnimator.ofFloat(addJbNum, "alpha", 0f, 0.8f, 0.2f, 0.2f, 0.8f, 0);
        yiAlpha.setDuration(1300);

        //数字动画
        ObjectAnimator yiTransY2 = ObjectAnimator.ofFloat(addJbNum, "translationY", 0, -150);
        yiTransY2.setDuration(600);
        yiTransY2.setStartDelay(610);

        AnimatorSet animOwnSet = new AnimatorSet();
        animOwnSet.setInterpolator(new LinearInterpolator());
        animOwnSet.play(jBtranslationY).with(jBtranslationX).with(jBScaleX).with(jBScaleY)
                .after(xingAlpha).after(jianbeiScaleX).after(jianbeiScaleY)
                .before(yiTransY).before(yiAlpha).before(yiTransY2);
        animOwnSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                xing.setVisibility(View.VISIBLE);
                jiangbei.setVisibility(View.VISIBLE);
                addJbNum.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                jbNumTxt.setText(jbNum + "");
                xing.setVisibility(View.INVISIBLE);
                jiangbei.setVisibility(View.INVISIBLE);
                addJbNum.setVisibility(View.INVISIBLE);
                jiangbei.setTranslationX(0);
                jiangbei.setTranslationY(0);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                jbNumTxt.setText(jbNum + "");
                xing.setVisibility(View.INVISIBLE);
                jiangbei.setVisibility(View.INVISIBLE);
                addJbNum.setVisibility(View.INVISIBLE);
                jiangbei.setTranslationX(0);
                jiangbei.setTranslationY(0);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animOwnSet.start();

    }

    public static void showOtherJiangbei(View xing, View jiangbei, final View yi, TextView mude, int jbNum) {
        xing.setVisibility(View.VISIBLE);
        jiangbei.setVisibility(View.VISIBLE);
        yi.setVisibility(View.VISIBLE);
        yi.setAlpha(0);

        //奖杯放大缩小效果
        ObjectAnimator xingAlpha = ObjectAnimator.ofFloat(xing, "alpha", 0f, 1f, 0f, 1f, 0f);
        ObjectAnimator jianbeiScaleX = ObjectAnimator.ofFloat(jiangbei, "scaleX", 0f, 1.2f, 1f, 1.2f, 1f);
        ObjectAnimator jianbeiScaleY = ObjectAnimator.ofFloat(jiangbei, "scaleY", 0f, 1.2f, 1f, 1.2f, 1f);
        xingAlpha.setDuration(1500);
        jianbeiScaleX.setDuration(1500);
        jianbeiScaleY.setDuration(1500);

        // 移动时效果
        if (tOtherX <= 0) {
            int[] mudeLocation = new int[2];
            mude.getLocationInWindow(mudeLocation);
            int[] jiangbeiLocation = new int[2];
            jiangbei.getLocationInWindow(jiangbeiLocation);
            tOtherX = mudeLocation[0] - mude.getWidth() / 2 - jiangbeiLocation[0] - jiangbei.getMeasuredWidth() / 2;
            tOtherY = mudeLocation[1] - mude.getHeight() / 2 - jiangbeiLocation[1] - jiangbei.getMeasuredHeight() / 2;
        }
        ObjectAnimator jBtranslationX = ObjectAnimator.ofFloat(jiangbei, "translationX", 0, tOtherX);
        ObjectAnimator jBtranslationY = ObjectAnimator.ofFloat(jiangbei, "translationY", 0, tOtherY);

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

        //数字动画
        ObjectAnimator yiTransY2 = ObjectAnimator.ofFloat(yi, "translationY", 0, -150);
        yiTransY2.setDuration(600);
        yiTransY2.setStartDelay(610);

        AnimatorSet animOtherSet = new AnimatorSet();
        animOtherSet.setInterpolator(new LinearInterpolator());
        animOtherSet.play(jBtranslationY).with(jBtranslationX).with(jBScaleX).with(jBScaleY)
                .after(xingAlpha).after(jianbeiScaleX).after(jianbeiScaleY)
                .before(yiTransY).before(yiAlpha).before(yiTransY2);
        animOtherSet.start();
        animOtherSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                xing.setVisibility(View.VISIBLE);
                jiangbei.setVisibility(View.VISIBLE);
                yi.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mude.setText(jbNum + "");
                xing.setVisibility(View.INVISIBLE);
                jiangbei.setVisibility(View.INVISIBLE);
                yi.setVisibility(View.INVISIBLE);
                jiangbei.setTranslationX(0);
                jiangbei.setTranslationY(0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mude.setText(jbNum + "");
                xing.setVisibility(View.INVISIBLE);
                jiangbei.setVisibility(View.INVISIBLE);
                yi.setVisibility(View.INVISIBLE);
                jiangbei.setTranslationX(0);
                jiangbei.setTranslationY(0);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public static void startVideoTranstate(TextureView mTV) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(mTV, "translationX", 0, -mTV.getMeasuredWidth())
                .setDuration(1000);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mTV, "translationY", 0, mTV.getMeasuredHeight())
                .setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translationX).before(translationY);
        animatorSet.start();
    }

    public static void recoverVideoTranstate(TextureView mTV) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(mTV, "translationX", -mTV.getMeasuredWidth(), 0)
                .setDuration(1000);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(mTV, "translationY", mTV.getMeasuredHeight(), 0)
                .setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translationY).before(translationX);
        animatorSet.start();
    }

}
