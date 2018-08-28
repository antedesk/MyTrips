package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.minimal.BudgetInfo;
import it.antedesk.mytrips.model.minimal.CategoryBudget;
import it.antedesk.mytrips.model.minimal.CategoryTotalInfo;
import it.antedesk.mytrips.model.minimal.DailyBudget;
import it.antedesk.mytrips.model.minimal.DatesInfo;
import it.antedesk.mytrips.repository.DiaryRepository;
import it.antedesk.mytrips.repository.NoteRepository;

public class GeneralStatsViewModel  extends AndroidViewModel {

    private AppDatabase database;
    private AppExecutors appExecutors;

    public GeneralStatsViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getsInstance(this.getApplication());
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<Integer> getTotalCheckIn() {
        return NoteRepository.getInstance(database, appExecutors).getTotalCheckIn();
    }

    public LiveData<Integer> getTotalCities() {
        return NoteRepository.getInstance(database, appExecutors).getTotalCities();
    }

    public LiveData<Integer> getTotalCountries() {
        return NoteRepository.getInstance(database, appExecutors).getTotalCountries();
    }

    public LiveData<Integer> getTotalDays() {
        return NoteRepository.getInstance(database, appExecutors).getTotalDays();
    }

    public LiveData<Integer> getTotalActivities() {
        return NoteRepository.getInstance(database, appExecutors).getTotalActivities();
    }

    public LiveData<Integer> getTotalDiaries() {
        return DiaryRepository.getInstance(database, appExecutors).getTotalDiaries();
    }

    public LiveData<List<CategoryTotalInfo>> getActivitiesDistribution() {
        return NoteRepository.getInstance(database, appExecutors).getActivitiesDistribution();
    }

}
