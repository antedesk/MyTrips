package it.antedesk.mytrips;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import it.antedesk.mytrips.adapter.DiaryViewAdapter;
import it.antedesk.mytrips.database.AppDatabase;
import it.antedesk.mytrips.database.AppExecutors;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.viewmodel.LoadDiariesViewModel;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class WelcomeActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // trick to make scrollview working with tabbeView
        NestedScrollView scrollView = findViewById (R.id.nested_scrollview);
        scrollView.setFillViewport (true);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
/*
        final Diary d = new Diary("pippo", "my pippo", Calendar.getInstance().getTime(),
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
        //  mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //  tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DiaryFragment extends Fragment implements DiaryViewAdapter.DiaryViewAdapterOnClickHandler {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String IS_A_PLAN = "is_a_plan";

        private DiaryViewAdapter dvApter;

        public DiaryFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DiaryFragment newInstance(boolean isPlane) {
            DiaryFragment fragment = new DiaryFragment();
            Bundle args = new Bundle();
            args.putBoolean(IS_A_PLAN, isPlane);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            textView.setText(getString(R.string.section_format));
            //textView.setText(getString(R.string.section_format, getArguments().getBoolean(IS_A_PLAN)));

            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            int portraitColumns = tabletSize ? 2 : 1;
            int landscapeColumns = tabletSize ? 3 : calculateNoOfColumns(Objects.requireNonNull(getActivity()).getApplicationContext());
            int numberOfColumns =
                    getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT ? portraitColumns : landscapeColumns;

            // creating a GridLayoutManager
            GridLayoutManager mLayoutManager
                    = new GridLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(), numberOfColumns);

            RecyclerView recyclerView = rootView.findViewById(R.id.diaries_recycler_view);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setHasFixedSize(true);

            dvApter = new DiaryViewAdapter(this);
            recyclerView.setAdapter(dvApter);

            if (getArguments() != null) {
                retrieveDiariesOrPlans(getArguments().getBoolean(IS_A_PLAN));
            }

            return rootView;
        }

        @Override
        public void onClick(Diary selectedDiary) {

        }

        private void retrieveDiariesOrPlans(final boolean isPlane){
            LoadDiariesViewModel dataViewModel = ViewModelProviders.of(this).get(LoadDiariesViewModel.class);
            if(isPlane) {
                dataViewModel.getPlans().observe(this, plans -> {
                    if(plans!=null)
                        dvApter.setDiarysData(plans);
                });
            } else {
                dataViewModel.getDiaries().observe(this, diaries -> {
                    if(diaries!=null)
                        dvApter.setDiarysData(diaries);
                });
            }
        }

        /**
         * Calculates the number of columns for the gridlayout
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
