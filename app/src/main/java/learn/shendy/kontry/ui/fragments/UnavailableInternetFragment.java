package learn.shendy.kontry.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import learn.shendy.kontry.databinding.FragmentUnavailableInternetBinding;
import learn.shendy.kontry.enums.NETWORK_STATUS;
import learn.shendy.kontry.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class UnavailableInternetFragment extends BaseFragment {
    public static final String TAG = "UnavailableInternetFrag";

    private FragmentUnavailableInternetBinding mBinding;

    // MARK: Constructor Methods

    public UnavailableInternetFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentUnavailableInternetBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFragment(view, savedInstanceState);
        setupOnBackPressedCallbacks();
        setupObservers();
    }

    // MARK: Setup Methods

    private void setupFragment(@NonNull View view, @Nullable Bundle savedInstanceState) {
        hideActionBar();

        mBinding.uiRetryBtn.setOnClickListener(this::onRetry);

        mStore.actions.hideContentLoader();
        mStore.actions.hideWatermark();
        mStore.actions.hideBottomNav();
    }

    private void setupOnBackPressedCallbacks() {
        addOnBackPressedCallback(onBackPressedFragmentCallback);
    }

    private OnBackPressedCallback onBackPressedFragmentCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (mStore.internet.isOffline()) {
                requireActivity().finish();
            } else {
                setEnabled(false);
                navigateBack();
            }
        }
    };

    @SuppressLint("CheckResult")
    private void setupObservers() {
        mStore.internet
                .networkOnlineStatusObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(status -> Log.d(TAG, "setupObservers: " + status))
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe(
                        this::onInternetBackOnline,
                        ToastUtil::showUnhandledError
                );
    }

    private void onRetry(View btn) {
        Completable
                .fromAction(() -> mBinding.setDoesRetry(true))
                .delay(1000, TimeUnit.MILLISECONDS)
                .andThen(Observable.fromCallable(() -> mStore.internet.isOnline()))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(isOnline -> { if (isOnline) navigateBack(); })
                .doOnNext(__ -> mBinding.setDoesRetry(false))
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe();
    }

    private void onInternetBackOnline(NETWORK_STATUS status) {
        Completable
                .fromAction(() -> mBinding.setDoesRetry(true))
                .delay(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(this::navigateBack)
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe();
    }
}
