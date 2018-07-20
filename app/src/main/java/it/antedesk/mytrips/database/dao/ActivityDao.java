package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.Activity;
import it.antedesk.mytrips.model.CheckIn;

@Dao
public interface ActivityDao {

    @Query("SELECT * FROM activities WHERE id=:id")
    Activity retrieveActivityById(int id);

    @Query("SELECT * FROM activities ORDER BY date_time DESC")
    List<Activity> loadAllActivities();

    @Query("SELECT * FROM activities WHERE diary_id=:diaryId")
    List<Activity> retrieveActivitiesByDiaryId(final int diaryId);

    @Query("SELECT * FROM check_ins WHERE id=:checkInId")
    CheckIn retrieveCheckInById(final int checkInId);

    @Insert
    void insertActivity(Activity activity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateActivity(Activity activity);

    @Delete
    void deleteActivity(Activity activity);
}
