package learn.shendy.kontry.animators.anims;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import learn.shendy.kontry.animators.interfaces.ObjectAnimatorBuild;

public class RotateAnimator implements ObjectAnimatorBuild {

    private View mTarget;

    private boolean mRotate;
    private float mRotation;

    private boolean mRotateInX;
    private float mRotationX;

    private boolean mRotateInY;
    private float mRotationY;

    public static RotateAnimator Builder(View target) {
        return new RotateAnimator(target);
    }

    private RotateAnimator(View target) {
        mTarget = target;
    }

    public RotateAnimator setRotation(float rotation) {
        mRotation = rotation;
        mRotate = true;
        return this;
    }

    public RotateAnimator setRotationX(float rotationX) {
        mRotationX = rotationX;
        mRotateInX = true;
        return this;
    }

    public RotateAnimator setRotationY(float rotationY) {
        mRotationY = rotationY;
        mRotateInY = true;
        return this;
    }

    @Override
    public ObjectAnimator build() {
        List<PropertyValuesHolder> propertyValues = new ArrayList<>();

        if (mRotate) {
            propertyValues.add(PropertyValuesHolder.ofFloat(View.ROTATION, mRotation));
        }

        if (mRotateInX) {
            propertyValues.add(PropertyValuesHolder.ofFloat(View.ROTATION_X, mRotationX));
        }

        if (mRotateInY) {
            propertyValues.add(PropertyValuesHolder.ofFloat(View.ROTATION_Y, mRotationY));
        }

        return ObjectAnimator.ofPropertyValuesHolder(
                mTarget,
                propertyValues.toArray(new PropertyValuesHolder[propertyValues.size()])
        );
    }
}
