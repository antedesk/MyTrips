package it.antedesk.mytrips;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AddDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        Spinner categorySpinner = findViewById(R.id.diary_category_spinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.diary_categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        Spinner coTravelerSpinner = findViewById(R.id.diary_cotraveler_spinner);
        ArrayAdapter<CharSequence> coTraveleAdapter = ArrayAdapter.createFromResource(this,
                R.array.diary_cotraveler_array, android.R.layout.simple_spinner_item);
        coTraveleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }
}
