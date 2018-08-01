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
import it.antedesk.mytrips.adapter.DiaryViewAdapter;
import it.antedesk.mytrips.model.Diary;
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
        private boolean isPlan;

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

            return rootView;
        }

        @Override
        public void onClick(Diary selectedDiary) {
            Snackbar.make(getActivity().findViewById(R.id.diaries_recycler_view),"Selected "+selectedDiary.getName(), Snackbar.LENGTH_LONG).show();

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

        @Override
        public void onResume() {
            super.onResume();
            if (getArguments() != null) {
                retrieveDiariesOrPlans(getArguments().getBoolean(IS_A_PLAN));
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
