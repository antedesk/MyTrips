package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.Diary;

public class DiaryRepository {

    private static final String LOG_TAG = DiaryRepository.class.getSimpleName();

    private LiveData<List<Diary>> diaries;

    private static DiaryRepository sInstance;
    private final AppDatabase mDb;

    private DiaryRepository(final AppDatabase database) {
        mDb = database;
    }

    public static DiaryRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DiaryRepository.class) {
                if (sInstance == null) {
                    sInstance = new DiaryRepository(database);
                }
            }
        }
        return sInstance;
    }

    LiveData<List<Diary>> getPlans() {
        diaries = mDb.getDiaryDao().loadAllDiaries(false);
        return diaries;
    }

    LiveData<List<Diary>> getDiaries() {
        diaries = mDb.getDiaryDao().loadAllDiaries(true);
        return diaries;
    }

    //HOW TO DO THIS?
    public void insertDiary (Diary word) {
        //new insertAsyncTask(mDiaryDao).execute(word);
    }


}
