package it.antedesk.mytrips.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import it.antedesk.mytrips.DiaryDetailActivity;
import it.antedesk.mytrips.NoteDetailActivity;
import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.ui.adapter.DiaryViewAdapter;
import it.antedesk.mytrips.viewmodel.LoadDiariesViewModel;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static it.antedesk.mytrips.utils.Constants.SELECTED_DIARY;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiaryFragment extends Fragment implements DiaryViewAdapter.DiaryViewAdapterOnClickHandler {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String IS_A_PLAN = "is_a_plan";
    private static final String LIST_STATE = "listState";

    private Parcelable mListState = null;
    private RecyclerView mDiaryRecyclerView;

    private DiaryViewAdapter dvApter;

    public static List<Diary> mDiaries;
    public static long mDiaryId;

    public DiaryFragment() {
    }

    /**
     * Returns a new instance of this fragment
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

        mDiaryRecyclerView = rootView.findViewById(R.id.diaries_recycler_view);
        mDiaryRecyclerView.setLayoutManager(mLayoutManager);
        mDiaryRecyclerView.setHasFixedSize(true);

        if(savedInstanceState!=null
                && savedInstanceState.containsKey(LIST_STATE)) {
            mListState = savedInstanceState.getParcelable(LIST_STATE);
        }


        dvApter = new DiaryViewAdapter(this);
        mDiaryRecyclerView.setAdapter(dvApter);

        return rootView;
    }

    @Override
    public void onClick(Diary selectedDiary) {
        Intent intent = new Intent(getActivity(), DiaryDetailActivity.class);
        intent.putExtra(SELECTED_DIARY, selectedDiary);
        startActivity(intent);
    }

    private void retrieveDiariesOrPlans(final boolean isPlane){
        LoadDiariesViewModel dataViewModel = ViewModelProviders.of(this).get(LoadDiariesViewModel.class);
        if(isPlane) {
            dataViewModel.getPlans().observe(this, plans -> {
                if(plans!=null)
                    dvApter.setDiarysData(plans);
                if (mListState!=null)
                    mDiaryRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            });
        } else {
            dataViewModel.getDiaries().observe(this, diaries -> {
                mDiaries = diaries;
                if(diaries!=null)
                    dvApter.setDiarysData(diaries);
                if (mListState!=null)
                    mDiaryRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            });
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mListState = mDiaryRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(LIST_STATE, mListState);
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