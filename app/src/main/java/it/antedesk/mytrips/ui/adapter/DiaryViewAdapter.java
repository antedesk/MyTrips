package it.antedesk.mytrips.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.Diary;

public class DiaryViewAdapter extends RecyclerView.Adapter<DiaryViewAdapter.DiaryAdapterViewHolder> {
    private List<Diary> diaryList;

    private final DiaryViewAdapterOnClickHandler mClickHandler;

    @NonNull
    @Override
    public DiaryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context parentContex = parent.getContext();
        int layoutIdForListItem = R.layout.diary_item;
        LayoutInflater inflater = LayoutInflater.from(parentContex);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new DiaryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryAdapterViewHolder holder, int position) {
        Diary diary = diaryList.get(position);
        Log.d(DiaryAdapterViewHolder.class.getName(), diary.toString());
        holder.mDiaryTitle.setText(diary.getName());

        DateFormat dateFormat = DateFormat.getDateInstance();

        holder.mDiaryDate.setText(dateFormat.format(diary.getStartDate()));
        holder.mDiaryDescription.setText(diary.getDescription());
        holder.mDiaryCategory.setText(String.valueOf(diary.getCategory()));
    }

    @Override
    public int getItemCount() {
        if (diaryList == null || diaryList.isEmpty()) return 0;
        return diaryList.size();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface DiaryViewAdapterOnClickHandler {
        void onClick(Diary selectedDiary);
    }

    public DiaryViewAdapter(DiaryViewAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class DiaryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.diary_title_tv)
        TextView mDiaryTitle;
        @BindView(R.id.diary_category_tv)
        TextView mDiaryCategory;
        @BindView(R.id.diary_date_tv)
        TextView mDiaryDate;
        @BindView(R.id.diary_description_tv)
        TextView mDiaryDescription;


        public DiaryAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Diary diary = diaryList.get(adapterPosition);
            mClickHandler.onClick(diary);
        }
    }

    public void setDiarysData(List<Diary> data) {
        diaryList = data;
        notifyDataSetChanged();
    }
}