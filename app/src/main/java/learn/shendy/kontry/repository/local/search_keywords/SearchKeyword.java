package learn.shendy.kontry.repository.local.search_keywords;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

import learn.shendy.kontry.repository.local.base.BaseEntity;

@Entity(
        tableName = "search_keywords",
        indices = {@Index(value = {"name"}, unique = true)}
)
public class SearchKeyword extends BaseEntity {
    private static final String TAG = "SearchKeyword";

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "used_times")
    private Integer mUsedTimes = 1;

    public SearchKeyword() { }

    @Ignore
    public SearchKeyword(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Integer getUsedTimes() {
        return mUsedTimes;
    }

    public void setUsedTimes(Integer usedTimes) {
        mUsedTimes = usedTimes;
    }

    public void incrementUsedTimes() {
        mUsedTimes++;
    }

    @Override
    public String toString() {
        return "SearchKeyword { " +
                "mName = '" + mName + '\'' +
                ", mUsedTimes = " + mUsedTimes +
                " } ";
    }
}
