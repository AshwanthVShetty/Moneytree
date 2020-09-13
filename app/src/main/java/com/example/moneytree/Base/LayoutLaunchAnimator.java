package com.example.moneytree.Base;

import android.animation.Animator;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.RelativeLayout;

import com.example.moneytree.R;

public class LayoutLaunchAnimator {
    static public void startAnim(View v,RelativeLayout rl,int c){
        final View view=v;
        final RelativeLayout relativeLayout=rl;
        final int code=c;
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                int x=code==0?relativeLayout.getRight():relativeLayout.getLeft();
                int y=relativeLayout.getBottom();
                int endraius=(int)Math.hypot(relativeLayout.getWidth(),relativeLayout.getHeight());
                final Animator animator= ViewAnimationUtils.createCircularReveal(relativeLayout,x,y,0,endraius);
                animator.start();
            }
        });
    }
}
