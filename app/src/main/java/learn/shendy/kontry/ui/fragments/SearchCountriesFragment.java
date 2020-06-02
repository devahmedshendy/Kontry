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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import learn.shendy.kontry.R;
import learn.shendy.kontry.databinding.FragmentSearchCountriesBinding;
import learn.shendy.kontry.databinding.ToolbarSearchCountriesFragmentBinding;
import learn.shendy.kontry.repository.local.search_keywords.SearchKeyword;
import learn.shendy.kontry.repository.model.country.CountryCell;
import learn.shendy.kontry.store.Store;
import learn.shendy.kontry.ui.adapters.TopSearchKeywordsAdapter;
import learn.shendy.kontry.ui.adapters.TopSearchKeywordsAdapter.TopSearchKeywordsHandler;
import learn.shendy.kontry.utils.ToastUtil;
import retrofit2.HttpException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchCountriesFragment extends BaseFragment implements TopSearchKeywordsHandler {
    public static final String TAG = "SearchCountriesFragment";

    private TopSearchKeywordsAdapter mAdapter;

    private ToolbarSearchCountriesFragmentBinding mToolbarBinding;
    private FragmentSearchCountriesBinding mBinding;

    private Store mStore;

    // MARK: Constructor Methods

    public SearchCountriesFragment() {
        // Required empty public constructor
    }

    // MARK: Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mToolbarBinding = ToolbarSearchCountriesFragmentBinding.inflate(inflater, container, false);

        setCustomActionBarView(mToolbarBinding.getRoot());

        mBinding = FragmentSearchCountriesBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupFragment(view, savedInstanceState);
        loadTopKeywords();
        setupObservers();
    }

    // MARK: Setup Methods

    private void setupFragment(View view, Bundle savedInstanceState) {
        mStore = new ViewModelProvider(requireActivity()).get(Store.class);

        openKeyboardFor(mToolbarBinding.scSearchCountryInput);

        mToolbarBinding.setIsEmptySearchInput(true);
        mBinding.setIsEmptySearchInput(true);

        mAdapter = new TopSearchKeywordsAdapter(requireContext(), this);
        mBinding.scTopSearchKeywordsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.scTopSearchKeywordsRecycler.setAdapter(mAdapter);

        mToolbarBinding.scCancelBtn.setOnClickListener(__ -> navigateBack());
        mToolbarBinding.scClearTextBtn.setOnClickListener(__ -> clearSearchInput());
    }

    @SuppressLint("CheckResult")
    private void loadTopKeywords() {
        mStore.actions
                .topSearchKeywordsObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        this::updateTopSearchKeywordsRecycler,
                        this::onTopSearchKeywordsObserverFailed
                );
    }

    private void setupObservers() {
        observeSearchInput();
    }

    @SuppressLint("CheckResult")
    private void observeSearchInput() {
        // Unfortunately. This doesn't work
        // This is a platform bug: https://github.com/JakeWharton/RxBinding/issues/192
//        Disposable a = RxTextView
//                .textChanges(mToolbarBinding.scSearchCountryInput)
//                .skipInitialValue()
//                .take(1)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(__ -> mBinding.scTopSearchKeywordsRecycler.setVisibility(View.GONE))
//                .subscribe(
//                        __ -> {},
//                        ToastUtil::showUnhandledError
//                );

        RxTextView
                .textChanges(mToolbarBinding.scSearchCountryInput)
                .skipInitialValue()
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(__ -> mBinding.scTopSearchKeywordsRecycler.setVisibility(View.GONE))
                .debounce(500, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .map(this::respondToEmptySearchInput)
                .filter(keyword -> !keyword.isEmpty())
                .doOnNext(keyword -> mStore.actions
                        .searchCountries(keyword)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(this::saveDisposableOnSubscribe)
                        .subscribe(
                                this::onSearchCountriesResult,
                                this::onSearchCountriesFailed
                        ))
                .doOnSubscribe(this::saveDisposableOnSubscribe)
                .subscribe();
    }

    @Override
    public void onTopSearchKeywordSelect(SearchKeyword keyword) {
        mToolbarBinding.scSearchCountryInput.setText(keyword.getName());
        mToolbarBinding.scSearchCountryInput.setSelection(keyword.getName().length());
    }

    @Override
    public void onTopSearchKeywordDelete(SearchKeyword keyword) {
        mStore.actions
                .deleteTopSearchKeyword(keyword)
                .subscribe();
    }

    // MARK: Observer Listeners

    private String respondToEmptySearchInput(String keyword) {
        mToolbarBinding.setIsEmptySearchInput(keyword.isEmpty());
        mBinding.setIsEmptySearchInput(keyword.isEmpty());
        if (keyword.isEmpty()) mStore.mutations.CLEAR_COUNTRY_CELLS();

        return keyword;
    }

    private void onSearchCountriesResult(List<CountryCell> result) {
        mStore.mutations.EMIT_COUNTRY_CELLS(result);
        mBinding.scSearchResultCountTv.setText(String.valueOf(result.size()));
    }

    private void onSearchCountriesFailed(Throwable t) {
        if (reactWhenNoInternetThrowable(t)) return;

        if (t instanceof HttpException) {
            HttpException e = (HttpException) t;
            if (e.code() == 404) {
                onSearchCountriesResult(Collections.emptyList());
                return;
            }
        }

        ToastUtil.showShortError(R.string.unable_to_search_countries, t);
    }

    private void updateTopSearchKeywordsRecycler(List<SearchKeyword> keywords) {
        if (keywords.isEmpty()) mBinding.scTopSearchKeywordsRecycler.setVisibility(View.GONE);
        mAdapter.update(keywords);
    }

    private void onTopSearchKeywordsObserverFailed(Throwable t) {
        ToastUtil.showShortError(R.string.unable_to_load_top_search_keywords, t);
    }

    // MARK: OnClick Listener

    private void clearSearchInput() {
        mToolbarBinding.scSearchCountryInput.setText("");
        mToolbarBinding.scClearTextBtn.setVisibility(View.GONE);
    }
}
