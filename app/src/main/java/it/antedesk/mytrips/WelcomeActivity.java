package it.antedesk.mytrips;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.ui.adapter.DiaryViewAdapter;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.ui.fragment.DiaryFragment;
import it.antedesk.mytrips.ui.fragment.adapter.SectionsPagerAdapter;
import it.antedesk.mytrips.viewmodel.LoadDiariesViewModel;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static it.antedesk.mytrips.utils.SupportVariablesDefinition.IS_PLAN;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.nested_scrollview)
    NestedScrollView scrollView;
    @BindView (R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.fab_add_diary)
    FloatingActionButton fabDiary;
    @BindView(R.id.fab_add_plan)
    FloatingActionButton fabPlan;

    private SectionsPagerAdapter mSectionsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        // trick to make scrollview working with tabbeView
        scrollView.setFillViewport (true);

        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);

        tabLayout.setupWithViewPager(mViewPager);
/*
        final Diary d = new Diary("pippo_0", "my pippo_0", Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime(), 200.59, "EUR", "family", true);
        Log.d(WelcomeActivity.class.getName(), d.getName());

        AppDatabase mDb = AppDatabase.getsInstance(getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.getDiaryDao().insert(d);
            }
        });
*/

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fabDiary.show();
                    fabPlan.hide();
                } else {
                    fabDiary.hide();
                    fabPlan.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        Intent intent = new Intent(this, AddDiaryActivity.class);
        fabDiary.setOnClickListener((View view) -> {
            Snackbar.make(view, "FAB Diary", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            intent.putExtra(IS_PLAN, false);
            startActivity(intent);
        });

        fabPlan.setOnClickListener((View view) -> {
            Snackbar.make(view, "FAB Plan", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            intent.putExtra(IS_PLAN, true);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id)
        {
            case R.id.action_profile:
                return true;
            case R.id.action_stats:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Allows to set up the tabs and titles
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(DiaryFragment.newInstance(false), getString(R.string.tab_diaries));
        adapter.addFrag(DiaryFragment.newInstance(true), getString(R.string.tab_plans));
        viewPager.setAdapter(adapter);
    }
}
