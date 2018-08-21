package it.antedesk.mytrips.utils;

public final class Constants {

    public static final String IS_PLAN = "IS_PLAN";
    public static final String SELECTED_DIARY = "SELECTED_DIARY";
    public static final String SELECTED_DIARY_ID = "SELECTED_DIARY_ID";

    // for geocoder
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final String RECEIVER = "_RECEIVER";
    public static final String RESULT_DATA_KEY = "_RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = "_LOCATION_DATA_EXTRA";

    public static final String FETCH_ADDRESS_INTENT_SERVICE = "FETCHING_ADDRESS_IS";
    public final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    public final static String KEY_LOCATION = "location";


    /**
     * Constant used in the location settings dialog.
     */
    public static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;
}
