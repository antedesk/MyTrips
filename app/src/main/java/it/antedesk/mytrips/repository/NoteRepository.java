package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.model.minimal.CheckinMinimal;

public class NoteRepository {

    private static final String LOG_TAG = NoteRepository.class.getSimpleName();

    private static NoteRepository sInstance;
    private final AppDatabase mDb;
    private final AppExecutors appExecutors;

    private NoteRepository(final AppDatabase database, final AppExecutors appExecutors) {
        mDb = database;
        this.appExecutors = appExecutors;
    }

    public static NoteRepository getInstance(final AppDatabase database, final AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (NoteRepository.class) {
                if (sInstance == null) {
                    sInstance = new NoteRepository(database, appExecutors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Note>> getNotes() {
        MutableLiveData<List<Note>> notes = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> notes.postValue(mDb.getNoteDao().loadNotes()));
        return notes;
    }

    public LiveData<List<Note>> getDiaryNotes(long diaryId) {
        MutableLiveData<List<Note>> notes = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> notes.postValue(mDb.getNoteDao().loadNotesByDiaryId(diaryId)));
        return notes;
    }

    public LiveData<Note> geteNoteById(long diaryId) {
        MutableLiveData<Note> note = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> note.postValue(mDb.getNoteDao().retrieveNoteById(diaryId)));
        return note;
    }

    public LiveData<List<Note>> retrieveNotesByDiaryId(long diaryId) {
        MutableLiveData<List<Note>> notes = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> notes.postValue(mDb.getNoteDao().retrieveNotesByDiaryId(diaryId)));
        return notes;
    }

    public LiveData<List<CheckinMinimal>> getCheckinsByDiaryId(long diaryId) {
        MutableLiveData<List<CheckinMinimal>> checkIns = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> checkIns.postValue(mDb.getNoteDao().getCheckinsByDiaryId(diaryId)));
        return checkIns;
    }

    public LiveData<Double> getTotalBudgetByDiaryId(long diaryId) {
        MutableLiveData<Double> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getNoteDao().getTotalBudgetByDiaryId(diaryId)));
        return total;
    }

    public LiveData<Double> getTotalBudget4AllNotes() {
        MutableLiveData<Double> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getNoteDao().getTotalBudget4AllNotes()));
        return total;
    }

    public LiveData<Double> getTotalBudgetByCategories() {
        MutableLiveData<Double> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getNoteDao().getTotalBudgetByCategories()));
        return total;
    }

    public LiveData<Double> getTotalBudgetByCategoriesAndDiaryId(long diaryId) {
        MutableLiveData<Double> total = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> total.postValue(mDb.getNoteDao().getTotalBudgetByCategoriesAndDiaryId(diaryId)));
        return total;
    }

    public void insertNote (Note note) {
        appExecutors.diskIO().execute(() -> mDb.getNoteDao().insert(note));
    }

    public void updateNote (Note note) {
        appExecutors.diskIO().execute(() -> mDb.getNoteDao().update(note));
    }

    public void deleteNote (Note note) {
        appExecutors.diskIO().execute(() -> mDb.getNoteDao().delete(note));
    }

}
