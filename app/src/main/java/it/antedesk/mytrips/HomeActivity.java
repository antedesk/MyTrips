package it.antedesk.mytrips;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.Diary;

public class HomeActivity extends AppCompatActivity {

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String currentDBPath=getDatabasePath("mytrips.db").getAbsolutePath();
        Log.d("pathDB", currentDBPath);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        /*
            Diary d = new Diary("pippo", "my pippo", Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(), 200.59, "EUR", "family", false);
        */
    }
}
