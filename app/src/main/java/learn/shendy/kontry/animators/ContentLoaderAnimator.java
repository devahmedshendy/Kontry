package learn.shendy.kontry.animators;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.interpolator.view.animation.FastOutLinearInInterpolator;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import learn.shendy.kontry.animators.anims.BaseAnimator;
import learn.shendy.kontry.animators.anims.ScaleAnimator;
import learn.shendy.kontry.animators.interfaces.IShowHideAnimator;

public class ContentLoaderAnimator extends BaseAnimator implements IShowHideAnimator {
    private static final String TAG = "ContentLoaderAnimator";

    private static final int SHOW_DURATION = 250;
//    private static final int HIDE_DURATION = 100;

    private boolean isShown = false;

    private AnimatorSet mShowAnimator;
    private Interpolator mShowInterpolator;
    private AnimatorSet mHideAnimator;
    private Interpolator mHideInterpolator;

    private ImageView mContentLoaderIV;
    private ProgressBar mSpinner;

    // MARK: Constructor Methods

    public ContentLoaderAnimator(
            ImageView contentLoaderIV,
            ProgressBar spinner
    ) {
        mContentLoaderIV = contentLoaderIV;
        mSpinner = spinner;

        setDefaultState();

        mShowAnimator = showAnimator();
        mShowInterpolator = new FastOutSlowInInterpolator();
        mHideAnimator = hideAnimator();
        mHideInterpolator = new FastOutLinearInInterpolator();
    }

    // MARK: Animator Methods

    private void setDefaultState() {
        mContentLoaderIV.setScaleX(0f);
        mContentLoaderIV.setScaleY(0f);

        mSpinner.setScaleX(0f);
        mSpinner.setScaleY(0f);
    }

    private AnimatorSet showAnimator() {
        Animator scaleOutContentLoaderIV = ScaleAnimator
                .Builder(mContentLoaderIV)
                .fromX(0f).toX(1f)
                .fromY(0f).toY(1f)
                .build();

        Animator scaleOutSpinner = ScaleAnimator
                .Builder(mSpinner)
                .fromX(0f).toX(1f)
                .fromY(0f).toY(1f)
                .build();

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                scaleOutContentLoaderIV,
                scaleOutSpinner
        );

        animatorSet.setInterpolator(mShowInterpolator);

        animatorSet.setDuration(SHOW_DURATION);

        return animatorSet;
    }

    private AnimatorSet hideAnimator() {
        Animator scaleOutContentLoaderIV = ScaleAnimator
                .Builder(mContentLoaderIV)
                .fromX(1f).toX(0f)
                .fromY(1f).toY(0f)
                .build();

        Animator scaleOutSpinner = ScaleAnimator
                .Builder(mSpinner)
                .fromX(1f).toX(0f)
                .fromY(1f).toY(0f)
                .build();

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                scaleOutContentLoaderIV,
                scaleOutSpinner
        );

        animatorSet.setInterpolator(mHideInterpolator);

        return animatorSet;
    }

    @Override
    public void show() {
        if (!isShown) {
            setCurrentAnimation(mShowAnimator);
            start();
            isShown = true;
        }
    }

    @Override
    public void hide() {
        if (isShown) {
            setCurrentAnimation(mHideAnimator);
            start();
            isShown = false;
        }
    }
}
