package it.antedesk.mytrips.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.minimal.CheckinMinimal;
import it.antedesk.mytrips.utils.Constants;

import static it.antedesk.mytrips.utils.Constants.FAILURE_RESULT;
import static it.antedesk.mytrips.utils.Constants.FETCH_ADDRESS_INTENT_SERVICE;
import static it.antedesk.mytrips.utils.Constants.LOCATION_DATA_EXTRA;
import static it.antedesk.mytrips.utils.Constants.RESULT_DATA_KEY;
import static it.antedesk.mytrips.utils.Constants.SUCCESS_RESULT;

/**
 * Asynchronously handles an intent using a worker thread. Receives a ResultReceiver object and a
 * location through an intent. Tries to fetch the address for the location using a Geocoder, and
 * sends the result to the ResultReceiver.
 */
public class FetchAddressIntentService extends IntentService {

    /**
     * The receiver where results are forwarded from this service.
     */
    private ResultReceiver mReceiver;

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public FetchAddressIntentService() {
        // Use the FETCH_ADDRESS_INTENT_SERVICE to name the worker thread.
        super(FETCH_ADDRESS_INTENT_SERVICE);
    }

    /**
     * Tries to get the location address using a Geocoder. If successful, sends an address to a
     * result receiver. If unsuccessful, sends an error message instead.
     * Note: We define a {@link android.os.ResultReceiver} in * MainActivity to process content
     * sent from this service.
     *
     * This service calls this method from the default worker thread with the intent that started
     * the service. When this method returns, the service automatically stops.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";

        mReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        // Check if receiver was properly registered.
        if (mReceiver == null) {
            Log.wtf(FETCH_ADDRESS_INTENT_SERVICE, "No receiver received. There is nowhere to send the results.");
            return;
        }

        // Get the location passed to this service through an extra.
        Location location = intent.getParcelableExtra(LOCATION_DATA_EXTRA);

        CheckinMinimal checkin = new CheckinMinimal();
        // Make sure that the location data was really sent over through an extra. If it wasn't,
        // send an error error message and return.
        if (location == null) {
            errorMessage = getString(R.string.no_location_data_provided);
            checkin.setAddress(errorMessage);
            Log.wtf(FETCH_ADDRESS_INTENT_SERVICE, errorMessage);
            deliverResultToReceiver(FAILURE_RESULT, null);
            return;
        }

        // Errors could still arise from using the Geocoder (for example, if there is no
        // connectivity, or if the Geocoder is given illegal location data). Or, the Geocoder may
        // simply not have an address for a location. In all these cases, we communicate with the
        // receiver using a resultCode indicating failure. If an address is found, we use a
        // resultCode indicating success.

        // The Geocoder used in this sample. The Geocoder's responses are localized for the given
        // Locale, which represents a specific geographical or linguistic region. Locales are used
        // to alter the presentation of information such as numbers or dates to suit the conventions
        // in the region they describe.
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(FETCH_ADDRESS_INTENT_SERVICE, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(FETCH_ADDRESS_INTENT_SERVICE, errorMessage + ". " +
                    "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(FETCH_ADDRESS_INTENT_SERVICE, errorMessage);
            }
            checkin.setAddress(errorMessage);
            deliverResultToReceiver(FAILURE_RESULT, checkin);
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<>();
            for(int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }
            checkin.setLatitude(address.getLatitude());
            checkin.setLongitude(address.getLongitude());
            checkin.setCity(address.getLocality());
            checkin.setCountry(address.getCountryName());
            checkin.setCountryCode(address.getCountryCode());
            checkin.setAddress(TextUtils.join(System.getProperty("line.separator"), addressFragments));
            Log.i(FETCH_ADDRESS_INTENT_SERVICE, getString(R.string.address_found));
            deliverResultToReceiver(SUCCESS_RESULT, checkin);
        }
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverResultToReceiver(int resultCode, CheckinMinimal checkin) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_DATA_KEY, checkin);
        mReceiver.send(resultCode, bundle);
    }
}