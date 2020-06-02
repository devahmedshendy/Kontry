package learn.shendy.kontry.animators.anims;

import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.view.View;

import learn.shendy.kontry.animators.interfaces.TimeAnimatorBuild;

public class EndlessRotateAnimator implements TimeAnimatorBuild {

    private View mTarget;
    private long mStep;

    private TimeAnimator mEndlessAnimator;
    private ObjectAnimator mRotateAnimator;

    public static EndlessRotateAnimator Builder(View target) {
        return new EndlessRotateAnimator(target);
    }

    private EndlessRotateAnimator(View target) {
        mTarget = target;
    }

    public EndlessRotateAnimator setStep(int stepInMilliseconds) {
        mStep = stepInMilliseconds;
        return this;
    }

    private TimeListener endlessAnimatorListener = new TimeListener() {
        @Override
        public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
            if (deltaTime == mStep) {
                mRotateAnimator.start();
            }
        }
    };

    @Override
    public TimeAnimator build() {
        mRotateAnimator = RotateAnimator
                .Builder(mTarget)
                .setRotation(10)
                .build();

        mEndlessAnimator = new TimeAnimator();

        mEndlessAnimator.setTimeListener(endlessAnimatorListener);

        return mEndlessAnimator;
    }
}
