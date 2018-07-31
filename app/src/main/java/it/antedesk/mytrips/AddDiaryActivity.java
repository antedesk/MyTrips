package it.antedesk.mytrips;

import android.app.DatePickerDialog;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDiaryActivity extends AppCompatActivity {

    private Calendar myCalendar = Calendar.getInstance();
    @BindView(R.id.diary_start_date_edt)
    EditText startDateEditTxt;
    @BindView(R.id.diary_end_date_edt)
    EditText endDateEditTxt;
    @BindView(R.id.diary_category_spinner)
    Spinner categorySpinner;
    @BindView(R.id.diary_cotraveler_spinner)
    Spinner coTravelerSpinner;
    @BindView(R.id.diary_currency_spinner)
    Spinner currenciesSpinner;
    @BindView(R.id.diary_name_txtinl)
    TextInputLayout diaryNameInLayout;
    @BindView(R.id.diary_name_edtxt)
    TextInputEditText diaryNameEditText;
    @BindView(R.id.diary_description_txtinl)
    TextInputLayout diaryDescInLayout;
    @BindView(R.id.diary_description_edtxt)
    TextInputEditText diaryDescEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);

        setupDates();
        setupSpinners();

        diaryNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > diaryNameInLayout.getCounterMaxLength())
                    diaryNameEditText.setError(getString(R.string.error_str_len) + diaryNameInLayout.getCounterMaxLength());
                else
                    diaryNameEditText.setError(null);
            }
        });

        diaryDescEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > diaryDescInLayout.getCounterMaxLength())
                    diaryDescEditText.setError(getString(R.string.error_str_len) + diaryDescInLayout.getCounterMaxLength());
                else
                    diaryDescEditText.setError(null);
            }
        });
    }

    /**
     * method to initialize the DatePikerDialogs and text for the start and end dates.
     */
    private void setupDates() {
        DatePickerDialog.OnDateSetListener startDatePickerListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            DateFormat dateFormat = DateFormat.getDateInstance();
            startDateEditTxt.setText(dateFormat.format(myCalendar.getTime()));
        };

        DatePickerDialog.OnDateSetListener endDatePickerListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            DateFormat dateFormat = DateFormat.getDateInstance();
            endDateEditTxt.setText(dateFormat.format(myCalendar.getTime()));
        };

        startDateEditTxt.setOnClickListener(view -> new DatePickerDialog(this, startDatePickerListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        endDateEditTxt.setOnClickListener(view -> new DatePickerDialog(this, endDatePickerListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    /**
     * method to initialize the Spinners
     */
    private void setupSpinners() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.diary_categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> coTraveleAdapter = ArrayAdapter.createFromResource(this,
                R.array.diary_cotraveler_array, android.R.layout.simple_spinner_item);
        coTraveleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coTravelerSpinner.setAdapter(coTraveleAdapter);

        ArrayAdapter<CharSequence> currenciesAdapter = ArrayAdapter.createFromResource(this,
                R.array.current_currencies_array, android.R.layout.simple_spinner_item);
        currenciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currenciesSpinner.setAdapter(currenciesAdapter);
    }
}
