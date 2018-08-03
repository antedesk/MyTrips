package it.antedesk.mytrips.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.Note;
import it.antedesk.mytrips.ui.adapter.NoteViewAdapter;
import it.antedesk.mytrips.viewmodel.LoadDiaryDataViewModel;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;
import static it.antedesk.mytrips.utils.SupportVariablesDefinition.SELECTED_DIARY_ID;

public class NotesFragment extends Fragment implements NoteViewAdapter.NoteViewAdapterOnClickHandler {

    private NoteViewAdapter noteApter;

    public NotesFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static NotesFragment newInstance(long diaryId) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putLong(SELECTED_DIARY_ID, diaryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diary_detail_notes_list, container, false);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        int portraitColumns = tabletSize ? 2 : 1;
        int landscapeColumns = tabletSize ? 3 : calculateNoOfColumns(Objects.requireNonNull(getActivity()).getApplicationContext());
        int numberOfColumns =
                getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT ? portraitColumns : landscapeColumns;

        // creating a GridLayoutManager
        GridLayoutManager mLayoutManager
                = new GridLayoutManager(Objects.requireNonNull(getActivity()).getApplicationContext(), numberOfColumns);

        RecyclerView recyclerView = rootView.findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        noteApter = new NoteViewAdapter(this);
        recyclerView.setAdapter(noteApter);

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


    private void retrieveDiaryNotes(final long diaryId) {
        LoadDiaryDataViewModel dataViewModel = ViewModelProviders.of(this).get(LoadDiaryDataViewModel.class);
        dataViewModel.getDiaryNotes(diaryId).observe(this, (List<Note> notes) -> {
            if (notes != null)
                noteApter.setNotesData(notes);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            retrieveDiaryNotes(getArguments().getLong(SELECTED_DIARY_ID));
        }
    }

    @Override
    public void onClick(Note selectedNote) {

    }
}
