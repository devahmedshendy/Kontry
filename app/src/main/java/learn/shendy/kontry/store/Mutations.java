package learn.shendy.kontry.store;

import java.util.Collections;
import java.util.List;

import learn.shendy.kontry.enums.BOTTOM_NAV_EVENT;
import learn.shendy.kontry.enums.NETWORK_STATUS;
import learn.shendy.kontry.repository.local.search_keywords.SearchKeyword;
import learn.shendy.kontry.repository.model.country.CountryCell;

public class Mutations {
    private static final String TAG = "Mutations";

    // MARK: Properties

    private final State state;

    // MARK: Constructor Methods

    public Mutations(State state) {
        this.state = state;
    }

    // MARK: Component Mutations

    public void EMIT_WATERMARK_STATUS(boolean show) {
        state.mWatermarkStatus.onNext(show);
    }

    public void EMIT_BOTTOM_NAV_STATUS(boolean show) {
        state.mBottomNavStatus.onNext(show);
    }

    public void EMIT_CONTENT_LOADER_STATUS(boolean show) {
        state.mContentLoaderStatus.onNext(show);
    }

    public void EMIT_BOTTOM_NAV_EVENT(BOTTOM_NAV_EVENT event) {
        state.mBottomNavEvents.onNext(event);
    }

    public void EMIT_PLEASE_WAIT_STATUS(boolean show) {
        state.mPleaseWaitStatus.onNext(show);
    }

    public void EMIT_NETWORK_STATUS(NETWORK_STATUS status) {
        state.mNetworkStatus.onNext(status);
    }

    // MARK: Data Mutations

//    public void SET_COUNTRY_CELLS(List<CountryCell> list) {
//        state.mCountryCells = list;
//    }

    public void EMIT_COUNTRY_CELLS(List<CountryCell> list) {
        state.mCountryCellsStatus.onNext(list);
    }

    public void CLEAR_COUNTRY_CELLS() {
        EMIT_COUNTRY_CELLS(Collections.emptyList());
    }

//    public void SET_TOP_SEARCH_KEYWORDS(List<String> list) {
//        state.mTopSearchKeywords = list;
//    }

    public void EMIT_TOP_5_SEARCH_KEYWORDS(List<SearchKeyword> list) {
        state.mTopSearchKeywordsStatus.onNext(list);
    }

//    public void DELETE_TOP_SEARCH_KEYWORD(String keyword) {
//        state.mTopSearchKeywords.remove(keyword);
//    }
}
