package it.antedesk.mytrips.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.minimal.BudgetInfo;
import it.antedesk.mytrips.model.minimal.CategoryBudget;
import it.antedesk.mytrips.model.minimal.DailyBudget;
import it.antedesk.mytrips.model.minimal.DatesInfo;
import it.antedesk.mytrips.repository.DiaryRepository;
import it.antedesk.mytrips.repository.NoteRepository;

public class DiaryStatisticsViewModel extends AndroidViewModel {

    private AppDatabase database;
    private AppExecutors appExecutors;

    public DiaryStatisticsViewModel(@NonNull Application application) {
        super(application);
        database = AppDatabase.getsInstance(this.getApplication());
        appExecutors = AppExecutors.getInstance();
    }

    public LiveData<List<CategoryBudget>> getTotalBudgetByCategoriesAndDiaryId(long diaryId) {
        return NoteRepository.getInstance(database, appExecutors).getTotalBudgetByCategoriesAndDiaryId(diaryId);
    }

    public LiveData<DailyBudget> getAVGBudgetByDiaryId(long diaryId) {
        return NoteRepository.getInstance(database, appExecutors).getAVGBudgetByDiaryId(diaryId);
    }

    public LiveData<Integer> getTotalNotesByDiaryId(long diaryId) {
        return NoteRepository.getInstance(database, appExecutors).getTotalNotesByDiaryId(diaryId);
    }

    public LiveData<Integer> getTotalCheckinsByDiaryId(long diaryId) {
        return NoteRepository.getInstance(database, appExecutors).getTotalCheckinsByDiaryId(diaryId);
    }

    public LiveData<Double> getTotalBudgetByDiaryId(long diaryId) {
        return NoteRepository.getInstance(database, appExecutors).getTotalBudgetByDiaryId(diaryId);
    }

    public LiveData<BudgetInfo> getDiaryBudgetByDiaryId(long diaryId) {
        return DiaryRepository.getInstance(database, appExecutors).getBudgetByDiaryId(diaryId);
    }

    public LiveData<DatesInfo> getDatesInfoByDiaryId(long diaryId) {
        return NoteRepository.getInstance(database, appExecutors).getDatesInfoByDiaryId(diaryId);
    }

    public LiveData<List<DailyBudget>> getTotalBudgetByDayAndDiaryId(long diaryId) {
        return NoteRepository.getInstance(database, appExecutors).getTotalBudgetByDayAndDiaryId(diaryId);
    }
}
