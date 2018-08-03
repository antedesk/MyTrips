package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.model.Note;

@Dao
public interface NoteDao extends BaseDao<Note>{

    @Query("SELECT * FROM notes")
    List<Note> loadNotes();

    @Query("SELECT * FROM notes WHERE id=:id")
    Note retrieveNoteById(long id);

    @Query("SELECT * FROM notes WHERE diary_id=:diaryId")
    List<Note> retrieveNotesByDiaryId(final long diaryId);

    @Query("SELECT * FROM check_ins WHERE id=:checkInId")
    CheckIn retrieveCheckInById(final long checkInId);

    @Query("SELECT * FROM notes WHERE diary_id=:id")
    List<Note> loadNotesByDiaryId(long id);

    @Query("SELECT COUNT(DISTINCT address) " +
            "FROM notes LEFT JOIN check_ins on notes.check_in_id = check_ins.id " +
            "WHERE diary_id=:diaryId " +
            "ORDER BY date_time DESC")
    int getTotalCheckinsByDiaryId(long diaryId);

    @Query("SELECT SUM(budget) FROM notes WHERE diary_id=:diaryId")
    double getTotalBudgetByDiaryId(long diaryId);

    @Query("SELECT SUM(budget) FROM notes")
    double getTotalBudget4AllNotes();

    @Query("SELECT category, SUM(budget) FROM notes GROUP BY category")
    double getTotalBudgetByCategories();

    @Query("SELECT category, SUM(budget) FROM notes WHERE diary_id=:diaryId GROUP BY category")
    double getTotalBudgetByCategoriesAndDiaryId(long diaryId);
}
