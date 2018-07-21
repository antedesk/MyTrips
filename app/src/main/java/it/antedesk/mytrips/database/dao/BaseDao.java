package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import it.antedesk.mytrips.model.CheckIn;

public interface BaseDao<T> {

    @Insert
    void insertEntity(CheckIn checkIn);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEntity(CheckIn checkIn);

    @Delete
    void deleteEntity(CheckIn checkIn);
}
