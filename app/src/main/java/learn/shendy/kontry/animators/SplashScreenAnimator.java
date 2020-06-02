package learn.shendy.kontry.animators;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.Transition.TransitionListener;
import androidx.transition.TransitionManager;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import learn.shendy.kontry.R;
import learn.shendy.kontry.animators.anims.BaseAnimator;
import learn.shendy.kontry.animators.anims.FadeAnimator;
import learn.shendy.kontry.animators.anims.ScaleAnimator;
import learn.shendy.kontry.animators.anims.SlideAnimator;
import learn.shendy.kontry.utils.ThemeUtils;

public class SplashScreenAnimator  extends BaseAnimator {
    private static final String TAG = "SplashScreenAnimator";

    private final BehaviorSubject<Completable> mState = BehaviorSubject.create();

    private static final int DURATION = 2000;
    private static final float ACTION_BAR_SIZE;

    private Context mContext;
    private ConstraintLayout mRootConstraintLayout;
    private ImageView mLogoIV;
    private TextView mAppNameTV;
    private TextView mAppSloganTV;

    private int mStartLayout = R.layout.fragment_splash_screen_start;
    private int mEndLayout = R.layout.fragment_splash_screen_end;

    static {
        ACTION_BAR_SIZE = ThemeUtils.getActionBarSize();
    }

    // MARK: Constructor Methods

    public SplashScreenAnimator(
            Context context,
            ConstraintLayout rootConstraintLayout,
            ImageView logoIV,
            TextView appNameTV,
            TextView appSloganTV
    ) {
        mContext = context;
        mRootConstraintLayout = rootConstraintLayout;

        mLogoIV = logoIV;
        mAppNameTV = appNameTV;
        mAppSloganTV = appSloganTV;

        setCurrentAnimation(setupAnimator());
    }

    // MARK: Setup Methods

    private Transition setupConstraintsTransition() {
        ConstraintSet newConstraintSet = new ConstraintSet();

        newConstraintSet.clone(mContext, mEndLayout);
        newConstraintSet.applyTo(mRootConstraintLayout);

        return new ChangeBounds()
                .setDuration(DURATION)
                .addListener(constraintsTransitionListener)
                .setInterpolator(new FastOutSlowInInterpolator());

    }

    private AnimatorSet setupAnimator() {
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                setupLogoAnimator(),
                setupLogoNameAndSloganAnimator()
        );

        return animatorSet;
    }

    private Animator setupLogoAnimator() {
        Animator fadeOutAppName = FadeAnimator
                .Builder(mLogoIV)
                .from(1f).to(.1f)
                .build();

        fadeOutAppName.setStartDelay(1000);
        fadeOutAppName.setDuration(1000);
        return fadeOutAppName;
    }

    private AnimatorSet setupLogoNameAndSloganAnimator() {
        Animator scaleOutAppName = ScaleAnimator
                .Builder(mAppNameTV)
                .fromX(1f).toX(0f)
                .fromY(1f).toY(0f)
                .build();

        Animator slideUpAppName = SlideAnimator
                .YBuilder(mAppNameTV)
                .from(0f).to(-100f)
                .build();

        Animator scaleOutAppSlogan = ScaleAnimator
                .Builder(mAppSloganTV)
                .fromX(1f).toX(0f)
                .fromY(1f).toY(0f)
                .build();

        Animator slideUpAppSlogan = SlideAnimator
                .YBuilder(mAppSloganTV)
                .from(0f).to(-200f)
                .build();

        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.playTogether(
                scaleOutAppName,
                slideUpAppName,
                scaleOutAppSlogan,
                slideUpAppSlogan
        );

        animatorSet.setDuration(DURATION / 2);

        return animatorSet;
    }

    private TransitionListener constraintsTransitionListener = new TransitionListener() {
        @Override
        public void onTransitionStart(Transition transition) {
        }

        @Override
        public void onTransitionEnd(Transition transition) {
            mState.onNext(Completable.complete());
        }

        @Override
        public void onTransitionCancel(Transition transition) {
        }

        @Override
        public void onTransitionPause(Transition transition) {
        }

        @Override
        public void onTransitionResume(Transition transition) {
        }
    };

    @Override
    public void start() {
        TransitionManager.beginDelayedTransition(
                mRootConstraintLayout,
                setupConstraintsTransition()
        );

        super.start();
    }

    public Observable<Completable> observeAnimatorState() {
        return mState
                .subscribeOn(Schedulers.single());
    }
}
