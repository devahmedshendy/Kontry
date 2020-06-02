package learn.shendy.kontry.ui.fragments.components;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import learn.shendy.kontry.animators.PleaseWaitAnimator;
import learn.shendy.kontry.databinding.FragmentPleaseWaitBinding;
import learn.shendy.kontry.store.Store;
import learn.shendy.kontry.ui.fragments.BaseFragment;
import learn.shendy.kontry.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PleaseWaitFragment extends BaseFragment {
    public static final String TAG = "PleaseWaitFragment";

    private Store mStore;
    private FragmentPleaseWaitBinding mBinding;
    private PleaseWaitAnimator mPleaseWaitAnimator;

    // MARK: Constructor Methods

    public PleaseWaitFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = FragmentPleaseWaitBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFragment(view, savedInstanceState);
        setupObservers();
    }

    // MARK: Setup Methods

    private void setupFragment(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mStore = new ViewModelProvider(requireActivity()).get(Store.class);

        mPleaseWaitAnimator = new PleaseWaitAnimator(mBinding.getRoot());
    }

    @SuppressLint("CheckResult")
    private void setupObservers() {
        mStore.actions
                .pleaseWaitObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe(
                        this::onStateChanged,
                        ToastUtil::showUnhandledError
                );
    }

    // MARK: Observer Listener Methods

    private void onStateChanged(boolean show) {
        if (show) {
            mPleaseWaitAnimator.show();
        } else {
            mPleaseWaitAnimator.hide();
        }
    }
}
