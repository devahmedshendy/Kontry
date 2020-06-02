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
import androidx.navigation.NavController;
import androidx.navigation.NavController.OnDestinationChangedListener;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.SimpleItemAnimator;

import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import learn.shendy.kontry.R;
import learn.shendy.kontry.databinding.FragmentCountriesBinding;
import learn.shendy.kontry.databinding.ToolbarCountriesFragmentBinding;
import learn.shendy.kontry.repository.model.country.Country;
import learn.shendy.kontry.repository.model.country.CountryCell;
import learn.shendy.kontry.ui.adapters.CountryCellsAdapter;
import learn.shendy.kontry.ui.adapters.CountryCellsAdapter.CountryNameClickHandler;
import learn.shendy.kontry.utils.ToastUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountriesFragment extends BaseFragment implements CountryNameClickHandler {
    private static final String TAG = "CountriesFragment";

    private CountryCellsAdapter mAdapter;

    private ToolbarCountriesFragmentBinding mToolbarBinding;
    private FragmentCountriesBinding mBinding;
    private NavController mNavController;

    // MARK: Constructor Methods

    public CountriesFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mToolbarBinding = ToolbarCountriesFragmentBinding.inflate(inflater, container, false);
        setCustomActionBarView(mToolbarBinding.getRoot());

        mBinding = FragmentCountriesBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (reactWhenFailedToReachInternet()) return;

        setupFragment(view, savedInstanceState);
        setupOnBackPressedCallbacks();
        setupObservers();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: starts");
        super.onPause();

        if (mNavController != null) {
            mNavController.removeOnDestinationChangedListener(onNavigationHappening);
        }
        Log.d(TAG, "onPause: ends");
    }

    // MARK: Setup Methods

    private void setupOnBackPressedCallbacks() {
        addOnBackPressedCallback(mOnBackPressedSearchCountriesFragmentCallback);
    }

    private OnBackPressedCallback mOnBackPressedSearchCountriesFragmentCallback = new OnBackPressedCallback(false) {
        @Override
        public void handleOnBackPressed() {
            initStoreState();
            setCustomActionBarView(mToolbarBinding.getRoot());
            finishSearchCountriesFragment();
        }
    };

    private void setupFragment(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNavController = Navigation.findNavController(view);

        mAdapter = new CountryCellsAdapter(requireContext(), this);

        mBinding.cCountriesRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.cCountriesRecycler.setAdapter(mAdapter);
        mBinding.cCountriesRecycler.setHasFixedSize(true);
        // Remove blinks
        ((SimpleItemAnimator) mBinding.cCountriesRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

        mToolbarBinding.toolbarCfSearchIcon.setOnClickListener(__ -> this.onSearchIconClick());

        mNavController.addOnDestinationChangedListener(onNavigationHappening);

        mStore.actions.hideBottomNav();
        mStore.actions.showWatermark();
        initStoreState();
    }

    @SuppressLint("CheckResult")
    private void initStoreState() {
        mStore.actions
                .initState()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe(
                        () -> {},
                        this::onInitStoreStateFailed
                );
    }

    private void setupObservers() {
        observeCountries();
    }

    @SuppressLint("CheckResult")
    private void observeCountries() {
        mStore.actions
                .countryCellsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mFragmentDisposables::add)
                .subscribe(
                        this::updateCountriesRecycler,
                        this::onCountriesObserverFailed
                );
    }

    private OnDestinationChangedListener onNavigationHappening = (controller, destination, arguments) -> {
        if (
                "fragment_country_details".equalsIgnoreCase(destination.getLabel().toString()) ||
                "no_internet_fragment".equalsIgnoreCase(destination.getLabel().toString())
        ) {
            finishSearchCountriesFragment();
        }
    };

    private void onSearchIconClick() {
        mStore.actions.updateCountries(Collections.emptyList());
        mOnBackPressedSearchCountriesFragmentCallback.setEnabled(true);

        openFragment(
                R.id.search_countries_container,
                new SearchCountriesFragment(),
                SearchCountriesFragment.TAG,
                false
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCountryNameClick(String name) {
        mStore.actions
                .findCountryByFullName(name)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::onCountryDetailsFound,
                        this::onFindCountryFailed
                );
    }

    private void updateCountriesRecycler(List<CountryCell> list) {
        mAdapter.updateList(list);
    }

    private void onCountryDetailsFound(Country country) {
        NavDirections action = CountriesFragmentDirections
                .actionCountriesFragmentToCountryDetailsFragment(country);

        mNavController.navigate(action);
    }

    private void onCountriesObserverFailed(Throwable t) {
        if (reactWhenNoInternetThrowable(t)) return;

        ToastUtil.showShortError(R.string.unable_to_load_countries, t);
    }

    private void onFindCountryFailed(Throwable t) {
        if (reactWhenNoInternetThrowable(t)) return;

        ToastUtil.showShortError(R.string.unable_to_load_country_details, t);
    }

    private void onInitStoreStateFailed(Throwable t) {
        if (reactWhenNoInternetThrowable(t)) return;

        ToastUtil.showShortError(R.string.unable_to_load_countries, t);
    }

    @SuppressLint("CheckResult")
    private void finishSearchCountriesFragment() {
        mOnBackPressedSearchCountriesFragmentCallback.setEnabled(false);
        closeFragment(SearchCountriesFragment.TAG);
    }
}
