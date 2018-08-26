package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.model.minimal.BudgetInfo;
import it.antedesk.mytrips.model.minimal.CategoryBudget;
import it.antedesk.mytrips.model.minimal.CheckinMinimal;
import it.antedesk.mytrips.model.minimal.DailyBudget;
import it.antedesk.mytrips.model.minimal.DatesInfo;

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
    int getTotalCheckinsByDiaryId(long diaryId);

    @Query("SELECT COUNT(id) FROM notes WHERE diary_id=:diaryId" )
    int getTotalNotesByDiaryId(long diaryId);

    @Query("SELECT SUM(notes.budget) as current_budget FROM notes WHERE diary_id=:diaryId")
    Double getTotalBudgetByDiaryId(long diaryId);

    @Query("SELECT SUM(budget) as budget, currency, category FROM notes WHERE diary_id=:diaryId GROUP BY category")
    CategoryBudget getTotalBudgetByCategoriesAndDiaryId(long diaryId);

    @Query("SELECT date(date_time) as date_time, SUM(budget) as budget, currency FROM notes WHERE diary_id=:diaryId GROUP BY date(datetime(date_time/1000, 'unixepoch'))")
    DailyBudget getTotalBudgetByDayAndDiaryId(long diaryId);

    @Query("SELECT (SUM(notes.budget)/COUNT(distinct(date(datetime(date_time/1000, 'unixepoch'))))) as budget, diaries.currency, null as date_time " +
            "FROM notes JOIN diaries on diary_id = diaries.id WHERE diary_id=:diaryId")
    DailyBudget getAVGBudgetByDiaryId(long diaryId);

    @Query("SELECT COUNT(distinct(date(datetime(notes.date_time/1000, 'unixepoch')))) as current_days, " +
            "julianday(date(datetime(diaries.end_date/1000, 'unixepoch'))) - " +
                "julianday(date(datetime(diaries.start_date/1000, 'unixepoch')))+1 as total_days " +
            "FROM notes JOIN diaries on diary_id = diaries.id WHERE diary_id=:diaryId")
    DatesInfo getDatesInfoByDiaryId(long diaryId);

    /*
     * The following methods should be used for the total stats, that will be developed in future
     * release
     */
    @Query("SELECT SUM(budget) FROM notes")
    double getTotalBudget4AllNotes();

    @Query("SELECT category, SUM(budget) FROM notes GROUP BY category")
    double getTotalBudgetByCategories();

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
