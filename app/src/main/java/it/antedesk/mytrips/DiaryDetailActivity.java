package it.antedesk.mytrips;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.ui.adapter.NoteViewAdapter;
import it.antedesk.mytrips.ui.fragment.adapter.SectionsPagerAdapter;
import it.antedesk.mytrips.viewmodel.LoadDiariesViewModel;
import it.antedesk.mytrips.viewmodel.LoadDiaryDataViewModel;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static it.antedesk.mytrips.utils.SupportVariablesDefinition.SELECTED_DIARY;
import static it.antedesk.mytrips.utils.SupportVariablesDefinition.SELECTED_DIARY_ID;

public class DiaryDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.fab)
    FloatingActionButton fabAddNote;

    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        // checking if it is null, if so close the activity
        if (intent == null) {
            Snackbar.make(findViewById(R.id.form_scroller), getString(R.string.general_error), Snackbar.LENGTH_LONG).show();
            finish();
        }

        Diary diary = intent != null ? intent.getParcelableExtra(SELECTED_DIARY) : null;
        if (diary == null) {
            snackBarMessage(R.string.general_error);
            finish();
        }
        toolbar.setTitle(diary.getName());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fabAddNote.show();
                } else {
                    fabAddNote.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(mViewPager);

        tabLayout.setupWithViewPager(mViewPager);
        fabAddNote.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        toRemove(diary.getId());

    }

    private void toRemove(long diaryId) {
        MutableLiveData<Long> id = new MutableLiveData<>();

        CheckIn checkIn = new CheckIn(
                41.9027835,
                12.4963655,
                "vattela a pesta, 45",
                "Roma",
                "Italia"
        );
        Note note = new Note(
                "TONNARELLO!",
                "description",
                Calendar.getInstance().getTime(),
                "Dinner",
                40,
                "EUR",
                0,
                "sun",
                30
        );
        Note note1 = new Note(
                "test2",
                "description long long long long long long long tooooooooo long",
                Calendar.getInstance().getTime(),
                "Dinner",
                40,
                "EUR",
                0,
                "sun",
                30
        );
        note.setDiaryId(diaryId);
        note1.setDiaryId(diaryId);
        AppDatabase mDb = AppDatabase.getsInstance(getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long checkinid = mDb.getCheckInDao().insert(checkIn);
                note.setCheckInId(checkinid);
                mDb.getNoteDao().insert(note);
            }
        });
    }

    private void snackBarMessage(int messageId) {
        Snackbar.make(findViewById(R.id.fab), getString(messageId), Snackbar.LENGTH_LONG).show();
    }

    /**
     * Allows to set up the tabs and titles
     *
     * @param mViewPager
     */
    private void setupViewPager(ViewPager mViewPager) {
        mSectionsPagerAdapter.addFrag(NotesFragment.newInstance(0), getString(R.string.tab_notes));
        mSectionsPagerAdapter.addFrag(PlaceholderFragment.newInstance(1), getString(R.string.checkins));
        mSectionsPagerAdapter.addFrag(PlaceholderFragment.newInstance(2), getString(R.string.stats));
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_diary_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_modify) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class NotesFragment extends Fragment implements NoteViewAdapter.NoteViewAdapterOnClickHandler {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private NoteViewAdapter noteApter;

        public NotesFragment() {
        }

        //TODO pass the diary id to the fragment!
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static NotesFragment newInstance(int sectionNumber) {
            NotesFragment fragment = new NotesFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_diary_detail_notes_list, container, false);

            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            int portraitColumns = tabletSize ? 2 : 1;
            int landscapeColumns = tabletSize ? 3 : calculateNoOfColumns(Objects.requireNonNull(getActivity()).getApplicationContext());
            int numberOfColumns =
                    getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT ? portraitColumns : landscapeColumns;

            // creating a GridLayoutManager
            GridLayoutManager mLayoutManager
                    = new GridLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(), numberOfColumns);

            RecyclerView recyclerView = rootView.findViewById(R.id.notes_recycler_view);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setHasFixedSize(true);

            noteApter = new NoteViewAdapter(this);
            recyclerView.setAdapter(noteApter);

            return rootView;
        }

        /**
         * Calculates the number of columns for the gridlayout
         *
         * @param context
         * @return
         */
        public static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int scalingFactor = 180;
            int noOfCol = (int) (dpWidth / scalingFactor);
            noOfCol = noOfCol >= 3 ? 2 : 1;
            return noOfCol;
        }


        private void retrieveDiaryNotes(final int diaryId) {
            LoadDiaryDataViewModel dataViewModel = ViewModelProviders.of(this).get(LoadDiaryDataViewModel.class);
            dataViewModel.getDiaryNotes(diaryId).observe(this, (List<Note> notes) -> {
                if (notes != null)
                    noteApter.setNotesData(notes);
            });
        }

        @Override
        public void onResume() {
            super.onResume();
            if (getArguments() != null) {
                retrieveDiaryNotes(getArguments().getInt(SELECTED_DIARY_ID));
            }
        }

        @Override
        public void onClick(Note selectedNote) {

        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_diary_detail, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }

        /**
         * Calculates the number of columns for the gridlayout
         *
         * @param context
         * @return
         */
        public static int calculateNoOfColumns(Context context) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int scalingFactor = 180;
            int noOfCol = (int) (dpWidth / scalingFactor);
            noOfCol = noOfCol >= 3 ? 2 : 1;
            return noOfCol;
        }
    }

}
