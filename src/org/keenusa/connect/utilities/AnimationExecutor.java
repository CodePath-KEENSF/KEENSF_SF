package org.keenusa.connect.utilities;

import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.view.View;
import android.view.ViewGroup;

public class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

	WeakHashMap<View,Integer> mOriginalViewHeightPool;
	
	private void setmOriginalViewHeightPool(){
		mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
	}
	
	@Override
    public void executeAnim(final View target, final int animType) {
        
		setmOriginalViewHeightPool();
		
		if(ExpandableStickyListHeadersListView.ANIMATION_EXPAND==animType&&target.getVisibility()==View.VISIBLE){
            return;
        }
        if(ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE==animType&&target.getVisibility()!=View.VISIBLE){
            return;
        }
        if(mOriginalViewHeightPool.get(target)==null){
            mOriginalViewHeightPool.put(target,target.getHeight());
        }
        final int viewHeight = mOriginalViewHeightPool.get(target);
        float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
        float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
        final ViewGroup.LayoutParams lp = target.getLayoutParams();
        
      ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
      animator.setDuration(300);
      target.setVisibility(View.VISIBLE);
      animator.addListener(new AnimatorListener() {
          @Override
          public void onAnimationStart(Animator animator) {
          }

          @Override
          public void onAnimationEnd(Animator animator) {
              if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                  target.setVisibility(View.VISIBLE);
              } else {
                  target.setVisibility(View.GONE);
              }
              target.getLayoutParams().height = viewHeight;
          }

          @Override
          public void onAnimationCancel(Animator animator) {

          }

          @Override
          public void onAnimationRepeat(Animator animator) {

          }
      });
      
      animator.addUpdateListener(new AnimatorUpdateListener() {
          @Override
          public void onAnimationUpdate(ValueAnimator valueAnimator) {
              lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
              target.setLayoutParams(lp);
              target.requestLayout();
          }
      });
      animator.start();
    }
}	