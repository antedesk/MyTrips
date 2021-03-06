package it.antedesk.mytrips.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import it.antedesk.mytrips.database.converter.DateConverter;
import it.antedesk.mytrips.database.dao.ActivityDao;
import it.antedesk.mytrips.database.dao.DiaryDao;
import it.antedesk.mytrips.database.dao.NoteDao;
import it.antedesk.mytrips.database.dao.UserDao;
import it.antedesk.mytrips.model.*;

@Database(entities = {User.class, Diary.class, Activity.class, Note.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase{

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "mytrips";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context) {
        if(sInstance == null){
            synchronized (LOCK){
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME).build();
            }
        }

        Log.d(LOG_TAG, "Getting the db instance");
        return sInstance;
    }

    public abstract UserDao getUserDao();
    public abstract DiaryDao getDiaryDao();
    public abstract ActivityDao getActivityDao();
    public abstract NoteDao getNoteDao();
}
