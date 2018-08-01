package it.antedesk.mytrips.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.ui.adapter.DiaryViewAdapter;
import it.antedesk.mytrips.viewmodel.LoadDiariesViewModel;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * A placeholder fragment containing a simple view.
 */
public class DiaryFragment extends Fragment implements DiaryViewAdapter.DiaryViewAdapterOnClickHandler {
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