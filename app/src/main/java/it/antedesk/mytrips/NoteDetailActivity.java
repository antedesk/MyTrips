package it.antedesk.mytrips;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.viewmodel.LoadNoteViewModel;

import static it.antedesk.mytrips.utils.Constants.SELECTED_NOTE;

public class NoteDetailActivity extends AppCompatActivity
        implements OnMapReadyCallback {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.notedt_date_tv)
    TextView mNotedtDateTv;
    @BindView(R.id.notedt_time_tv)
    TextView mNotedtTimeTv;
    @BindView(R.id.notedt_category_icon)
    ImageView mNotedtCategoryIcon;
    @BindView(R.id.notedt_category_tv)
    TextView mNotedtCategoryTv;
    @BindView(R.id.notedt_budget_tv)
    TextView mNotedtBudgetTv;
    @BindView(R.id.notedt_content_description_tv)
    TextView mNotedtContentDescriptionTv;

    private long noteId;
    private Note note;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_note_detail);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        // checking if it is null, if so close the activity
        if (intent == null) {
            Snackbar.make(findViewById(R.id.form_scroller), getString(R.string.general_error), Snackbar.LENGTH_LONG).show();
            finish();
        }

        assert intent != null;
        Note noteTmp = intent.getParcelableExtra(SELECTED_NOTE);

        if (note != null) {
            finish();
        }
        setTitle(noteTmp.getTitle());
        noteId = noteTmp.getId();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveDiaryNotes(noteId);
    }

    private void retrieveDiaryNotes(final long noteId) {
        LoadNoteViewModel dataViewModel = ViewModelProviders.of(this).get(LoadNoteViewModel.class);
        dataViewModel.geteNoteById(noteId).observe(this, (Note note) -> {
            if (note != null) {
                this.note = note;
                updateUI(note);
            }
        });
    }

    private void updateUI(Note note) {

        setTitle(note.getTitle());
        mNotedtBudgetTv.setText(TextUtils.concat(String.valueOf(note.getBudget()), note.getCurrency()!=null?" "+note.getCurrency():""));
        mNotedtCategoryTv.setText(note.getCategory());
        mNotedtCategoryIcon.setImageResource(getCategoryIcon(note.getCategoryId()));
        DateFormat dateFormat = DateFormat.getDateInstance();
        mNotedtDateTv.setText(dateFormat.format(note.getDateTime()));
        DateFormat timeFormat = DateFormat.getTimeInstance();
        mNotedtTimeTv.setText(timeFormat.format(note.getDateTime()));
        mNotedtContentDescriptionTv.setText(note.getDescription());

        if (note.getAddress() != null && mMap != null) {
            LatLng noteLocation = new LatLng(note.getLatitude(), note.getLongitude());
            mMap.addMarker(new MarkerOptions().position(noteLocation)
                    .title(note.getAddress())
                    .icon(BitmapDescriptorFactory.fromResource(getMarkerIcon(note.getCategoryId()))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(noteLocation,15));
        }
    }


    private int getMarkerIcon(String categoryId){
        switch(categoryId)
        {
            case "BRKFST":
                return R.drawable.ic_marker_breakfast;
            case "LNCH":
                return R.drawable.ic_marker_lunch;
            case "DNNR":
                return R.drawable.ic_marker_dinner;
            case "TRNSFR":
                return R.drawable.ic_marker_travel;
            case "OVRNGHY":
                return R.drawable.ic_marker_overnight_stay;
            case "EXCRSN":
                return R.drawable.ic_marker_excursion;
            case "SHPPNG":
                return R.drawable.ic_marker_shopping;
            case "CLTRL":
                return R.drawable.ic_marker_cultural;
            case "OTHR":
                return R.drawable.ic_marker_other;
            default:
                return R.drawable.ic_marker_ship;
        }
    }


    private int getCategoryIcon(String categoryId){
        switch(categoryId)
        {
            case "BRKFST":
                return R.drawable.ic_breakfast;
            case "LNCH":
                return R.drawable.ic_lunch;
            case "DNNR":
                return R.drawable.ic_dinner;
            case "TRNSFR":
                return R.drawable.ic_transfer;
            case "OVRNGHY":
                return R.drawable.ic_overnight;
            case "EXCRSN":
                return R.drawable.ic_excursion;
            case "SHPPNG":
                return R.drawable.ic_shopping;
            case "CLTRL":
                return R.drawable.ic_cultural;
            case "OTHR":
                return R.drawable.ic_other;
            default:
                return R.drawable.ic_world;
        }
    }
}
