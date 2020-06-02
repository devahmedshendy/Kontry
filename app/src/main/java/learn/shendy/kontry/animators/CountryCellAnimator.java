package learn.shendy.kontry.animators;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import learn.shendy.kontry.animators.anims.BaseAnimator;
import learn.shendy.kontry.animators.anims.FadeAnimator;
import learn.shendy.kontry.animators.anims.RotateAnimator;
import learn.shendy.kontry.animators.interfaces.IForwardBackwardAnimator;

public class CountryCellAnimator extends BaseAnimator implements IForwardBackwardAnimator {

    private static final int FORWARD_BACKWARD_DURATION = 500;
    private static final int TRANSITION_DURATION = 500;

    private TextView mCountryNameTV;
    private ImageView mExpandBtn;
    private View mCellWrapper;

    private TransitionDrawable mExpandBtnTransitionDrawable;
    private TransitionDrawable mCellTransitionDrawable;
    private AnimatorSet mExpandBtnForwardAnimation;
    private AnimatorSet mExpandBtnBackwardAnimation;

    public CountryCellAnimator(TextView countryNameTV, ImageView expandBtn, View cellWrapper) {
        mCountryNameTV = countryNameTV;
        mExpandBtn = expandBtn;
        mCellWrapper = cellWrapper;

        mExpandBtnTransitionDrawable = setupExpandBtnTransitionDrawable();
        mCellTransitionDrawable = setupCellTransitionDrawable();
        mExpandBtnForwardAnimation = setupExpandBtnForwardAnimation();
        mExpandBtnBackwardAnimation = setupExpandBtnBackwardAnimation();
    }

    private TransitionDrawable setupCellTransitionDrawable() {
        return (TransitionDrawable) mCellWrapper.getBackground();
    }

    private TransitionDrawable setupExpandBtnTransitionDrawable() {
        return (TransitionDrawable) mExpandBtn.getDrawable();
    }

    private AnimatorSet setupExpandBtnForwardAnimation() {
        ObjectAnimator fadeOutCountryName = FadeAnimator
                .Builder(mCountryNameTV)
                .from(1f).to(0f)
                .build();

        ObjectAnimator rotateExpandBtn = RotateAnimator
                .Builder(mExpandBtn)
                .setRotation(180f)
                .build();

        ObjectAnimator fadeInCountryName = FadeAnimator
                .Builder(mCountryNameTV)
                .from(0f).to(1f)
                .build();

//        mCountryNameTV.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//        ObjectAnimator x = ObjectAnimator.of(mCountryNameTV, View.TEXT_ALIGNMENT_CENTER, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet
//                .play(fadeOutCountryName).with(rotateExpandBtn)
//                .before(fadeInCountryName);
        animatorSet.play(
                rotateExpandBtn
        );
        animatorSet.setDuration(FORWARD_BACKWARD_DURATION);

        return animatorSet;
    }

    private AnimatorSet setupExpandBtnBackwardAnimation() {
        ObjectAnimator fadeOutCountryName = FadeAnimator
                .Builder(mCountryNameTV)
                .from(0f).to(1f)
                .build();

        Animator rotateExpandBtn = RotateAnimator
                .Builder(mExpandBtn)
                .setRotation(0f)
                .build();

        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.playTogether(
//                fadeOutCountryName,
//                rotateExpandBtn
//        );

        animatorSet.play(
                rotateExpandBtn
        );
        animatorSet.setDuration(FORWARD_BACKWARD_DURATION);

        return animatorSet;
    }

    @Override
    public void forward() {
        mCellTransitionDrawable.startTransition(TRANSITION_DURATION);
        mExpandBtnTransitionDrawable.startTransition(TRANSITION_DURATION);

        setCurrentAnimation(mExpandBtnForwardAnimation);
        start();
    }

    @Override
    public void backward() {
        mCellTransitionDrawable.reverseTransition(TRANSITION_DURATION);
        mExpandBtnTransitionDrawable.reverseTransition(TRANSITION_DURATION);

        setCurrentAnimation(mExpandBtnBackwardAnimation);
        start();
    }
}
