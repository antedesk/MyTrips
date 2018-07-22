package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.Activity;
import it.antedesk.mytrips.model.CheckIn;

public class ActivityRepository {

    private static final String LOG_TAG = ActivityRepository.class.getSimpleName();

    private LiveData<List<Activity>> diaries;

    private static ActivityRepository sInstance;
    private final AppDatabase mDb;

    private ActivityRepository(final AppDatabase database) {
        mDb = database;
    }

    public static ActivityRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (ActivityRepository.class) {
                if (sInstance == null) {
                    sInstance = new ActivityRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Activity>> getDiaryActivities(int diaryId) {
        diaries = mDb.getActivityDao().loadActivitiesByDiaryId(diaryId);
        return diaries;
    }

    public LiveData<Activity> geteActivityById(int diaryId) {
        return mDb.getActivityDao().retrieveActivityById(diaryId);
    }

    public LiveData<List<Activity>> retrieveActivitiesByDiaryId(int diaryId) {
        return mDb.getActivityDao().retrieveActivitiesByDiaryId(diaryId);
    }

    public int getTotalCheckinsByDiaryId(int diaryId) {
        return mDb.getActivityDao().getTotalCheckinsByDiaryId(diaryId);
    }

    public double getTotalBudgetByDiaryId(int diaryId) {
        return mDb.getActivityDao().getTotalBudgetByDiaryId(diaryId);
    }

    public double getTotalBudget4AllActivities() {
        return mDb.getActivityDao().getTotalBudget4AllActivities();
    }

    public double getTotalBudgetByCategories() {
        return mDb.getActivityDao().getTotalBudgetByCategories();
    }

    public double getTotalBudgetByCategoriesAndDiaryId(int diaryId) {
        return mDb.getActivityDao().getTotalBudgetByCategoriesAndDiaryId(diaryId);
    }

    public LiveData<CheckIn> retrieveCheckInById(int checkInId) {
        return mDb.getActivityDao().retrieveCheckInById(checkInId);
    }

    public void insertActivity (Activity activity) {
        mDb.getActivityDao().insert(activity);
    }

    public void updateActivity (Activity activity) {
        mDb.getActivityDao().update(activity);
    }

    public void deleteActivity (Activity activity) {
        mDb.getActivityDao().delete(activity);
    }

}
