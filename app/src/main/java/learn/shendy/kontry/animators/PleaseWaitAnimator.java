package learn.shendy.kontry.animators;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import learn.shendy.kontry.animators.anims.BaseAnimator;
import learn.shendy.kontry.animators.anims.FadeAnimator;
import learn.shendy.kontry.animators.interfaces.ShowHideAnimator;

public class PleaseWaitAnimator extends BaseAnimator implements ShowHideAnimator {

    private static final int SHOW_DURATION = 700;
    private static final int HIDE_DURATION = 300;

    private View mContainer;

    private AnimatorSet mShowAnimator;
    private AnimatorSet mHideAnimator;

    public PleaseWaitAnimator(View container) {
        mContainer = container;

        mShowAnimator = setupShowAnimator();
        mHideAnimator = setupHideAnimator();

        resetDefaultState();
    }

    private AnimatorSet setupShowAnimator() {
        ObjectAnimator fadeContainerIn = FadeAnimator
                .Builder(mContainer)
                .from(0f)
                .to(1f)
                .build();

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(fadeContainerIn);
        animatorSet.setDuration(SHOW_DURATION);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mContainer.setVisibility(View.VISIBLE);
            }
        });

        return animatorSet;
    }

    private AnimatorSet setupHideAnimator() {
        ObjectAnimator fadeContainerOut = FadeAnimator
                .Builder(mContainer)
                .from(1f)
                .to(0f)
                .build();

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(fadeContainerOut);
        animatorSet.setDuration(HIDE_DURATION);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mContainer.setVisibility(View.GONE);
            }
        });

        return animatorSet;
    }

    private void resetDefaultState() {
        mContainer.setVisibility(View.GONE);
    }

    @Override
    public void show() {
        setCurrentAnimation(mShowAnimator);
        super.start();
    }

    @Override
    public void hide() {
        setCurrentAnimation(mHideAnimator);
        super.start();
    }
}
