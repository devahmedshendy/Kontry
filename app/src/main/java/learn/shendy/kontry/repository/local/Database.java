package learn.shendy.kontry.repository.local;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import learn.shendy.kontry.repository.local.search_keywords.SearchKeyword;
import learn.shendy.kontry.repository.local.search_keywords.SearchKeywordDAO;

@androidx.room.Database(entities = {SearchKeyword.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static final String TAG = "Database";

    // MARK: DAOs

    public abstract SearchKeywordDAO mSearchKeywordDAO();

    // MARK: Singleton Definition

    private static Database INSTANCE;

    public static Database singleton(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room
                    .databaseBuilder(context, Database.class, "db")
                    .build();
        }

        return INSTANCE;
    }
}
