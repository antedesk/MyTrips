package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Activity;
import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.model.Diary;

public class ActivityRepository {

    private static final String LOG_TAG = ActivityRepository.class.getSimpleName();

    private static ActivityRepository sInstance;
    private final AppDatabase mDb;
    private final AppExecutors appExecutors;

    private ActivityRepository(final AppDatabase database, final AppExecutors appExecutors) {
        mDb = database;
        this.appExecutors = appExecutors;
    }

    public static ActivityRepository getInstance(final AppDatabase database, final AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (ActivityRepository.class) {
                if (sInstance == null) {
                    sInstance = new ActivityRepository(database, appExecutors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Activity>> getDiaryActivities(int diaryId) {
        MutableLiveData<List<Activity>> activities = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> activities.postValue(mDb.getActivityDao().loadActivitiesByDiaryId(diaryId)));
        return activities;
    }

    public LiveData<Activity> geteActivityById(int activityId) {
        MutableLiveData<Activity> activity = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> activity.postValue(mDb.getActivityDao().retrieveActivityById(activityId)));
        return activity;
    }

    public LiveData<List<Activity>> retrieveActivitiesByDiaryId(int diaryId) {
        MutableLiveData<List<Activity>> activities = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> activities.postValue(mDb.getActivityDao().retrieveActivitiesByDiaryId(diaryId)));
        return activities;
    }

    public LiveData<Integer> getTotalCheckinsByDiaryId(int diaryId) {
        MutableLiveData<Integer> totalCheckins = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> totalCheckins.postValue(mDb.getActivityDao().getTotalCheckinsByDiaryId(diaryId)));
        return totalCheckins;
    }

    public LiveData<Double> getTotalBudgetByDiaryId(int diaryId) {
        MutableLiveData<Double> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getActivityDao().getTotalBudgetByDiaryId(diaryId)));
        return total;
    }

    public LiveData<Double> getTotalBudget4AllActivities() {
        MutableLiveData<Double> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getActivityDao().getTotalBudget4AllActivities()));
        return total;
    }

    public LiveData<Double> getTotalBudgetByCategories() {
        MutableLiveData<Double> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getActivityDao().getTotalBudgetByCategories()));
        return total;
    }

    public LiveData<Double> getTotalBudgetByCategoriesAndDiaryId(int diaryId) {
        MutableLiveData<Double> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getActivityDao().getTotalBudgetByCategoriesAndDiaryId(diaryId)));
        return total;
    }

    public MutableLiveData<CheckIn> retrieveCheckInById(int checkInId) {
        MutableLiveData<CheckIn> checkIn = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> checkIn.postValue(mDb.getActivityDao().retrieveCheckInById(checkInId)));
        return checkIn;
    }

    public void insertActivity (Activity activity) {
        appExecutors.diskIO().execute(() -> mDb.getActivityDao().insert(activity));
    }

    public void updateActivity (Activity activity) {
        appExecutors.diskIO().execute(() -> mDb.getActivityDao().update(activity));
    }

    public void deleteActivity (Activity activity) {
        appExecutors.diskIO().execute(() ->
                mDb.getActivityDao().delete(activity)
        );
    }

}
