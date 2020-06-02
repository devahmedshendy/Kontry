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
import learn.shendy.kontry.animators.ContentLoaderAnimator;
import learn.shendy.kontry.databinding.FragmentContentLoaderBinding;
import learn.shendy.kontry.store.Store;
import learn.shendy.kontry.ui.fragments.BaseFragment;
import learn.shendy.kontry.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContentLoaderFragment extends BaseFragment {
    public static final String TAG = "ContentLoaderFragment";

    private Store mStore;
    private FragmentContentLoaderBinding mBinding;
    private ContentLoaderAnimator mContentLoaderAnimator;

    // MARK: Constructor Methods

    public ContentLoaderFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentContentLoaderBinding.inflate(inflater, container, false);
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
        mStore = new ViewModelProvider(requireActivity()).get(Store.class);

        mContentLoaderAnimator = new ContentLoaderAnimator(
                mBinding.contentLoaderIv,
                mBinding.contentLoaderSpinner
        );
    }

    @SuppressLint("CheckResult")
    private void setupObservers() {
        mStore.actions
                .contentLoaderShowStateObservable()
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
            mContentLoaderAnimator.show();
        } else {
            mContentLoaderAnimator.hide();
        }
    }
}
