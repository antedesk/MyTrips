package it.antedesk.mytrips;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String currentDBPath=getDatabasePath("mytrips.db").getAbsolutePath();
        Log.d("pathDB", currentDBPath);
    }
}
