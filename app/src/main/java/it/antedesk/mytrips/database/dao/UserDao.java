package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.Activity;
import it.antedesk.mytrips.model.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users WHERE id=:id")
    Activity retrieveUserById(int id);

    @Query("SELECT * FROM users")
    List<User> loadAllUsers();

    @Insert
    void insertUser(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateNote(User user);

    @Delete
    void deleteNote(User user);
}
