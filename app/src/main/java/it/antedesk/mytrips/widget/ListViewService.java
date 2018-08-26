package it.antedesk.mytrips.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.DateFormat;
import java.util.List;

import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.Diary;
import it.antedesk.mytrips.ui.fragment.DiaryFragment;

public class ListViewService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Diary> diaries;

    public ListRemoteViewsFactory(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        diaries = DiaryFragment.mDiaries;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return diaries == null ? 0 : diaries.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(diaries == null) return null;
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.diary_item_widget);
        remoteViews.setTextViewText(R.id.widget_diary_title_tv, diaries.get(position).getName());
        remoteViews.setTextViewText(R.id.widget_diary_category_tv, diaries.get(position).getCategory());
        remoteViews.setTextViewText(R.id.widget_diary_description_tv, diaries.get(position).getDescription());
        DateFormat dateFormat = DateFormat.getDateInstance();
        remoteViews.setTextViewText(R.id.widget_diary_date_tv, dateFormat.format(diaries.get(position).getStartDate()));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}

