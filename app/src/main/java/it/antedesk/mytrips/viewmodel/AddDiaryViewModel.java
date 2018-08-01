package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.repository.DiaryRepository;

public class AddDiaryViewModel extends AndroidViewModel {

    private AppDatabase database;
    private AppExecutors appExecutors;

    public AddDiaryViewModel(@NonNull Application application) {
        super(application);
        this.database = AppDatabase.getsInstance(this.getApplication());
        this.appExecutors = AppExecutors.getInstance();
    }

    public void addDiary(Diary diary) {
        DiaryRepository.getInstance(database, appExecutors).insertDiary(diary);
    }
}
