package learn.shendy.kontry.animators.anims;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import learn.shendy.kontry.animators.interfaces.ObjectAnimatorBuild;

public class ScaleAnimator implements ObjectAnimatorBuild {

    private View mTarget;

    private boolean mScaleX = false;
    private float mFromX;
    private float mToX;

    private boolean mScaleY = false;
    private float mFromY;
    private float mToY;

    public static ScaleAnimator Builder(View target) {
        return new ScaleAnimator(target);
    }

    private ScaleAnimator(View target) {
        mTarget = target;
    }
    
    public ScaleAnimator fromX(float value) {
        mFromX = value;
        return this;
    }
    
    public ScaleAnimator fromY(float value) {
        mFromY = value;
        return this;
    }
    
    public ScaleAnimator toX(float value) {
        mToX = value;
        mScaleX = true;
        return this;
    }
    
    
    public ScaleAnimator toY(float value) {
        mToY = value;
        mScaleY = true;
        return this;
    }

    @Override
    public ObjectAnimator build() {
        List<PropertyValuesHolder> propertyValues = new ArrayList<>();

        if (mScaleX) {
            propertyValues.add(PropertyValuesHolder.ofFloat(View.SCALE_X, mFromX, mToX));
        }

        if (mScaleY) {
            propertyValues.add(PropertyValuesHolder.ofFloat(View.SCALE_Y, mFromY, mToY));
        }

        return ObjectAnimator.ofPropertyValuesHolder(
                mTarget,
                propertyValues.toArray(new PropertyValuesHolder[propertyValues.size()])
        );
    }
}
