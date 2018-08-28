package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.model.minimal.BudgetInfo;

@Dao
public interface DiaryDao extends BaseDao<Diary>{

    @Query("SELECT * FROM diaries WHERE id=:id")
    Diary retrieveDiaryById(long id);

    @Query("SELECT * FROM diaries WHERE is_plan = :isPlan ORDER BY start_date DESC")
    List<Diary> loadAllDiaries(boolean isPlan);

    @Query("SELECT budget as total_budget, currency FROM diaries WHERE id = :diaryId")
    BudgetInfo getBudgetByDiaryId(long diaryId);

    @Query("SELECT COUNT(id) FROM diaries")
    int getTotalDiaries();
}
