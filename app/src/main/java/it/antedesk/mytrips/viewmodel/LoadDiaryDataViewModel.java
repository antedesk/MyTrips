package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.repository.NoteRepository;

public class LoadDiaryDataViewModel  extends AndroidViewModel {

    private AppDatabase database;
    private AppExecutors appExecutors;

    public LoadDiaryDataViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getsInstance(this.getApplication());
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<Note>> getDiaryNotes(int diaryId) {
        //return NoteRepository.getInstance(database, appExecutors).getNotes();
        return NoteRepository.getInstance(database, appExecutors).getDiaryNotes(diaryId);
    }

}
