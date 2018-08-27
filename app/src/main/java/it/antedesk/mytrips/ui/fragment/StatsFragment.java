package it.antedesk.mytrips.ui.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.R;

import it.antedesk.mytrips.model.minimal.CategoryBudget;
import it.antedesk.mytrips.model.minimal.DailyBudget;
import it.antedesk.mytrips.model.minimal.DatesInfo;
import it.antedesk.mytrips.viewmodel.DiaryStatisticsViewModel;

import static it.antedesk.mytrips.utils.Constants.SELECTED_DIARY_ID;

public class StatsFragment extends Fragment {

    @BindView(R.id.note_pie_chart)
    PieChart mPieChart;
    @BindView(R.id.note_line_chart)
    LineChart mLineChart;

    @BindView(R.id.current_vs_total_days_tv)
    TextView mCurrentVsTotalDayTv;
    @BindView(R.id.total_notes_tv)
    TextView mTotalNotestv;
    @BindView(R.id.total_checkins_tv)
    TextView mTotalCheckinsTv;
    @BindView(R.id.current_budget_tv)
    TextView mCurrentBudgetTv;
    @BindView(R.id.total_budget_tv)
    TextView mTotalBudgetTv;
    @BindView(R.id.average_budget_tv)
    TextView mAverageBudgeTv;
    @BindView(R.id.no_content_piechart)
    TextView mNoContentPieTv;
    @BindView(R.id.no_content_linechart)
    TextView mNoContentLineTv;

    private DiaryStatisticsViewModel dataViewModel;

    public StatsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StatsFragment newInstance(long diaryId) {
        StatsFragment fragment = new StatsFragment();
        Bundle args = new Bundle();
        args.putLong(SELECTED_DIARY_ID, diaryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stats, container, false);
        ButterKnife.bind(this, rootView);

        dataViewModel = ViewModelProviders.of(this).get(DiaryStatisticsViewModel.class);

        setPieChart(rootView);
        setLineChart(rootView);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            setMainStats(getArguments().getLong(SELECTED_DIARY_ID));
        }
    }

    private void setMainStats(final long diaryId) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumFractionDigits(0);
        formatter.setMaximumFractionDigits(2);

        dataViewModel.getAVGBudgetByDiaryId(diaryId).observe(this, dailyBudget -> {
            mAverageBudgeTv.setText(TextUtils.concat(formatter.format(dailyBudget.getBudget()), " ",
                    dailyBudget.getCurrency() != null ? dailyBudget.getCurrency() : ""));
        });

        dataViewModel.getTotalNotesByDiaryId(diaryId).observe(this, integer -> mTotalNotestv.setText(String.valueOf(integer)));

        dataViewModel.getTotalCheckinsByDiaryId(diaryId).observe(this, integer -> mTotalCheckinsTv.setText(String.valueOf(integer)));

        dataViewModel.getTotalBudgetByDiaryId(diaryId).observe(this, aDouble ->
                mCurrentBudgetTv.setText(aDouble != null ? formatter.format(aDouble) : "0"));

        dataViewModel.getDiaryBudgetByDiaryId(diaryId).observe(this, budgetInfo ->
                mTotalBudgetTv.setText(TextUtils.concat("/", formatter.format(budgetInfo.getTotalBudget()),
                        budgetInfo.getCurrency() != null ? " " + budgetInfo.getCurrency() : ""))
        );

        dataViewModel.getDatesInfoByDiaryId(diaryId).observe(this, (DatesInfo datesInfo) ->
                mCurrentVsTotalDayTv.setText(TextUtils.concat(String.valueOf(datesInfo.getCurrentDays()),
                        "/", String.valueOf(datesInfo.getTotalDays()))));
    }

    private void setPieChart(View rootView) {
        dataViewModel.getTotalBudgetByCategoriesAndDiaryId(getArguments().getLong(SELECTED_DIARY_ID)).observe(this,
                categoryBudgets -> {
                    if (categoryBudgets != null && !categoryBudgets.isEmpty())
                        mPieChart.setData(generatePieData(categoryBudgets));
                    else {
                        mPieChart.setVisibility(View.GONE);
                        mNoContentPieTv.setVisibility(View.VISIBLE);
                    }
                }
        );

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

    protected PieData generatePieData(List<CategoryBudget> categoryBudgets) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        for (CategoryBudget categoryBudget : categoryBudgets) {
            pieEntries.add(new PieEntry((float) categoryBudget.getBudget(), categoryBudget.getCategory()));
        }

        PieDataSet data = new PieDataSet(pieEntries, "");
        data.setColors(createPieChartColors());
        data.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        data.setValueTextSize(11f);
        data.setSliceSpace(2f);
        data.setValueTextColor(R.color.dark_gray);
        data.setValueTextSize(15f);

        PieData d = new PieData(data);

        return d;
    }

    private ArrayList<Integer> createPieChartColors() {

        ArrayList<Integer> colors = new ArrayList<Integer>();

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

    private void setLineChart(View rootView){

        dataViewModel.getTotalBudgetByDayAndDiaryId(getArguments().getLong(SELECTED_DIARY_ID)).observe(this,
                dailyBudgets -> {
                    if (dailyBudgets != null && !dailyBudgets.isEmpty()) {
                        mLineChart.setData(generateLineData(dailyBudgets));
                        // the labels that should be drawn on the XAxis
                        DateFormat dateFormatter = SimpleDateFormat.getDateInstance();
                        final String[] dates = new String[dailyBudgets.size()];
                        for (int i = 0; i < dates.length; i++) {
                            dates[i] = dateFormatter.format(dailyBudgets.get(i).getDateTime());
                        }
                        XAxis xAxis = mLineChart.getXAxis();
                        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1

                        if (dates.length > 0) {
                            IAxisValueFormatter formatter = (float value, AxisBase axis) ->
                            {
                                String label = "";
                                    try{
                                        label = dates[(int) value];
                                    }catch (Exception e){
                                    }
                                    return label;
                            };
                            xAxis.setValueFormatter(formatter);
                        }
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setLabelRotationAngle(-75);
                    } else {
                        mLineChart.setVisibility(View.GONE);
                        mNoContentLineTv.setVisibility(View.VISIBLE);
                    }
                }
        );

        mLineChart.getDescription().setEnabled(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.animateX(500);
        mLineChart.setExtraBottomOffset(10);

        //Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Light.ttf");

        Legend l = mLineChart.getLegend();
        //l.setTypeface(tf);

        YAxis leftAxis = mLineChart.getAxisLeft();
        //leftAxis.setTypeface(tf);

        mLineChart.getAxisRight().setEnabled(false);
    }

    protected LineData generateLineData(List<DailyBudget> dailyBudgets) {

        ArrayList<ILineDataSet> sets = new ArrayList<>();

        List<Entry> entries = new ArrayList<>();
        for (DailyBudget data : dailyBudgets) {
            entries.add(new Entry(dailyBudgets.indexOf(data), (float) data.getBudget()));
        }

        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.daily_trend_budget));
        dataSet.setLineWidth(2f);
        dataSet.setDrawCircles(false);
        dataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        sets.add(dataSet);

        LineData d = new LineData(sets);
        return d;
    }

}
