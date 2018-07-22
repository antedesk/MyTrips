package it.antedesk.mytrips.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import it.antedesk.mytrips.model.CheckIn;

@Dao
public interface CheckInDao  extends BaseDao<CheckIn>{

    @Query("SELECT * FROM check_ins WHERE id=:id")
    LiveData<CheckIn> retrieveCheckInById(int id);

    @Query("SELECT  COUNT(DISTINCT address) FROM check_ins")
    int getTotalCheckIn();

    @Query("SELECT  COUNT(DISTINCT city) FROM check_ins")
    int getTotalCities();

    @Query("SELECT COUNT(DISTINCT country) FROM check_ins")
    int getTotalCountries();

    @Query("SELECT DISTINCT country FROM check_ins")
    LiveData<List<String>> getCountries();

}
