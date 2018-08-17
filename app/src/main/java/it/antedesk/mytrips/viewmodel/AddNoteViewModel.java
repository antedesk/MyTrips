package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import it.antedesk.mytrips.R;
import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.repository.CheckInRepository;
import it.antedesk.mytrips.repository.NoteRepository;

public class AddNoteViewModel extends AndroidViewModel {

    private AppDatabase database;
    private AppExecutors appExecutors;

    public AddNoteViewModel(@NonNull Application application) {
        super(application);
        this.database = AppDatabase.getsInstance(this.getApplication());
        this.appExecutors = AppExecutors.getInstance();
    }

    public LiveData<Long> addCheckIn(CheckIn checkIn){
       return CheckInRepository.getInstance(database,appExecutors).insertCheckIn(checkIn);
    }

    public void addNote(Note note) {
        NoteRepository.getInstance(database, appExecutors).insertNote(note);
    }
}
