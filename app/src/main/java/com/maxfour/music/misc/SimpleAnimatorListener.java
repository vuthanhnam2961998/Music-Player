package com.maxfour.music.misc;

import android.animation.Animator;
//Cử động của hình ảnh
public abstract class SimpleAnimatorListener implements Animator.AnimatorListener {
    @Override
    public void onAnimationStart(Animator animation) { //bắt đầu

    }

    @Override
    public void onAnimationEnd(Animator animation) {//HẾT

    }

    @Override
    public void onAnimationCancel(Animator animation) {//Hủy bỏ

    }

    @Override
    public void onAnimationRepeat(Animator animation) {//Lặp lại

    }
}
