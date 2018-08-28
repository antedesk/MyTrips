package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.repository.NoteRepository;

public class LoadNoteViewModel extends AndroidViewModel {

    private AppDatabase database;
    private AppExecutors appExecutors;

    public LoadNoteViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getsInstance(this.getApplication());
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<Note> geteNoteById(long noteId) {
        return NoteRepository.getInstance(database, appExecutors).geteNoteById(noteId);
    }

}

