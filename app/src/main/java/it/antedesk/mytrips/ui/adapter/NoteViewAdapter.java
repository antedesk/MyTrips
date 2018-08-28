package it.antedesk.mytrips.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.antedesk.mytrips.R;
import it.antedesk.mytrips.model.ImageInfo;
import it.antedesk.mytrips.model.Note;

public class NoteViewAdapter extends RecyclerView.Adapter<NoteViewAdapter.NoteAdapterViewHolder> {
    private List<Note> noteList;
    private Context parentContex;

    private final NoteViewAdapterOnClickHandler mClickHandler;

    private String BREAKFAST;
    private String LUNCH;
    private String DINNER;
    private String TRANSFER;
    private String OVERNIGHT_STAY;
    private String EXCURSION;
    private String SHOPPING;
    private String CULTURAL_VISIT;
    private String OTHER;

    @NonNull
    @Override
    public NoteAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentContex = parent.getContext();
        int layoutIdForListItem = R.layout.note_item;
        LayoutInflater inflater = LayoutInflater.from(parentContex);

        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new NoteAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapterViewHolder holder, int position) {

        BREAKFAST      = parentContex.getString(R.string.note_category_breakfast);
        LUNCH          = parentContex.getString(R.string.note_category_lunch);
        DINNER         = parentContex.getString(R.string.note_category_dinner);
        TRANSFER       = parentContex.getString(R.string.note_category_transfer);
        OVERNIGHT_STAY = parentContex.getString(R.string.note_category_overnight_stay);
        EXCURSION      = parentContex.getString(R.string.note_category_excursion);
        SHOPPING       = parentContex.getString(R.string.note_category_shopping);
        CULTURAL_VISIT = parentContex.getString(R.string.note_category_excursion);
        OTHER          = parentContex.getString(R.string.note_category_other);

        Note note = noteList.get(position);
        Log.d(NoteAdapterViewHolder.class.getName(), note.toString());
        holder.mNoteTitle.setText(note.getTitle());

        DateFormat dateFormat = DateFormat.getDateInstance();
        holder.mNoteDate.setText(dateFormat.format(note.getDateTime()));
        holder.mNoteDescription.setText(note.getDescription());

        // get category icon
        Picasso.get()
                .load(getCategoryIcon(note.getCategoryId()))
                .placeholder(R.drawable.ic_world)
                .error(R.drawable.ic_world)
                .into(holder.mNoteActivity);
        Picasso.get()
                .load(getCoverImage(note.getCategoryId()))
                .placeholder(R.drawable.home01)
                .error(R.drawable.home01)
                .into(holder.mNoteCoverPhoto);
    }


    private int getCategoryIcon(String categoryId){
        switch(categoryId)
        {
            case "BRKFST":
                return R.drawable.ic_breakfast;
            case "LNCH":
                return R.drawable.ic_lunch;
            case "DNNR":
                return R.drawable.ic_dinner;
            case "TRNSFR":
                return R.drawable.ic_transfer;
            case "OVRNGHY":
                return R.drawable.ic_overnight;
            case "EXCRSN":
                return R.drawable.ic_excursion;
            case "SHPPNG":
                return R.drawable.ic_shopping;
            case "CLTRL":
                return R.drawable.ic_cultural;
            case "OTHR":
                return R.drawable.ic_other;
            default:
                return R.drawable.ic_world;
        }
    }

    private int getCoverImage(String categoryId){
        switch(categoryId)
        {
            case "BRKFST":
                return R.drawable.breakfast01;
            case "LNCH":
                return R.drawable.lunch01;
            case "DNNR":
                return R.drawable.dinner01;
            case "TRNSFR":
                return R.drawable.transfert01;
            case "OVRNGHY":
                return R.drawable.overnight01;
            case "EXCRSN":
                return R.drawable.excurtion01;
            case "SHPPNG":
                return R.drawable.shopping01;
            case "CLTRL":
                return R.drawable.cultural01;
            case "OTHR":
                return R.drawable.other01;
            default:
                return R.drawable.home01;
        }
    }

    @Override
    public int getItemCount() {
        if (noteList == null || noteList.isEmpty()) return 0;
        return noteList.size();
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface NoteViewAdapterOnClickHandler {
        void onClick(Note selectedNote);
    }

    public NoteViewAdapter(NoteViewAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public class NoteAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.note_title_tv)
        TextView mNoteTitle;
        @BindView(R.id.note_activity_icon)
        ImageView mNoteActivity;
        @BindView(R.id.note_cover_photo)
        ImageView mNoteCoverPhoto;
        @BindView(R.id.note_date_tv)
        TextView mNoteDate;
        @BindView(R.id.note_description_tv)
        TextView mNoteDescription;

        public NoteAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Note note = noteList.get(adapterPosition);
            mClickHandler.onClick(note);
        }
    }

    public void setNotesData(List<Note> data) {
        noteList = data;
        notifyDataSetChanged();
    }
}