package it.antedesk.mytrips.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.model.User;

@Dao
public interface UserDao extends BaseDao<User>{

    @Query("SELECT * FROM users WHERE id=:id")
    User retrieveUserById(int id);

    @Query("SELECT * FROM users")
    LiveData<List<User>> loadAllUsers();

}
