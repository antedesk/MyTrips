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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.minimal.CheckinMinimal;
import it.antedesk.mytrips.viewmodel.LoadDiaryCheckInsViewModel;

import static it.antedesk.mytrips.utils.Constants.SELECTED_DIARY_ID;

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

    private int getMarkerIcon(String categoryId){
        switch(categoryId)
        {
            case "BRKFST":
                return R.drawable.ic_world;
            case "LNCH":
                return R.drawable.ic_world;
            case "DNNR":
                return R.drawable.ic_world;
            case "TRNSFR":
                return R.drawable.ic_world;
            case "OVRNGHY":
                return R.drawable.ic_world;
            case "EXCRSN":
                return R.drawable.ic_world;
            case "SHPPNG":
                return R.drawable.ic_world;
            case "CLTRL":
                return R.drawable.ic_world;
            default:
                return R.drawable.ic_world;
        }
    }

    private void loadMarkers(){
        LoadDiaryCheckInsViewModel dataViewModel = ViewModelProviders.of(this).get(LoadDiaryCheckInsViewModel.class);
        dataViewModel.getCheckinsByDiaryId(diaryId).observe(this, (List<CheckinMinimal> checkIns) -> {
            if (checkIns != null && checkIns.size() != 0) {
                for (CheckinMinimal checkIn : checkIns) {
                    if(checkIn.getAddress() == null || checkIn.getAddress().isEmpty())
                        continue;
                    LatLng marker = new LatLng(checkIn.getLatitude(), checkIn.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(marker).title(checkIn.getTitle())
                            .icon(BitmapDescriptorFactory.fromResource(getMarkerIcon(checkIn.getCategoryId()))));
                }
                LatLng firstCheckin = new LatLng(checkIns.get(0).getLatitude(), checkIns.get(0).getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstCheckin,13));
            }
        });
    }
}