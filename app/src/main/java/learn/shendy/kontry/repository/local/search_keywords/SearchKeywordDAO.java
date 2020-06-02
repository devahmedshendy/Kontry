package learn.shendy.kontry.repository.local.search_keywords;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import learn.shendy.kontry.repository.local.base.BaseDAO;

@Dao
public abstract class SearchKeywordDAO extends BaseDAO<SearchKeyword> {
    private static final String TAG = "SearchKeywordDAO";

    // MARK: Find Queries

    @Query("SELECT * FROM search_keywords WHERE name LIKE :name LIMIT 1")
    public abstract SearchKeyword findOneByNameSync(String name);

    @Query("SELECT * FROM search_keywords ORDER BY used_times DESC LIMIT 5")
    public abstract Single<List<SearchKeyword>> findTop5UsedSearchKeywords();

    // MARK: Transaction Queries

    @Transaction
    public void upsert(SearchKeyword newKeyword) {
        SearchKeyword foundKeyword = findOneByNameSync(newKeyword.getName());

        if (foundKeyword != null) {
            foundKeyword.incrementUsedTimes();
            updateOneSync(foundKeyword);
        } else {
            insertOneSync(newKeyword);
        }

    }

    // MARK: Misc Queries

    @Query("DELETE FROM search_keywords " +
            "WHERE search_keywords.id NOT IN ( " +
                "SELECT id FROM search_keywords ORDER BY used_times DESC LIMIT 2 " +
            ")")
    public abstract Completable cleanSearchKeywords();

}
