package learn.shendy.kontry.animators.anims;

import android.animation.ObjectAnimator;
import android.util.Property;
import android.view.View;

import learn.shendy.kontry.animators.interfaces.ObjectAnimatorBuild;

public class SlideAnimator implements ObjectAnimatorBuild
{
    private Property<View, Float> mProperty;
    private View mTarget;
    private float mFrom;
    private float mTo;

    public static SlideAnimator XBuilder(View target) {
        return new SlideAnimator(target, View.TRANSLATION_X);
    }

    public static SlideAnimator YBuilder(View target) {
        return new SlideAnimator(target, View.TRANSLATION_Y);
    }

    public static SlideAnimator ZBuilder(View target) {
        return new SlideAnimator(target, View.TRANSLATION_Z);
    }

    private SlideAnimator(View target, Property<View, Float> property) {
        mTarget = target;
        mProperty = property;
    }

    @Override
    public ObjectAnimator build() {
        return ObjectAnimator.ofFloat(mTarget, mProperty, mFrom, mTo);
    }

    public SlideAnimator from(float from) {
        mFrom = from;
        return this;
    }

    public SlideAnimator to(float to) {
        mTo = to;
        return this;
    }
}
