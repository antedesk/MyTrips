package it.antedesk.mytrips.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.Diary;

public interface DiaryDao {

    @Query("SELECT * FROM diaries ORDER BY start_date DESC")
    LiveData<List<Diary>> loadAllDiary();

    @Insert
    void insertDiary(Diary diary);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDiary(Diary diary);

    @Delete
    void deleteDiary(Diary diary);
}
