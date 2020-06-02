package learn.shendy.kontry.repository.local.base;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import io.reactivex.Completable;

@Dao
public abstract class BaseDAO<T extends BaseEntity> {

    @Insert
    public abstract Completable insertOne(T entity);

    @Insert
    public abstract void insertOneSync(T entity);

    @Update
    public abstract Completable updateOne(T entity);

    @Update
    public abstract void updateOneSync(T entity);

    @Delete
    public abstract Completable deleteOne(T entity);
}
