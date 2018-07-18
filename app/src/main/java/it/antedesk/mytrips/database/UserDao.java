package it.antedesk.mytrips.database;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.User;

public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> loadAllUsers();

    @Insert
    void insertUser(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(User user);

    @Delete
    void deleteNote(User user);
}
