package it.antedesk.mytrips;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.viewmodel.AddDiaryViewModel;

import static it.antedesk.mytrips.utils.SupportVariablesDefinition.IS_PLAN;

public class AddNoteActivity extends AppCompatActivity {
    private Calendar calendar = Calendar.getInstance();
    @BindView(R.id.note_start_date_edt)
    EditText dateEditTxt;
    @BindView(R.id.note_time_edt)
    EditText timeEditTxt;
    @BindView(R.id.note_category_spinner)
    Spinner categorySpinner;
    @BindView(R.id.note_currency_spinner)
    Spinner currenciesSpinner;
    @BindView(R.id.note_name_txtinl)
    TextInputLayout diaryNameInLayout;
    @BindView(R.id.note_name_edtxt)
    TextInputEditText diaryNameEditText;
    @BindView(R.id.note_description_txtinl)
    TextInputLayout diaryDescInLayout;
    @BindView(R.id.note_description_edtxt)
    TextInputEditText diaryDescEditText;
    @BindView(R.id.note_money_edt)
    TextInputEditText diaryBudgetEditText;

    private boolean errors = false;
    private Date currentStartDate;
    private Date currentEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        // checking if it is null, if so close the activity
        if (intent == null) {
            Snackbar.make(findViewById(R.id.form_scroller), getString(R.string.general_error), Snackbar.LENGTH_LONG).show();
            finish();
        }

        setTitle(getString(R.string.add_note_str));

        setupDates();
        setupSpinners();
        setupTexListener();

    }

    /**
     * Listeners for the TextInputEditText
     */
    private void setupTexListener() {
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
            if (dateEditTxt.getError() != null) dateEditTxt.setError(null);

            setCalendarInfo(calendar, year, monthOfYear, dayOfMonth);

            DateFormat dateFormat = DateFormat.getDateInstance();
            currentStartDate = calendar.getTime();
            dateEditTxt.setText(dateFormat.format(currentStartDate));
        };

        DatePickerDialog.OnDateSetListener endDatePickerListener = (view, year, monthOfYear, dayOfMonth) -> {

            if (timeEditTxt.getError() != null) timeEditTxt.setError(null);

            setCalendarInfo(calendar, year, monthOfYear, dayOfMonth);
            DateFormat dateFormat = DateFormat.getDateInstance();
            currentEndDate = calendar.getTime();
            timeEditTxt.setText(dateFormat.format(currentEndDate));

        };

        dateEditTxt.setOnClickListener(view -> new DatePickerDialog(this, startDatePickerListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        timeEditTxt.setOnClickListener(view -> new DatePickerDialog(this, endDatePickerListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    /**
     * method to initialize the Spinners
     */
    private void setupSpinners() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.diary_categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> currenciesAdapter = ArrayAdapter.createFromResource(this,
                R.array.current_currencies_array, android.R.layout.simple_spinner_item);
        currenciesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currenciesSpinner.setAdapter(currenciesAdapter);
    }

    private Calendar setCalendarInfo(Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        return calendar;
    }

    private Calendar setTimeCalendarInfo(Calendar calendar, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar;
    }

    private Diary getFormData() {

        if (diaryNameEditText.getError() != null || diaryDescEditText.getError() != null) {
            errors = true;
        }

        if (diaryNameEditText.length() == 0) {
            diaryNameEditText.setError(getString(R.string.required_field));
            errors = true;
        }

        if (currentStartDate == null) {
            snackBarError(R.string.missing_data);
            dateEditTxt.setError(getString(R.string.missing_date));
            errors = true;
        }

        if (currentEndDate == null) {
            snackBarError(R.string.missing_data);
            timeEditTxt.setError(getString(R.string.missing_date));
            errors = true;
        }

        if (currentStartDate != null && currentEndDate != null
                && (currentEndDate.getTime() < currentStartDate.getTime())) {
            dateEditTxt.setError(getString(R.string.missing_date));
            errors = true;
        }

        double budget = 0;
        if (diaryBudgetEditText.getText() != null && diaryBudgetEditText.length() != 0)
            budget = Double.valueOf(diaryBudgetEditText.getText().toString());

        Diary diary = errors ? null : new Diary(diaryNameEditText.getText().toString(),
                diaryDescEditText.getText().toString(),
                currentStartDate,
                currentEndDate,
                budget, currenciesSpinner.getSelectedItem().toString().split(" - ")[0],
                categorySpinner.getSelectedItem().toString(), isPlan);

        return diary;
    }

    public void onSaveClick(View view) {
        Diary diary = getFormData();
        if (diary != null) {
            Log.d(AddDiaryActivity.class.getName(), diary.toString());
            AddDiaryViewModel dataViewModel = ViewModelProviders.of(this).get(AddDiaryViewModel.class);
            dataViewModel.addDiary(diary);
            finish();
        } else {
            errors = false;
            snackBarError(R.string.missing_data);
        }
    }

    private void snackBarError(int messageId) {
        Snackbar.make(findViewById(R.id.form_scroller), getString(messageId), Snackbar.LENGTH_LONG).show();
    }
}
