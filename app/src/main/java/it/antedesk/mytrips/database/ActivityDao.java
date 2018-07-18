package it.antedesk.mytrips.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.Activity;

public interface ActivityDao {

    @Query("SELECT * FROM activities ORDER BY date_time DESC")
    List<Activity> loadAllActivities();

    @Insert
    void insertActivity(Activity activity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateActivity(Activity activity);

    @Delete
    void deleteActivity(Activity activity);
}
