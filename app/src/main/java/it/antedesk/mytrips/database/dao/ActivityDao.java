package it.antedesk.mytrips.database.dao;

import android.arch.lifecycle.LiveData;
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
public interface ActivityDao extends BaseDao<Activity> {

    @Query("SELECT * FROM activities WHERE id=:id")
    LiveData<Activity>retrieveActivityById(int id);

    @Query("SELECT * FROM activities WHERE diary_id=:diaryId")
    LiveData<List<Activity>> retrieveActivitiesByDiaryId(final int diaryId);

    @Query("SELECT * FROM check_ins WHERE id=:checkInId")
    LiveData<CheckIn> retrieveCheckInById(final int checkInId);

    @Query("SELECT * FROM activities WHERE diary_id=:id ORDER BY date_time DESC")
    LiveData<List<Activity>> loadActivitiesByDiaryId(int id);

    @Query("SELECT COUNT(DISTINCT address) " +
            "FROM activities LEFT JOIN check_ins on activities.check_in_id = check_ins.id " +
            "WHERE diary_id=:diaryId " +
            "ORDER BY date_time DESC")
    int getTotalCheckinsByDiaryId(int diaryId);

    @Query("SELECT SUM(budget) FROM activities WHERE diary_id=:diaryId")
    double getTotalBudgetByDiaryId(int diaryId);

    @Query("SELECT SUM(budget) FROM activities")
    double getTotalBudget4AllActivities();

    @Query("SELECT category, SUM(budget) FROM activities GROUP BY category")
    double getTotalBudgetByCategories();

    @Query("SELECT category, SUM(budget) FROM activities WHERE diary_id=:diaryId GROUP BY category")
    double getTotalBudgetByCategoriesAndDiaryId(int diaryId);
}
