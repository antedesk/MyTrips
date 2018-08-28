package it.antedesk.mytrips.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.minimal.CategoryBudget;
import it.antedesk.mytrips.model.minimal.CategoryTotalInfo;
import it.antedesk.mytrips.model.minimal.DailyBudget;
import it.antedesk.mytrips.model.minimal.DatesInfo;
import it.antedesk.mytrips.viewmodel.DiaryStatisticsViewModel;
import it.antedesk.mytrips.viewmodel.GeneralStatsViewModel;

import static it.antedesk.mytrips.utils.Constants.SELECTED_DIARY_ID;

public class GeneralStatsFragment extends Fragment {

    @BindView(R.id.note_pie_chart)
    PieChart mPieChart;

    @BindView(R.id.total_days_tv)
    TextView mTotalDayTv;
    @BindView(R.id.activities_stats_tv)
    TextView mTotalActivitiesTv;
    @BindView(R.id.total_cities_tv)
    TextView mTotalCitiesTv;
    @BindView(R.id.total_checkins_tv)
    TextView mTotalCheckinsTv;
    @BindView(R.id.total_countries_tv)
    TextView mTotalCountriesTv;
    @BindView(R.id.total_diaries_tv)
    TextView mTotalDiariesTv;
    @BindView(R.id.no_content_piechart)
    TextView mNoContentPieTv;

    private GeneralStatsViewModel dataViewModel;

    public GeneralStatsFragment() {
    }

    /**
     * Returns a new instance of this fragment
     */
    public static GeneralStatsFragment newInstance() {
        return new GeneralStatsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_general_stats, container, false);
        ButterKnife.bind(this, rootView);

        dataViewModel = ViewModelProviders.of(this).get(GeneralStatsViewModel.class);

        setPieChart(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        setMainStats();
    }

    private void setMainStats() {
        dataViewModel.getTotalDays().observe(this, integer -> mTotalDayTv.setText(String.valueOf(integer)));
        dataViewModel.getTotalActivities().observe(this, integer -> mTotalActivitiesTv.setText(String.valueOf(integer)));
        dataViewModel.getTotalCheckIn().observe(this, integer -> mTotalCheckinsTv.setText(String.valueOf(integer)));
        dataViewModel.getTotalCities().observe(this, integer -> mTotalCitiesTv.setText(String.valueOf(integer)));
        dataViewModel.getTotalCountries().observe(this, integer -> mTotalCountriesTv.setText(String.valueOf(integer)));
        dataViewModel.getTotalDiaries().observe(this, integer -> mTotalDiariesTv.setText(String.valueOf(integer)));
    }

    private void setPieChart(View rootView) {

        dataViewModel.getActivitiesDistribution().observe(this, categoryTotalInfos -> {

            if (categoryTotalInfos != null && !categoryTotalInfos.isEmpty())
                mPieChart.setData(generatePieData(categoryTotalInfos));
            else {
                mPieChart.setVisibility(View.GONE);
                mNoContentPieTv.setVisibility(View.VISIBLE);
            }
        });

        mPieChart.getDescription().setEnabled(false);

        // radius of the center hole in percent of maximum radius
        mPieChart.setHoleRadius(30f);
        mPieChart.setTransparentCircleRadius(0f);

        Legend l = mPieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        mPieChart.setExtraBottomOffset(10f);
        mPieChart.setEntryLabelColor(R.color.dark_gray);
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    protected PieData generatePieData(List<CategoryTotalInfo> categoryTotalInfos) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (CategoryTotalInfo categoryTotalInfo : categoryTotalInfos) {
            pieEntries.add(new PieEntry((float) categoryTotalInfo.getTotal(), categoryTotalInfo.getCategory()));
        }

        PieDataSet data = new PieDataSet(pieEntries, "");
        data.setColors(createPieChartColors());
        data.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        data.setValueTextSize(11f);
        data.setSliceSpace(2f);
        data.setValueTextColor(R.color.dark_gray);
        data.setValueTextSize(15f);

        return new PieData(data);
    }

    private ArrayList<Integer> createPieChartColors() {

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        return colors;
    }

}
