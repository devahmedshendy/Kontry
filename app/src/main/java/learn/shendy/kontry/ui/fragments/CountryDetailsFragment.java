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

import io.reactivex.android.schedulers.AndroidSchedulers;
import learn.shendy.kontry.R;
import learn.shendy.kontry.databinding.FragmentCountryDetailsBinding;
import learn.shendy.kontry.databinding.ToolbarCountryDetailsFragmentBinding;
import learn.shendy.kontry.enums.BOTTOM_NAV_EVENT;
import learn.shendy.kontry.repository.model.country.Country;
import learn.shendy.kontry.store.Store;
import learn.shendy.kontry.utils.SVGUtil;
import learn.shendy.kontry.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class CountryDetailsFragment extends BaseFragment {
    private static final String TAG = "CountryDetailsFragment";

    private Store mStore;

    private Country mCountry;

    private NavController mNavController;
    private ToolbarCountryDetailsFragmentBinding mToolbarBinding;
    private FragmentCountryDetailsBinding mBinding;

    // MARK: Constructor Methods

    public CountryDetailsFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCountry = CountryDetailsFragmentArgs.fromBundle(getArguments()).getCountry();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mToolbarBinding = ToolbarCountryDetailsFragmentBinding.inflate(inflater, container, false);
        setCustomActionBarView(mToolbarBinding.getRoot());

        mBinding = FragmentCountryDetailsBinding.inflate(inflater, container, false);
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
        mNavController = Navigation.findNavController(view);
        mStore = new ViewModelProvider(requireActivity()).get(Store.class);

        mBinding.setCountry(mCountry);
        SVGUtil.fetchInto(requireContext(), mCountry.getFlagURL(), mBinding.ccCountryFlagIv);

        mStore.actions.hideWatermark();
        mStore.actions.showBottomNav();
    }

    @SuppressLint("CheckResult")
    private void setupObservers() {
        mStore.actions
                .bottomNavEventsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe(
                        this::onBottomNavEvent,
                        ToastUtil::showUnhandledError
                );
    }

    // MARK: Observer Handler Methods

    private void onBottomNavEvent(BOTTOM_NAV_EVENT event) {
        if (event == BOTTOM_NAV_EVENT.HOME) {
            mNavController.popBackStack(R.id.countriesFragment, false);
        }
    }
}
