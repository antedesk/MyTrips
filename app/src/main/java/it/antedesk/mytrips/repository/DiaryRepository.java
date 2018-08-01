package it.antedesk.mytrips.repository;

import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Diary;

public class DiaryRepository {

    private static DiaryRepository sInstance;
    private final AppDatabase mDb;
    private final AppExecutors appExecutors;

    private DiaryRepository(final AppDatabase database, final AppExecutors appExecutors) {
        mDb = database;
        this.appExecutors = appExecutors;
    }

    public static DiaryRepository getInstance(final AppDatabase database, final AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (DiaryRepository.class) {
                if (sInstance == null) {
                    sInstance = new DiaryRepository(database, appExecutors);
                }
            }
        }
        return sInstance;
    }

    public MutableLiveData<Diary> getDiaryById(int id) {
        MutableLiveData<Diary> diary = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> diary.postValue(mDb.getDiaryDao().retrieveDiaryById(id)));
        return diary;
    }

    public MutableLiveData<List<Diary>> getPlans() {
        MutableLiveData<List<Diary>> diaries = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> diaries.postValue(mDb.getDiaryDao().loadAllDiaries(true)));
        return diaries;
    }
    public MutableLiveData<List<Diary>> getDiaries() {
        MutableLiveData<List<Diary>> diaries = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> diaries.postValue(mDb.getDiaryDao().loadAllDiaries(false)));
        return diaries;
    }

    public void insertDiary (Diary diary) {
        appExecutors.diskIO().execute(() -> mDb.getDiaryDao().insert(diary));
    }

    public void updateDiary (Diary diary) {
        appExecutors.diskIO().execute(() -> mDb.getDiaryDao().update(diary));
    }

    public void deleteDiary (Diary diary) {
        appExecutors.diskIO().execute(() -> mDb.getDiaryDao().delete(diary));
    }

}
