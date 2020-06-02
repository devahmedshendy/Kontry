package learn.shendy.kontry.repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import learn.shendy.kontry.App;
import learn.shendy.kontry.repository.local.Database;
import learn.shendy.kontry.repository.local.search_keywords.SearchKeyword;
import learn.shendy.kontry.repository.local.search_keywords.SearchKeywordDAO;
import learn.shendy.kontry.repository.model.builders.CountryBuilder;
import learn.shendy.kontry.repository.model.builders.CountryCellBuilder;
import learn.shendy.kontry.repository.model.country.Country;
import learn.shendy.kontry.repository.model.country.CountryCell;
import learn.shendy.kontry.repository.remote.RestCountriesService;
import learn.shendy.kontry.repository.remote.RetrofitClient;

public class Repository {
    private static final String TAG = "Repository";

    private Scheduler mIOScheduler = Schedulers.io();

    private SearchKeywordDAO mSearchKeywordDAO;
    private RestCountriesService mRestCountriesService;

    public Repository() {
        mSearchKeywordDAO = Database.singleton(App.INSTANCE).mSearchKeywordDAO();
        mRestCountriesService = RetrofitClient.getRestCountriesService();
    }

    // MARK: REST Methods

    public Observable<List<CountryCell>> findAllCountryCells() {
        String fields = "name;flag;capital;population";
        return mRestCountriesService
                .getAll(fields)
                .concatMapSingle(list -> Observable
                        .fromIterable(list)
                        .map(CountryCellBuilder::fromCountryCellResponse)
                        .toList()
                )
                .subscribeOn(mIOScheduler);
    }

    public Maybe<Country> findCountryByFullName(String fullName) {
        String fields = "name;flag;capital;region;population;currencies;languages";

        return mRestCountriesService
                .getCountryByFullName(fullName, fields)
                .delay(1000, TimeUnit.MILLISECONDS)
                .map(response -> CountryBuilder.fromCountryResponse(response.get(0)))
                .subscribeOn(mIOScheduler);
    }

    public Observable<List<CountryCell>> searchCountries(String name) {
        String fields = "name;flag;capital;population";

        return mRestCountriesService
                .getCountryByName(name, fields)
                .concatMapSingle(list -> Observable
                        .fromIterable(list)
                        .map(CountryCellBuilder::fromCountryCellResponse)
                        .toList()
                )
                .subscribeOn(mIOScheduler);
    }

    // MARK: Room Database Methods

    public Completable cleanSearchKeywords() {
        return mSearchKeywordDAO
                .cleanSearchKeywords()
                .subscribeOn(mIOScheduler);
    }
    public Single<List<SearchKeyword>> findTop5UsedSearchKeywords() {
        return mSearchKeywordDAO
                .findTop5UsedSearchKeywords()
                .subscribeOn(mIOScheduler);
    }

    public Completable upsertSearchKeyword(SearchKeyword newKeyword) {
        return Completable
                .fromAction(() -> mSearchKeywordDAO.upsert(newKeyword))
                .subscribeOn(mIOScheduler);
    }

    public Completable deleteSearchKeyword(SearchKeyword keyword) {
        return mSearchKeywordDAO
                .deleteOne(keyword)
                .subscribeOn(mIOScheduler);
    }
}
