package learn.shendy.kontry.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import learn.shendy.kontry.R;
import learn.shendy.kontry.animators.SplashScreenAnimator;
import learn.shendy.kontry.databinding.FragmentSplashScreenStartBinding;
import learn.shendy.kontry.enums.NETWORK_STATUS;
import learn.shendy.kontry.store.Store;
import learn.shendy.kontry.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreenFragment extends BaseFragment {
    public static final String TAG = "SplashScreenFragment";

    private Store mStore;
    private NavController mNavController;
    private FragmentSplashScreenStartBinding mBinding;
    private SplashScreenAnimator mSplashScreenAnimator;

    // MARK: Constructor Methods

    public SplashScreenFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentSplashScreenStartBinding
                .inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFragment(view, savedInstanceState);
        startAnimator();
    }

    @Override
    public void onResume() {
        super.onResume();
        setupAnimatorObserver();
    }

    // MARK: Setup Methods

    private void setupFragment(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNavController = Navigation.findNavController(view);
        mStore = new ViewModelProvider(this).get(Store.class);

        hideActionBar();

        mSplashScreenAnimator = new SplashScreenAnimator(
                requireContext(),
                mBinding.splashScreenLayout,
                mBinding.splashScreenLogoIv,
                mBinding.splashScreenAppNameTv,
                mBinding.splashScreenAppSloganTv
        );

        mStore.actions
                .cleanSearchKeywords()
                .subscribe();
    }

    @SuppressLint("CheckResult")
    private void makeSureInternetIsOnline() {
        mStore.internet
                .networkStatusObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe(
                        this::onNetworkStatus,
                        ToastUtil::showUnhandledError
                );
    }

    private void startAnimator() {
        Completable
                .timer(1500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(mSplashScreenAnimator::start)
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe();
    }

    private void setupAnimatorObserver() {
        mSplashScreenAnimator
                .observeAnimatorState()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(__ -> onNetworkStatus(NETWORK_STATUS.ONLINE))
//                .doOnNext(__ -> makeSureInternetIsOnline())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe();
    }

    private void onNetworkStatus(NETWORK_STATUS status) {
        int action = status == NETWORK_STATUS.OFFLINE ?
                    R.id.global_action_to_noInternetFragment :
                    R.id.action_splashScreenFragment_to_countriesFragment;

        mNavController.navigate(action);
    }
}
