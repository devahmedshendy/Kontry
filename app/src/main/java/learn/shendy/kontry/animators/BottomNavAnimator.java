package learn.shendy.kontry.animators;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.view.View;

import learn.shendy.kontry.animators.anims.BaseAnimator;
import learn.shendy.kontry.animators.anims.ScaleAnimator;
import learn.shendy.kontry.animators.anims.SlideAnimator;
import learn.shendy.kontry.animators.interfaces.IEnterLeaveAnimator;
import learn.shendy.kontry.utils.ThemeUtils;

public class BottomNavAnimator extends BaseAnimator implements IEnterLeaveAnimator {
    private static final String TAG = "BottomNavAnimator";

    private static final int SHOW_DURATION = 200;
    private static final int SHOW_DELAY = 400;
    private static final int HIDE_DURATION = 200;

    private static final float ACTION_BAR_SIZE;

    static {
        ACTION_BAR_SIZE = ThemeUtils.getActionBarSize();
    }

    private boolean isEntered = false;

    private View mContainer;
    private View mHomeIV;

    private AnimatorSet mEnterAnimator;
    private AnimatorSet mLeaveAnimator;

    // MARK: Constructor Methods

    public BottomNavAnimator(View container, View homeIV) {
        mContainer = container;
        mHomeIV = homeIV;

        setDefaultState();
        setupEnterAnimator();
        setupLeaveAnimator();
    }

    // MARK: Setup Methods

    public void setDefaultState() {
        mContainer.setVisibility(View.GONE);

        mContainer.setTranslationY(ACTION_BAR_SIZE);

        mHomeIV.setTranslationY(ACTION_BAR_SIZE);
        mHomeIV.setScaleX(0f);
        mHomeIV.setScaleY(0f);
    }

    private void setupEnterAnimator() {
        Animator slideInHomeIVFromBottom = SlideAnimator
                .YBuilder(mHomeIV)
                .from(ACTION_BAR_SIZE).to(0f)
                .build();

        Animator scaleOutHomeIV = ScaleAnimator
                .Builder(mHomeIV)
                .fromX(0f).toX(1f)
                .fromY(0f).toY(1f)
                .build();

        AnimatorSet homeAnimator = new AnimatorSet();

        homeAnimator.playTogether(
                slideInHomeIVFromBottom,
                scaleOutHomeIV
        );
        homeAnimator.setStartDelay(SHOW_DURATION / 2);

        Animator slideInContainerFromBottom = SlideAnimator
                .YBuilder(mContainer)
                .from(ACTION_BAR_SIZE).to(0f)
                .build();

        mEnterAnimator = new AnimatorSet();

        mEnterAnimator.playTogether(
                slideInContainerFromBottom,
                homeAnimator
        );

        mEnterAnimator.setDuration(SHOW_DURATION);
        mEnterAnimator.setStartDelay(SHOW_DELAY);

        mEnterAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupLeaveAnimator() {
        Animator slideOutContainerFromBottom = SlideAnimator
                .YBuilder(mContainer)
                .from(0f).to(ACTION_BAR_SIZE)
                .build();

        mLeaveAnimator = new AnimatorSet();

        mLeaveAnimator.play(slideOutContainerFromBottom);

        mLeaveAnimator.setDuration(HIDE_DURATION);

        mLeaveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setDefaultState();
            }
        });
    }

    @Override
    public void enter() {
        if (!isEntered) {
            setCurrentAnimation(mEnterAnimator);
            super.start();

            isEntered = true;
        }
    }

    @Override
    public void leave() {
        if (isEntered) {
            setCurrentAnimation(mLeaveAnimator);
            super.start();

            isEntered = false;
        }
    }
}
