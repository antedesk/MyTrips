package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.model.Note;

public class NoteRepository {

    private static final String LOG_TAG = NoteRepository.class.getSimpleName();

    private LiveData<List<Note>> diaries;

    private static NoteRepository sInstance;
    private final AppDatabase mDb;

    private NoteRepository(final AppDatabase database) {
        mDb = database;
    }

    public static NoteRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (NoteRepository.class) {
                if (sInstance == null) {
                    sInstance = new NoteRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Note>> getDiaryNotes(int diaryId) {
        diaries = mDb.getNoteDao().loadNotesByDiaryId(diaryId);
        return diaries;
    }

    public LiveData<Note> geteNoteById(int diaryId) {
        return mDb.getNoteDao().retrieveNoteById(diaryId);
    }

    public LiveData<List<Note>> retrieveNotesByDiaryId(int diaryId) {
        return mDb.getNoteDao().retrieveNotesByDiaryId(diaryId);
    }

    public int getTotalCheckinsByDiaryId(int diaryId) {
        return mDb.getNoteDao().getTotalCheckinsByDiaryId(diaryId);
    }

    public double getTotalBudgetByDiaryId(int diaryId) {
        return mDb.getNoteDao().getTotalBudgetByDiaryId(diaryId);
    }

    public double getTotalBudget4AllNotes() {
        return mDb.getNoteDao().getTotalBudget4AllNotes();
    }

    public double getTotalBudgetByCategories() {
        return mDb.getNoteDao().getTotalBudgetByCategories();
    }

    public double getTotalBudgetByCategoriesAndDiaryId(int diaryId) {
        return mDb.getNoteDao().getTotalBudgetByCategoriesAndDiaryId(diaryId);
    }

    public LiveData<CheckIn> retrieveCheckInById(int checkInId) {
        return mDb.getNoteDao().retrieveCheckInById(checkInId);
    }

    public void insertNote (Note note) {
        mDb.getNoteDao().insert(note);
    }

    public void updateNote (Note note) {
        mDb.getNoteDao().update(note);
    }

    public void deleteNote (Note note) {
        mDb.getNoteDao().delete(note);
    }

}
