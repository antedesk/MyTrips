package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.CheckIn;

public class CheckInRepository {
    private static final String LOG_TAG = CheckInRepository.class.getSimpleName();

    private LiveData<List<CheckIn>> checkIns;

    private static CheckInRepository sInstance;
    private final AppDatabase mDb;
    private final AppExecutors appExecutors;

    private CheckInRepository(final AppDatabase database, final AppExecutors appExecutors) {
        mDb = database;
        this.appExecutors = appExecutors;
    }

    public static CheckInRepository getInstance(final AppDatabase database, final AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (CheckInRepository.class) {
                if (sInstance == null) {
                    sInstance = new CheckInRepository(database, appExecutors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<CheckIn>> getCheckIns() {
        MutableLiveData<List<CheckIn>> checkIns = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> checkIns.postValue(mDb.getCheckInDao().retrieveCheckIns()));
        return checkIns;
    }

    public LiveData<CheckIn> getCheckInById(long id) {
        MutableLiveData<CheckIn> checkIn = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> checkIn.postValue(mDb.getCheckInDao().retrieveCheckInById(id)));
        return checkIn;
    }

    public LiveData<Integer> getTotalCheckIns() {
        MutableLiveData<Integer> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getCheckInDao().getTotalCheckIn()));
        return total;
    }

    public LiveData<Integer> getTotalCities() {

        MutableLiveData<Integer> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getCheckInDao().getTotalCities()));
        return total;
    }

    public LiveData<Integer> getTotalCountries() {
        MutableLiveData<Integer> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getCheckInDao().getTotalCountries()));
        return total;
    }

    public LiveData<List<String>>  getVisitedCountries() {
        MutableLiveData<List<String>> countries = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> countries.postValue(mDb.getCheckInDao().getCountries()));
        return countries;
    }

    public LiveData<Long> insertCheckIn (CheckIn checkIn) {
        MutableLiveData<Long> id = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> id.postValue(mDb.getCheckInDao().insert(checkIn)));
        return id;
    }

    public void updateCheckIn (CheckIn checkIn) {
        appExecutors.diskIO().execute(() -> mDb.getCheckInDao().update(checkIn));
    }

    public void deleteCheckIn (CheckIn checkIn) {
        appExecutors.diskIO().execute(() -> mDb.getCheckInDao().delete(checkIn));
    }

}
