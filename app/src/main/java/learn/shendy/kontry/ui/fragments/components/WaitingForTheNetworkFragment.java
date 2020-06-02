package learn.shendy.kontry.ui.fragments.components;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import learn.shendy.kontry.animators.NoInternetConnectionAnimator;
import learn.shendy.kontry.databinding.FragmentWaitingForTheNetworkBinding;
import learn.shendy.kontry.ui.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingForTheNetworkFragment extends BaseFragment {
    public static final String TAG = "NoInternetConnectionFra";

    private NoInternetConnectionAnimator mNoInternetConnectionAnimator;
    private FragmentWaitingForTheNetworkBinding mBinding;

    // MARK: Constructor Methods

    public WaitingForTheNetworkFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentWaitingForTheNetworkBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFragment();
        setupObservers();
    }

    // MARK: Setup Methods

    private void setupFragment() {
        mNoInternetConnectionAnimator = new NoInternetConnectionAnimator(mBinding.getRoot());
    }

    private void setupObservers() {
        mStore.internet
                .networkOfflineStatusObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(__ -> mNoInternetConnectionAnimator.show())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe();

        mStore.internet
                .networkOnlineStatusObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(__ -> mNoInternetConnectionAnimator.hide())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe();
    }
}
