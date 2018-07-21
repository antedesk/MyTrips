package it.antedesk.mytrips.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.CheckIn;

public interface CheckInDao {

    @Query("SELECT * FROM check_ins")
    LiveData<List<CheckIn>> loadAllCheckIns();

    @Query("SELECT COUNT(id) FROM check_ins")
    int getTotalCheckIn();

    @Insert
    void insertNote(CheckIn checkIn);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(CheckIn checkIn);

    @Delete
    void deleteNote(CheckIn checkIn);
}
