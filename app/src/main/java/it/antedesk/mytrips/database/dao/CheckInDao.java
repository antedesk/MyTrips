package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.CheckIn;

@Dao
public interface CheckInDao {

    @Query("SELECT * FROM check_ins")
    List<CheckIn> loadAllCheckIns();

    @Insert
    void insertNote(CheckIn checkIn);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(CheckIn checkIn);

    @Delete
    void deleteNote(CheckIn checkIn);
}
