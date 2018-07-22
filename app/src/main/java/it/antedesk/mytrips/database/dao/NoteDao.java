package it.antedesk.mytrips.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.Activity;
import it.antedesk.mytrips.model.Note;

@Dao
public interface NoteDao extends BaseDao<Note>{

    @Query("SELECT * FROM notes WHERE id=:id")
    LiveData<Note> retrieveNoteById(int id);

    @Query("SELECT * FROM notes ORDER BY date_time DESC")
    LiveData<List<Note>> loadAllNotes();

    @Query("SELECT * FROM notes WHERE diary_id=:diaryId")
    LiveData<List<Activity>> retrieveNotesByDiaryId(final int diaryId);


    @Query("SELECT * FROM activities WHERE diary_id=:id ORDER BY date_time DESC")
    LiveData<List<Activity>> loadActivitiesByDiaryId(int id);

    @Query("SELECT COUNT(DISTINCT address) " +
            "FROM notes LEFT JOIN check_ins on notes.check_in_id = check_ins.id " +
            "WHERE diary_id=:diaryId " +
            "ORDER BY date_time DESC")
    int getTotalCheckinsByDiaryId(int diaryId);

    @Query("SELECT SUM(budget) FROM notes WHERE diary_id=:diaryId")
    double getTotalBudgetByDiaryId(int diaryId);

    @Query("SELECT SUM(budget) FROM notes")
    double getTotalBudget4AllActivity();

    @Query("SELECT category, SUM(budget) FROM notes GROUP BY category")
    double getTotalBudgetByCategories();

    @Query("SELECT category, SUM(budget) FROM notes WHERE diary_id=:diaryId GROUP BY category")
    double getTotalBudgetByCategoriesAndDiaryId(int diaryId);
}
