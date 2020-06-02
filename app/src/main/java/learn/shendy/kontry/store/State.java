package learn.shendy.kontry.store;

import java.util.List;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import learn.shendy.kontry.enums.BOTTOM_NAV_EVENT;
import learn.shendy.kontry.enums.NETWORK_STATUS;
import learn.shendy.kontry.repository.local.search_keywords.SearchKeyword;
import learn.shendy.kontry.repository.model.country.CountryCell;

public class State {
    private static final String TAG = "State";

    // MARK: Properties

//    List<CountryCell> mCountryCells;
//    List<String> mTopSearchKeywords;

    // Data Status Subjects
    final BehaviorSubject<List<CountryCell>> mCountryCellsStatus = BehaviorSubject.create();
    final BehaviorSubject<List<SearchKeyword>> mTopSearchKeywordsStatus = BehaviorSubject.create();

    // Components Status Subjects
    final PublishSubject<Boolean> mContentLoaderStatus = PublishSubject.create();
    final PublishSubject<Boolean> mWatermarkStatus = PublishSubject.create();
    final PublishSubject<Boolean> mBottomNavStatus = PublishSubject.create();
    final PublishSubject<Boolean> mPleaseWaitStatus = PublishSubject.create();

    // Network/Internet Status
    final PublishSubject<NETWORK_STATUS> mNetworkStatus = PublishSubject.create();

    // Bottom Nav Events
    final PublishSubject<BOTTOM_NAV_EVENT> mBottomNavEvents = PublishSubject.create();

    // MARK: Constructor Methods

    public State() { }
}
