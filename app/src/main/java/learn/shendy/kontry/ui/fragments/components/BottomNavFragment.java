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
import learn.shendy.kontry.animators.BottomNavAnimator;
import learn.shendy.kontry.databinding.FragmentBottomNavBinding;
import learn.shendy.kontry.enums.BOTTOM_NAV_EVENT;
import learn.shendy.kontry.store.Store;
import learn.shendy.kontry.ui.fragments.BaseFragment;
import learn.shendy.kontry.utils.ToastUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class BottomNavFragment extends BaseFragment {
    public static final String TAG = "BottomNavFragment";

    private ViewGroup mContainer;

    private FragmentBottomNavBinding mBinding;
    private BottomNavAnimator mBottomNavAnimator;

    private Store mStore;

    public BottomNavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContainer = container;
        mBinding = FragmentBottomNavBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFragment();
        setupObservers();
    }

    private void setupFragment() {
        mStore = new ViewModelProvider(requireActivity()).get(Store.class);

        mBottomNavAnimator = new BottomNavAnimator((ViewGroup) mBinding.getRoot().getParent(), mBinding.bottomNavHomeIv);

        mBinding.bottomNavHomeIv.setOnClickListener(__ ->
                mStore.mutations.EMIT_BOTTOM_NAV_EVENT(BOTTOM_NAV_EVENT.HOME));
    }

    @SuppressLint("CheckResult")
    private void setupObservers() {
        mStore.actions
                .bottomNavStateObservable()
                .skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe(
                        this::onStateChanged,
                        ToastUtil::showUnhandledError
                );
    }

    // MARK: Observer Handler Methods

    private void onStateChanged(boolean enter) {
        if (enter) {
            mBottomNavAnimator.enter();
        } else {
            mBottomNavAnimator.leave();
        }
    }
}
