package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.Activity;
import it.antedesk.mytrips.model.Note;

public interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY date_time DESC")
    List<Note> loadAllNotes();

    @Query("SELECT * FROM notes WHERE diary_id=:diaryId")
    List<Activity> retrieveNotesByDiaryId(final int diaryId);

    @Insert
    void insertNote(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);
}
