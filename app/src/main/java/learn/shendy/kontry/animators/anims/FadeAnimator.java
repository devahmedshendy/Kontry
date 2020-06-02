package learn.shendy.kontry.animators.anims;

import android.animation.ObjectAnimator;
import android.view.View;

import learn.shendy.kontry.animators.interfaces.ObjectAnimatorBuild;

public class FadeAnimator implements ObjectAnimatorBuild {

    private View mTarget;

    private float mFrom;
    private float mTo;

    public static FadeAnimator Builder(View target) {
        return new FadeAnimator(target);
    }

    private FadeAnimator(View target) {
        mTarget = target;
    }
    
    public FadeAnimator from(float value) {
        mFrom = value;
        return this;
    }
    
    public FadeAnimator to(float value) {
        mTo = value;
        return this;
    }

    @Override
    public ObjectAnimator build() {
        return ObjectAnimator.ofFloat(mTarget, View.ALPHA, mFrom, mTo);
    }
}
