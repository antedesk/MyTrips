package it.antedesk.mytrips;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.ui.fragment.CheckInsFragment;
import it.antedesk.mytrips.ui.fragment.NotesFragment;
import it.antedesk.mytrips.ui.fragment.StatsFragment;
import it.antedesk.mytrips.ui.fragment.adapter.SectionsPagerAdapter;

import static it.antedesk.mytrips.utils.Constants.SELECTED_DIARY;
import static it.antedesk.mytrips.utils.Constants.SELECTED_DIARY_ID;

public class DiaryDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

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
            snackBarMessage();
            finish();
        }
        toolbar.setTitle(diary != null ? diary.getName() : "");
        diaryId = diary.getId();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setAppBarLayoutElevation(10);
                        fabAddNote.show();
                        break;
                    case 1:
                        setAppBarLayoutElevation(10);
                        fabAddNote.hide();
                        break;
                    default:
                        setAppBarLayoutElevation(0);
                        fabAddNote.hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(mViewPager);

        tabLayout.setupWithViewPager(mViewPager);

        Intent addNoteIntent = new Intent(this, AddNoteActivity.class);
        fabAddNote.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            addNoteIntent.putExtra(SELECTED_DIARY_ID, diaryId);
            startActivity(addNoteIntent);
        });
    }

    private void setAppBarLayoutElevation(float elevation){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", elevation));
            appBarLayout.setStateListAnimator(stateListAnimator);
        }
    }

    private void snackBarMessage() {
        Snackbar.make(findViewById(R.id.fab), getString(R.string.general_error), Snackbar.LENGTH_LONG).show();
    }

    /**
     * Allows to set up the tabs and titles
     */
    private void setupViewPager(ViewPager mViewPager) {
        mSectionsPagerAdapter.addFrag(NotesFragment.newInstance(diaryId), getString(R.string.tab_notes));
        mSectionsPagerAdapter.addFrag(CheckInsFragment.newInstance(diaryId), getString(R.string.checkins));
        mSectionsPagerAdapter.addFrag(StatsFragment.newInstance(diaryId), getString(R.string.stats));
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
/*
    // Future release
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_diary_detail, menu);
        return true;
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_update) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
