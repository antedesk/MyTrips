package it.antedesk.mytrips.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import it.antedesk.mytrips.model.User;

@Dao
public interface UserDao extends BaseDao<User>{

    @Query("SELECT * FROM users WHERE id=:id")
    User retrieveUserById(long id);

}
