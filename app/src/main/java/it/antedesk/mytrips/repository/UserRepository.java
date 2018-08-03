package it.antedesk.mytrips.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.model.User;

public class UserRepository {
    private static final String LOG_TAG = UserRepository.class.getSimpleName();

    private static UserRepository sInstance;
    private final AppDatabase mDb;
    private final AppExecutors appExecutors;

    private UserRepository(final AppDatabase database, final AppExecutors appExecutors) {
        mDb = database;
        this.appExecutors = appExecutors;
    }

    public static UserRepository getInstance(final AppDatabase database, final AppExecutors appExecutors) {
        if (sInstance == null) {
            synchronized (UserRepository.class) {
                if (sInstance == null) {
                    sInstance = new UserRepository(database, appExecutors);
                }
            }
        }
        return sInstance;
    }

    public LiveData<User> geteUserById(long userId) {
        MutableLiveData<User> user = new MutableLiveData<>();
        appExecutors.diskIO().execute(() -> user.postValue(mDb.getUserDao().retrieveUserById(userId)));
        return user;
    }

    public void insertUser (User user) {
        appExecutors.diskIO().execute(() -> mDb.getUserDao().insert(user));
    }

    public void updateUser (User user) {
        appExecutors.diskIO().execute(() -> mDb.getUserDao().update(user));
    }

    public void deleteUser (User user) {
        appExecutors.diskIO().execute(() -> mDb.getUserDao().delete(user));
    }
}
