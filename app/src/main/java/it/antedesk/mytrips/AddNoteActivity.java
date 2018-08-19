package it.antedesk.mytrips;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.service.FetchAddressIntentService;
import it.antedesk.mytrips.viewmodel.AddNoteViewModel;

import static it.antedesk.mytrips.utils.Constants.FETCH_ADDRESS_INTENT_SERVICE;
import static it.antedesk.mytrips.utils.Constants.LOCATION_DATA_EXTRA;
import static it.antedesk.mytrips.utils.Constants.RECEIVER;
import static it.antedesk.mytrips.utils.Constants.RESULT_DATA_KEY;
import static it.antedesk.mytrips.utils.Constants.SELECTED_DIARY_ID;
import static it.antedesk.mytrips.utils.Constants.SUCCESS_RESULT;

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


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";

    protected Location mLastLocation;
    private AddressResultReceiver mResultReceiver;

    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     */
    private boolean mAddressRequested;

    private String mAddressOutput;

    private TextView mLocationAddressTextView;

    private ProgressBar mProgressBar;

    private Button mFetchAddressButton;

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


        mResultReceiver = new AddressResultReceiver(new Handler());

        mLocationAddressTextView = findViewById(R.id.location_address_view);
        mProgressBar = findViewById(R.id.progress_bar_add_loc);
        mFetchAddressButton = findViewById(R.id.fetch_address_button);

        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = false;
        mAddressOutput = "";
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        updateUIWidgets();
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

        if (currentDate == null || timeEditTxt.getText().length() == 0) {
            snackBarError(R.string.missing_data);
            timeEditTxt.setError(getString(R.string.missing_time));
            errors = true;
        }


        double budget = 0;
        if (noteBudgetEditText.getText() != null && noteBudgetEditText.length() != 0)
            budget = Double.valueOf(noteBudgetEditText.getText().toString());

        return errors ? null : new Note(
                diaryId,
                noteNameEditText.getText().toString(),
                noteDescEditText.getText().toString(),
                calendar.getTime(),
                activitySpinner.getSelectedItem().toString(),
                budget,
                currenciesSpinner.getSelectedItem().toString().split(" - ")[0],
                41.890251,//41.9027835,
                12.492373,//12.4963655,
                "vattela a pesca, 45",
                "Roma",
                "Italia",
                "IT",
                "sun",
                30
        );
    }

    public void onSaveNoteClick(View view) {
        Note note = getFormData();
        if (note != null) {
            Log.d(AddNoteActivity.class.getName(), note.toString());
            AddNoteViewModel dataViewModel = ViewModelProviders.of(this).get(AddNoteViewModel.class);
            dataViewModel.addNote(note);
            finish();
        } else {
            errors = false;
            snackBarError(R.string.missing_data);
        }
    }

    private void snackBarError(int messageId) {
        Snackbar.make(findViewById(R.id.form_scroller), getString(messageId), Snackbar.LENGTH_LONG).show();
    }


    @Override
    public void onStart() {
        super.onStart();

        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getAddress();
        }
    }

    /**
     * Updates fields based on data stored in the bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
                displayAddressOutput();
            }
        }
    }

    /**
     * Runs when user clicks the Fetch Address button.
     */
    @SuppressWarnings("unused")
    public void fetchAddressButtonHandler(View view) {
        if (mLastLocation != null) {
            startIntentService();
            return;
        }

        // If we have not yet retrieved the user location, we process the user's request by setting
        // mAddressRequested to true. As far as the user is concerned, pressing the Fetch Address button
        // immediately kicks off the process of getting the address.
        mAddressRequested = true;
        updateUIWidgets();
    }

    /**
     * Creates an intent, adds location data to it as an extra, and starts the intent service for
     * fetching an address.
     */
    private void startIntentService() {
        // Create an intent for passing to the intent service responsible for fetching the address.
        Intent intent = new Intent(this, FetchAddressIntentService.class);

        // Pass the result receiver as an extra to the service.
        intent.putExtra(RECEIVER, mResultReceiver);

        // Pass the location data as an extra to the service.
        intent.putExtra(LOCATION_DATA_EXTRA, mLastLocation);

        // Start the service. If the service isn't already running, it is instantiated and started
        // (creating a process for it if needed); if it is running then it remains running. The
        // service kills itself automatically once all intents are processed.
        startService(intent);
    }

    /**
     * Gets the address for the last known location.
     */
    @SuppressWarnings("MissingPermission")
    private void getAddress() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.w(FETCH_ADDRESS_INTENT_SERVICE, "onSuccess:null");
                            return;
                        }

                        mLastLocation = location;

                        // Determine whether a Geocoder is available.
                        if (!Geocoder.isPresent()) {
                            showSnackbar(getString(R.string.no_geocoder_available));
                            return;
                        }

                        // If the user pressed the fetch address button before we had the location,
                        // this will be set to true indicating that we should kick off the intent
                        // service after fetching the location.
                        if (mAddressRequested) {
                            startIntentService();
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(FETCH_ADDRESS_INTENT_SERVICE, "getLastLocation:onFailure", e);
                    }
                });
    }

    /**
     * Updates the address in the UI.
     */
    private void displayAddressOutput() {
        mLocationAddressTextView.setText(mAddressOutput);
    }

    /**
     * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
     */
    private void updateUIWidgets() {
        if (mAddressRequested) {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);
            mFetchAddressButton.setEnabled(false);
        } else {
            mProgressBar.setVisibility(ProgressBar.GONE);
            mFetchAddressButton.setEnabled(true);
        }
    }

    /**
     * Shows a toast with the given text.
     */
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }
    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(FETCH_ADDRESS_INTENT_SERVICE, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(AddNoteActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            Log.i(FETCH_ADDRESS_INTENT_SERVICE, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(AddNoteActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(FETCH_ADDRESS_INTENT_SERVICE, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(FETCH_ADDRESS_INTENT_SERVICE, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getAddress();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    
    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    private class AddressResultReceiver extends ResultReceiver {
        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(RESULT_DATA_KEY);
            displayAddressOutput();

            // Show a toast message if an address was found.
            if (resultCode == SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            updateUIWidgets();
        }
    }
}
