package it.antedesk.mytrips;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.viewmodel.AddNoteViewModel;

import static it.antedesk.mytrips.utils.SupportVariablesDefinition.SELECTED_DIARY_ID;

public class AddNoteActivity extends AppCompatActivity {
    private Calendar calendar = Calendar.getInstance();
    @BindView(R.id.note_start_date_edt)
    EditText dateEditTxt;
    @BindView(R.id.note_time_edt)
    EditText timeEditTxt;
    @BindView(R.id.note_category_spinner)
    Spinner activitySpinner;
    @BindView(R.id.note_currency_spinner)
    Spinner currenciesSpinner;
    @BindView(R.id.note_name_txtinl)
    TextInputLayout noteNameInLayout;
    @BindView(R.id.note_name_edtxt)
    TextInputEditText noteNameEditText;
    @BindView(R.id.note_description_txtinl)
    TextInputLayout noteDescInLayout;
    @BindView(R.id.note_description_edtxt)
    TextInputEditText noteDescEditText;
    @BindView(R.id.note_money_edt)
    TextInputEditText noteBudgetEditText;

    private boolean errors = false;
    private Date currentDate;
    private long diaryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        // checking if it is null, if so close the activity
        if (intent == null) {
            Snackbar.make(findViewById(R.id.form_scroller), getString(R.string.general_error), Snackbar.LENGTH_LONG).show();
            finish();
        }

        assert intent != null;
        diaryId = intent.getLongExtra(SELECTED_DIARY_ID, 0);

        if (diaryId == 0) {
            Snackbar.make(findViewById(R.id.form_scroller), getString(R.string.general_error), Snackbar.LENGTH_LONG).show();
            finish();
        }

        setTitle(getString(R.string.add_note_str));

        setupDateTime();
        setupSpinners();
        setupTexListener();

    }

    /**
     * Listeners for the TextInputEditText
     */
    private void setupTexListener() {
        noteNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > noteNameInLayout.getCounterMaxLength())
                    noteNameEditText.setError(getString(R.string.error_str_len) + noteNameInLayout.getCounterMaxLength());
                else
                    noteNameEditText.setError(null);
            }
        });

        noteDescEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > noteDescInLayout.getCounterMaxLength())
                    noteDescEditText.setError(getString(R.string.error_str_len) + noteDescInLayout.getCounterMaxLength());
                else
                    noteDescEditText.setError(null);
            }
        });
    }

    /**
     * method to initialize the DatePikerDialogs and text for the start and end dates.
     */
    private void setupDateTime() {
        DatePickerDialog.OnDateSetListener datePickerListener = (view, year, monthOfYear, dayOfMonth) -> {
            if (dateEditTxt.getError() != null) dateEditTxt.setError(null);

            setCalendarInfo(calendar, year, monthOfYear, dayOfMonth);

            DateFormat dateFormat = DateFormat.getDateInstance();
            currentDate = calendar.getTime();
            dateEditTxt.setText(dateFormat.format(currentDate));
        };

        TimePickerDialog.OnTimeSetListener timePickerListener = (timePicker, hourOfDay, minute) -> {
            setTimeCalendarInfo(calendar, hourOfDay, minute);
            String timeStr = hourOfDay+":"+minute;
            timeEditTxt.setText(timeStr);
        };

        dateEditTxt.setOnClickListener(view -> new DatePickerDialog(this, datePickerListener, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        timeEditTxt.setOnClickListener(view -> new TimePickerDialog(this, timePickerListener, calendar
                .get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),
                true).show());
    }

    /**
     * method to initialize the Spinners
     */
    private void setupSpinners() {
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.activity_categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(categoryAdapter);

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

    private Calendar setTimeCalendarInfo(Calendar calendar, int hour, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    private Note getFormData() {

        if (noteNameEditText.getError() != null || noteDescEditText.getError() != null) {
            errors = true;
        }

        if (noteNameEditText.length() == 0) {
            noteNameEditText.setError(getString(R.string.required_field));
            errors = true;
        }

        if (currentDate == null) {
            snackBarError(R.string.missing_data);
            dateEditTxt.setError(getString(R.string.missing_date));
            errors = true;
        }

        double budget = 0;
        if (noteBudgetEditText.getText() != null && noteBudgetEditText.length() != 0)
            budget = Double.valueOf(noteBudgetEditText.getText().toString());


        return new Note(
                diaryId,
                noteNameEditText.getText().toString(),
                noteDescEditText.getText().toString(),
                calendar.getTime(),
                activitySpinner.getSelectedItem().toString(),
                budget,
                currenciesSpinner.getSelectedItem().toString().split(" - ")[0],
                0,
                "sun",
                30
        );
    }

    public void onSaveNoteClick(View view) {
        CheckIn checkIn = new CheckIn(
                41.890251,//41.9027835,
                12.492373,//12.4963655,
                "vattela a pesca, 45",
                "Roma",
                "Italia"
        );
        Note note = getFormData();
        if (note != null) {
            Log.d(AddNoteActivity.class.getName(), note.toString());
            AddNoteViewModel dataViewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);
            dataViewModel.addCheckIn(checkIn).observe(this, checkInId ->{
                note.setCheckInId(checkInId);
                dataViewModel.addNote(note);
            });
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
