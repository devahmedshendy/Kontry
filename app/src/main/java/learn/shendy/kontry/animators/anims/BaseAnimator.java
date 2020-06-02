package learn.shendy.kontry.animators.anims;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;

public abstract class BaseAnimator {

    private AnimatorSet mCurrentAnimation;
    private boolean isStarted = false;


    protected void startAnimator(AnimatorSet animatorSet) {
       mCurrentAnimation = animatorSet;

       start();
    }

    public void setCurrentAnimation(AnimatorSet currentAnimation) {
        mCurrentAnimation = currentAnimation;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void start() {
       mCurrentAnimation.start();
       mCurrentAnimation.addListener(new AnimatorListenerAdapter() {
           @Override
           public void onAnimationEnd(Animator animation) {
               isStarted = false;
           }

           @Override
           public void onAnimationStart(Animator animation) {
               isStarted = true;
           }
       });
    }

    public void pause() {
       mCurrentAnimation.pause();
    }

    public void stop() {
       mCurrentAnimation.cancel();
       mCurrentAnimation.removeAllListeners();
    }
}
