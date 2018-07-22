package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.CheckIn;

public class CheckInRepository {
    private static final String LOG_TAG = CheckInRepository.class.getSimpleName();

    private LiveData<List<CheckIn>> checkIns;

    private static CheckInRepository sInstance;
    private final AppDatabase mDb;

    private CheckInRepository(final AppDatabase database) {
        mDb = database;
    }

    public static CheckInRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (CheckInRepository.class) {
                if (sInstance == null) {
                    sInstance = new CheckInRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<CheckIn> getCheckInById(int id) {
        return mDb.getCheckInDao().retrieveCheckInById(id);
    }

    public int getTotalCheckIns() {
        return mDb.getCheckInDao().getTotalCheckIn();
    }

    public int getTotalCities() {
        return mDb.getCheckInDao().getTotalCities();
    }

    public int getTotalCountries() {
        return mDb.getCheckInDao().getTotalCountries();
    }

    public LiveData<List<String>>  getVisitedCountries() {
        return mDb.getCheckInDao().getCountries();
    }

    public void insertCheckIn (CheckIn checkIn) {
        mDb.getCheckInDao().insert(checkIn);
    }

    public void updateCheckIn (CheckIn checkIn) {
        mDb.getCheckInDao().update(checkIn);
    }

    public void deleteCheckIn (CheckIn checkIn) {
        mDb.getCheckInDao().delete(checkIn);
    }

}
