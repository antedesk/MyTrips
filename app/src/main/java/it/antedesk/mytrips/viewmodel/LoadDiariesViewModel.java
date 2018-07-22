package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.repository.DiaryRepository;

public class LoadDiariesViewModel extends AndroidViewModel {

    private LiveData<List<Diary>> diaries;
    private LiveData<List<Diary>> plans;

    public LoadDiariesViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getsInstance(this.getApplication());
        AppExecutors appExecutors = AppExecutors.getInstance();
        diaries = DiaryRepository.getInstance(database, appExecutors).getDiaries();
        plans = DiaryRepository.getInstance(database, appExecutors).getPlans();
    }

    public LiveData<List<Diary>> getDiaries() {
        return diaries;
    }
    public LiveData<List<Diary>> getPlans() {
        return plans;
    }
}
