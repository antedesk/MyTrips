package it.antedesk.mytrips;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.Diary;

public class HomeActivity extends AppCompatActivity {

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDb = AppDatabase.getsInstance(getApplicationContext());

        final Diary d = new Diary("pippo", "my pippo", Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(), 200.59, "EUR", "family", false);


        final LiveData<List<Diary>> dd = mDb.getDiaryDao().loadAllDiaries(false);
        dd.observe(this, new Observer<List<Diary>>() {
            @Override
            public void onChanged(@Nullable List<Diary> diaries) {
                assert diaries != null;
                for(Diary diary : diaries) {
                    Log.d("TEST", diary.getName());
                }
            }
        });


/*
        final Diary d = new Diary("pippo", "my pippo", Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(), 200.59, "EUR", "family", false);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.getDiaryDao().insertDiary(d);
                List<Diary> dd = mDb.getDiaryDao().loadAllDiaries2(false);
                Log.d("pathDB", dd.get(0).getName());
            }
        });  */
        String currentDBPath=getDatabasePath("mytrips.db").getAbsolutePath();
        Log.d("pathDB", currentDBPath);

    }
}
