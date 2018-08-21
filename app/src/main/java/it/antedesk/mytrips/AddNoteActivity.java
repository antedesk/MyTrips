package it.antedesk.mytrips;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.model.minimal.CheckinMinimal;
import it.antedesk.mytrips.service.FetchAddressIntentService;
import it.antedesk.mytrips.viewmodel.AddNoteViewModel;

import static it.antedesk.mytrips.utils.Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static it.antedesk.mytrips.utils.Constants.KEY_LOCATION;
import static it.antedesk.mytrips.utils.Constants.KEY_REQUESTING_LOCATION_UPDATES;
import static it.antedesk.mytrips.utils.Constants.LOCATION_DATA_EXTRA;
import static it.antedesk.mytrips.utils.Constants.RECEIVER;
import static it.antedesk.mytrips.utils.Constants.REQUEST_CHECK_SETTINGS;
import static it.antedesk.mytrips.utils.Constants.RESULT_DATA_KEY;
import static it.antedesk.mytrips.utils.Constants.SELECTED_DIARY_ID;
import static it.antedesk.mytrips.utils.Constants.UPDATE_INTERVAL_IN_MILLISECONDS;

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

    @BindView(R.id.location_address_view)
    EditText mLocationAddressET;


    private boolean errors = false;
    private Date currentDate;
    private long diaryId;


    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    protected LocationRequest mLocationRequest;
    private AddressResultReceiver mResultReceiver;

    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Tracks whether the user has requested an address. Becomes true when the user requests an
     * address and false when the address (or an error message) is delivered.
     */
    private boolean mAddressRequested;

    private CheckinMinimal mCheckinMinimal;


    private Button mFetchAddressButton;
    private static final String TAG = AddNoteActivity.class.getSimpleName();


    // Keys for storing activity state in the Bundle.


    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;
    private ProgressDialog mProgressDialog;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;


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

        mLocationAddressET = findViewById(R.id.location_address_view);
        mFetchAddressButton = findViewById(R.id.fetch_address_button);

        // Set defaults, then update using values stored in the Bundle.
        mAddressRequested = false;
        mRequestingLocationUpdates = true;

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        // create the process of building the LocationCallback, LocationRequest, and LocationSettingsRequest objects.
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();

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
            String timeStr = hourOfDay + ":" + minute;
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

    /**
     * Updates fields based on data stored in the bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
        }
    }

    /**
     * Sets up the location request.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                if(mAddressRequested)
                    startIntentService();
            }
        };
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    Log.i(TAG, "All location settings are satisfied.");
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                    "location settings ");
                            try {
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(AddNoteActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.i(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);
                            Toast.makeText(AddNoteActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                            mRequestingLocationUpdates = false;
                    }
                });
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, task -> mRequestingLocationUpdates = false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Remove location updates to save battery.
        stopLocationUpdates();
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
    /**
     * Toggles the visibility of the progress bar. Enables or disables the Fetch Address button.
     */
    private void updateUIWidgets() {
        if (mAddressRequested) {
            showProgressDialog();
            mFetchAddressButton.setEnabled(false);
        } else {
            hideProgressDialog();
            mFetchAddressButton.setEnabled(true);
        }
    }

    /**
     * Runs when user clicks the Fetch Address button.
     */
    public void fetchAddressButtonHandler(View view) {
        if (mCurrentLocation != null) {
            startIntentService();
        } else {
            mAddressRequested = true;
            updateUIWidgets();
            startLocationUpdates();
        }
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
        intent.putExtra(LOCATION_DATA_EXTRA, mCurrentLocation);

        startService(intent);
    }

    /**
     * Updates the address in the UI.
     */
    private void displayAddressOutput() {
        if(mCheckinMinimal != null)
            mLocationAddressET.setText(mCheckinMinimal.getAddress());
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbarPermission(final int mainTextStringId, final int actionStringId,
                                        View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
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
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbarPermission(R.string.permission_rationale,
                    android.R.string.ok, view -> {
                        ActivityCompat.requestPermissions(AddNoteActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_PERMISSIONS_REQUEST_CODE);
                    });
        } else {
            Log.i(TAG, "Requesting permission");
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
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (mRequestingLocationUpdates) {
                    Log.i(TAG, "Permission granted, updates requested, starting location updates");
                    startLocationUpdates();
                }
            } else {
                // Permission denied.

                showSnackbarPermission(R.string.permission_denied_explanation,
                        R.string.settings, view -> {
                            // Build intent that displays the App settings screen.
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
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
            mCheckinMinimal = resultData.getParcelable(RESULT_DATA_KEY);
            displayAddressOutput();

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
            updateUIWidgets();
        }
    }
}
