package it.antedesk.mytrips.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "images",
        indices = { @Index(value = {"id"}, unique = true) },
        foreignKeys = {@ForeignKey(entity = Note.class,
                parentColumns = "id",
                childColumns = "note_id",
                onDelete = CASCADE)})
public class Image {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "note_id")
    private int noteId;
    private String name;
    private String path;
    @ColumnInfo(name = "is_cover")
    private boolean isCover;

    public Image(int id, int noteId, String name, String path, boolean isCover) {
        this.id = id;
        this.noteId = noteId;
        this.name = name;
        this.path = path;
        this.isCover = isCover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isCover() {
        return isCover;
    }

    public void setCover(boolean isCover) {
        this.isCover = isCover;
    }
}
