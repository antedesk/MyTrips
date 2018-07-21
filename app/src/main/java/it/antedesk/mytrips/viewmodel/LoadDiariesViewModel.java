package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.Diary;

//TODO REFACTORING THIS CLASS!
public class LoadDiariesViewModel extends AndroidViewModel {

    private LiveData<List<Diary>> diaries;

    public LoadDiariesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        diaries = database.getDiaryDao().loadAllDiaries(true);
    }

    public LiveData<List<Diary>> getDiaries() {
        return diaries;
    }
}
