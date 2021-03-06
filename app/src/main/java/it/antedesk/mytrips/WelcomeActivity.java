package it.antedesk.mytrips;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.ui.fragment.DiaryFragment;
import it.antedesk.mytrips.ui.fragment.GeneralStatsFragment;
import it.antedesk.mytrips.ui.fragment.adapter.SectionsPagerAdapter;

import static it.antedesk.mytrips.utils.Constants.IS_PLAN;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.nested_scrollview)
    NestedScrollView scrollView;
    @BindView (R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    ViewPager mViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.fab_add_diary)
    FloatingActionButton fabDiary;
    @BindView(R.id.fab_add_plan)
    FloatingActionButton fabPlan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        toolbar.setLogo(R.drawable.ic_white_ship);
        // trick to make scrollview working with tabbeView
        scrollView.setFillViewport (true);

        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setAppBarLayoutElevation(10);
                    fabDiary.show();
                    //fabPlan.hide(); //future release
                } else {
                    setAppBarLayoutElevation(0);
                    fabDiary.hide();
                    //fabPlan.show(); //future release
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupFABs();
    }

    private void setAppBarLayoutElevation(float value){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", value));
            appBarLayout.setStateListAnimator(stateListAnimator);
        }
    }

/*
    // Future release
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    // Future release
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
*/
    private void setupFABs(){
        Intent intent = new Intent(this, AddDiaryActivity.class);
        fabDiary.setOnClickListener((View view) -> {
            intent.putExtra(IS_PLAN, false);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                startActivity(intent, bundle);
            } else
                startActivity(intent);
        });

        /*
        //future release
        fabPlan.setOnClickListener((View view) -> {
            intent.putExtra(IS_PLAN, true);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
                startActivity(intent, bundle);
            } else
                startActivity(intent);
        });
        */
    }

    /**
     * Allows to set up the tabs and titles
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(DiaryFragment.newInstance(false), getString(R.string.tab_diaries));
        //adapter.addFrag(DiaryFragment.newInstance(true), getString(R.string.tab_plans)); //future release
        adapter.addFrag(GeneralStatsFragment.newInstance(), getString(R.string.tab_my_stats));

        viewPager.setAdapter(adapter);
    }
}
