package learn.shendy.kontry.repository.local.base;


import androidx.databinding.BaseObservable;
import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.Date;

import learn.shendy.kontry.utils.DateConverters;

public class BaseEntity extends BaseObservable implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @TypeConverters(DateConverters.class)
    @ColumnInfo(name = "created_at", defaultValue = "CURRENT_TIMESTAMP")
    private Date mCreatedAt = new Date();

    @TypeConverters(DateConverters.class)
    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    private Date mUpdatedAt = new Date();

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public Date getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        mCreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        mUpdatedAt = updatedAt;
    }
}
