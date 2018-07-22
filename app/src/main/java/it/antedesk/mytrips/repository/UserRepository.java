package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;
import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.User;

public class UserRepository {
    private static final String LOG_TAG = UserRepository.class.getSimpleName();

    private static UserRepository sInstance;
    private final AppDatabase mDb;

    private UserRepository(final AppDatabase database) {
        mDb = database;
    }

    public static UserRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (UserRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository(database);
                }
            }
        }
        return sInstance;
    }

    public LiveData<User> geteUserById(int userId) {
        return mDb.getUserDao().retrieveUserById(userId);
    }

    public void insertUser (User user) {
        mDb.getUserDao().insert(user);
    }

    public void updateUser (User user) {
        mDb.getUserDao().update(user);
    }

    public void deleteUser (User user) {
        mDb.getUserDao().delete(user);
    }
}
