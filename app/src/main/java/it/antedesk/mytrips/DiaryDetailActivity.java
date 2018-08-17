package it.antedesk.mytrips;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.CheckIn;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.ui.fragment.CheckInsFragment;
import it.antedesk.mytrips.ui.fragment.NotesFragment;
import it.antedesk.mytrips.ui.fragment.adapter.SectionsPagerAdapter;

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

    private long diaryId;

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
        diaryId = diary.getId();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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
        //fabAddNote.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //        .setAction("Action", null).show());


        Intent addNoteIntent = new Intent(this, AddNoteActivity.class);
        fabAddNote.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
            addNoteIntent.putExtra(SELECTED_DIARY_ID, diaryId);
            startActivity(addNoteIntent);
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
        mSectionsPagerAdapter.addFrag(NotesFragment.newInstance(diaryId), getString(R.string.tab_notes));
        mSectionsPagerAdapter.addFrag(CheckInsFragment.newInstance(diaryId), getString(R.string.checkins));
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

        if(id == R.id.action_update) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
