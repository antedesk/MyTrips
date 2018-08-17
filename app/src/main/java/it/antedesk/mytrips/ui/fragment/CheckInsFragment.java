package it.antedesk.mytrips.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.viewmodel.LoadDiaryCheckInsViewModel;

import static it.antedesk.mytrips.utils.SupportVariablesDefinition.SELECTED_DIARY_ID;

/**
 * A fragment containing the markers of all the user's check-ins.
 */
public class CheckInsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    private long diaryId;

    public CheckInsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CheckInsFragment newInstance(long diaryId) {
        CheckInsFragment fragment = new CheckInsFragment();
        Bundle args = new Bundle();
        args.putLong(SELECTED_DIARY_ID, diaryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_checkins, container, false);

        if (getArguments() != null)
            diaryId = getArguments().getLong(SELECTED_DIARY_ID);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        loadMarkers();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMarkers();
    }

    private void loadMarkers(){
        LoadDiaryCheckInsViewModel dataViewModel = ViewModelProviders.of(this).get(LoadDiaryCheckInsViewModel.class);
        dataViewModel.getCheckinsByDiaryId(diaryId).observe(this, (List<CheckIn> checkIns) -> {
            if (checkIns != null && checkIns.size() != 0) {
                LatLng firstCheckin = new LatLng(checkIns.get(0).getLatitude(), checkIns.get(0).getLongitude());
                for (CheckIn checkIn : checkIns) {
                    LatLng marker = new LatLng(checkIn.getLatitude(), checkIn.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(marker).title("Marker in position " + checkIns.indexOf(checkIn)));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLng(firstCheckin));
            }
        });
    }
}