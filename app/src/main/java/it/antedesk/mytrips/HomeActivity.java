package it.antedesk.mytrips;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;
import java.util.List;

import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.viewmodel.LoadDiariesViewModel;

public class HomeActivity extends AppCompatActivity {

    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDb = AppDatabase.getsInstance(getApplicationContext());

        final Diary d = new Diary("pippo", "my pippo", Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(), 200.59, "EUR", "family", false);
        retrieveDiaries();

    }

    private void retrieveDiaries(){
        LoadDiariesViewModel diariesVM = ViewModelProviders.of(this).get(LoadDiariesViewModel.class);
        diariesVM.getDiaries().observe(this, diaries -> {
            assert diaries != null;
            for(Diary diary : diaries) {
                Log.d("TEST", diary.getName());
            }
        });
    }
}
