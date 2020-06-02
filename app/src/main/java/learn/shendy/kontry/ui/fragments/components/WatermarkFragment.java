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
import androidx.navigation.NavController;

import io.reactivex.android.schedulers.AndroidSchedulers;
import learn.shendy.kontry.databinding.FragmentWatermarkBinding;
import learn.shendy.kontry.store.Store;
import learn.shendy.kontry.ui.fragments.BaseFragment;
import learn.shendy.kontry.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class WatermarkFragment extends BaseFragment {
    public static final String TAG = "WatermarkFragment";

    private Store mStore;

    private NavController mNavController;
    private FragmentWatermarkBinding mBinding;

    // MARK: Constructor Methods

    public WatermarkFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentWatermarkBinding.inflate(inflater, container, false);
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
    }

    @SuppressLint("CheckResult")
    private void setupObservers() {
        mStore.actions
                .watermarkStateObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe(
                        mBinding::setShowWatermark,
                        ToastUtil::showUnhandledError
                );
    }
}
