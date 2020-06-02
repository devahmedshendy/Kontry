package learn.shendy.kontry.store;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import learn.shendy.kontry.enums.BOTTOM_NAV_EVENT;
import learn.shendy.kontry.repository.Repository;
import learn.shendy.kontry.repository.local.search_keywords.SearchKeyword;
import learn.shendy.kontry.repository.model.country.Country;
import learn.shendy.kontry.repository.model.country.CountryCell;

public class Actions {
    private static final String TAG = "Actions";

    // MARK: Properties

    private final Repository mRepository;

    private final State state;
    private final Mutations mutations;

    private Scheduler mSingleScheduler = Schedulers.single();
    private Scheduler mIOScheduler = Schedulers.io();
    private Scheduler mComputationScheduler = Schedulers.computation();

    // MARK: Constructor Methods

    public Actions(State state, Mutations mutations, Repository repository) {
        this.state = state;
        this.mutations = mutations;
        this.mRepository = repository;
    }

    // MARK: State Observable Actions

    public Observable<Boolean> contentLoaderShowStateObservable() {
        return state.mContentLoaderStatus
                .subscribeOn(mSingleScheduler);
    }

    public Observable<Boolean> watermarkStateObservable() {
        return state.mWatermarkStatus
                .subscribeOn(mSingleScheduler);
    }

    public Observable<Boolean> bottomNavStateObservable() {
        return state.mBottomNavStatus
                .subscribeOn(mSingleScheduler);
    }

    public Observable<List<SearchKeyword>> topSearchKeywordsObservable() {
        return state
                .mTopSearchKeywordsStatus
                .subscribeOn(mComputationScheduler);
    }

    public Observable<List<CountryCell>> countryCellsObservable() {
        return state
                .mCountryCellsStatus
                .subscribeOn(mComputationScheduler);
    }

    public Observable<BOTTOM_NAV_EVENT> bottomNavEventsObservable() {
        return state
                .mBottomNavEvents
                .subscribeOn(mComputationScheduler);
    }

    public Observable<Boolean> pleaseWaitObservable() {
        return state
                .mPleaseWaitStatus
                .subscribeOn(mComputationScheduler);
    }

    // MARK: State Mutation Actions

    public void showWatermark() {
        mutations.EMIT_WATERMARK_STATUS(true);
    }

    public void hideWatermark() {
        mutations.EMIT_WATERMARK_STATUS(false);
    }

    public void showBottomNav() {
        mutations.EMIT_BOTTOM_NAV_STATUS(true);
    }

    public void hideBottomNav() {
        mutations.EMIT_BOTTOM_NAV_STATUS(false);
    }

    public void showContentLoader() {
        mutations.EMIT_CONTENT_LOADER_STATUS(true);
    }

    public void hideContentLoader() {
        mutations.EMIT_CONTENT_LOADER_STATUS(false);
    }

    public void showPleaseWait() {
        mutations.EMIT_PLEASE_WAIT_STATUS(true);
    }

    public void hidePleaseWait() {
        mutations.EMIT_PLEASE_WAIT_STATUS(false);
    }

    // MARK: Other Actions

    public Completable initState() {
        return Completable
                .fromSingle(findTop5UsedSearchKeywords())
                .andThen(findAllCountryCells())
                .ignoreElements();
    }

    public Completable cleanSearchKeywords() {
        return mRepository
                .cleanSearchKeywords();

    }

    public Single<List<SearchKeyword>> findTop5UsedSearchKeywords() {
        return mRepository
                .findTop5UsedSearchKeywords()
                .doOnSuccess(mutations::EMIT_TOP_5_SEARCH_KEYWORDS);
    }

    public Completable deleteTopSearchKeyword(SearchKeyword keyword) {
        return mRepository
                .deleteSearchKeyword(keyword)
                .andThen(Single.defer(this::findTop5UsedSearchKeywords))
                .ignoreElement();
    }

    public void updateCountries(List<CountryCell> countries) {
        mutations.EMIT_COUNTRY_CELLS(countries);
    }

    public Observable<List<CountryCell>> findAllCountryCells() {
        return Completable
                .fromAction(this::showContentLoader)
                .andThen(Completable.fromAction(mutations::CLEAR_COUNTRY_CELLS))
                .andThen(Observable.defer(mRepository::findAllCountryCells))
                .doOnNext(mutations::EMIT_COUNTRY_CELLS)
                .doOnNext(__ -> this.hideContentLoader())
                .subscribeOn(mIOScheduler);
    }

    public Maybe<Country> findCountryByFullName(String name) {
        return Completable
                .fromAction(this::showPleaseWait)
                .andThen(Maybe.defer(() -> mRepository.findCountryByFullName(name)))
                .doOnSuccess(__ -> hidePleaseWait())
                .doOnError(__ -> hidePleaseWait())
                .subscribeOn(mIOScheduler);
    }

    @SuppressLint("CheckResult")
    public Observable<List<CountryCell>> searchCountries(String keyword) {
        return Completable
                .fromAction(this::showContentLoader)
                .andThen(Observable.defer(() -> mRepository.searchCountries(keyword)))
                .doOnNext(__ -> mRepository.upsertSearchKeyword(new SearchKeyword(keyword)).subscribe())
                .doOnNext(__ -> this.hideContentLoader())
                .doOnError(__ -> this.hideContentLoader())
                .subscribeOn(mIOScheduler);
    }

    // MARK: Data Provider Methods

    private List<String> buildFakeTopKeywords() {
        List<String> list = new ArrayList<>();

        list.add("united");
        list.add("canada");
        list.add("egy");
        list.add("indones");

        return list;
    }

    private List<CountryCell> buildFakeCountries() {
        List<CountryCell> list = new ArrayList<>();

        CountryCell c = CountryCell.Builder()
                .setName("Chile")
                .setFlagURL("https://restcountries.eu/data/chl.svg")
                .setCapital("Santiago")
                .setPopulation(18191900)
                .build();

        list.add(c);

        c = CountryCell.Builder()
                .setName("Algeria")
                .setFlagURL("https://restcountries.eu/data/dza.svg")
                .setCapital("Algiers")
                .setPopulation(40400000)
                .build();

        list.add(c);

        c = CountryCell.Builder()
                .setName("Armenia")
                .setFlagURL("https://restcountries.eu/data/arm.svg")
                .setCapital("Yerevan")
                .setPopulation(2994400)
                .build();

        list.add(c);

        c = CountryCell.Builder()
                .setName("Bahrain")
                .setFlagURL("https://restcountries.eu/data/bhr.svg")
                .setCapital("Manama")
                .setPopulation(1404900)
                .build();

        list.add(c);

        return list;
    }
}
