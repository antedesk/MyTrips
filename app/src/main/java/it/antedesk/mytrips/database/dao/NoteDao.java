package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.model.minimal.CheckinMinimal;

@Dao
public interface NoteDao extends BaseDao<Note>{

    @Query("SELECT * FROM notes")
    List<Note> loadNotes();

    @Query("SELECT * FROM notes WHERE id=:id")
    Note retrieveNoteById(long id);

    @Query("SELECT * FROM notes WHERE diary_id=:diaryId ORDER BY date_time DESC")
    List<Note> retrieveNotesByDiaryId(final long diaryId);

    @Query("SELECT * FROM notes WHERE diary_id=:id ORDER BY date_time DESC")
    List<Note> loadNotesByDiaryId(long id);

    @Query("SELECT id, title, category, category_id, latitude, longitude, address, city, country, country_code " +
            "FROM notes WHERE diary_id=:diaryId AND latitude IS NOT NULL  AND longitude IS NOT NULL")
    List<CheckinMinimal> getCheckinsByDiaryId(long diaryId);

    @Query("SELECT COUNT(DISTINCT address) " +
            "FROM notes " +
            "WHERE diary_id=:diaryId " +
            "ORDER BY date_time DESC")
    long getTotalCheckinsByDiaryId(long diaryId);

    @Query("SELECT SUM(budget) FROM notes WHERE diary_id=:diaryId")
    double getTotalBudgetByDiaryId(long diaryId);

    @Query("SELECT SUM(budget) FROM notes")
    double getTotalBudget4AllNotes();

    @Query("SELECT category, SUM(budget) FROM notes GROUP BY category")
    double getTotalBudgetByCategories();

    @Query("SELECT category, SUM(budget) FROM notes WHERE diary_id=:diaryId GROUP BY category")
    double getTotalBudgetByCategoriesAndDiaryId(long diaryId);

    @Query("SELECT  COUNT(DISTINCT address) FROM notes")
    int getTotalCheckIn();

    @Query("SELECT  COUNT(DISTINCT city) FROM notes")
    int getTotalCities();

    @Query("SELECT COUNT(DISTINCT country) FROM notes")
    int getTotalCountries();

    @Query("SELECT DISTINCT country FROM notes")
    List<String> getCountries();

    @Query("SELECT DISTINCT country_code FROM notes")
    List<String> getCountryCodes();


}
