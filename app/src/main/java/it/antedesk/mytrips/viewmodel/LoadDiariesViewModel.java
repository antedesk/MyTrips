package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.repository.DiaryRepository;

public class LoadDiariesViewModel extends AndroidViewModel {

    private AppDatabase database;
    private AppExecutors appExecutors;

    public LoadDiariesViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getsInstance(this.getApplication());
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<Diary>> getDiaries() {
        return DiaryRepository.getInstance(database, appExecutors).getDiaries();
    }
    public LiveData<List<Diary>> getPlans() {
        return DiaryRepository.getInstance(database, appExecutors).getPlans();
    }
}
