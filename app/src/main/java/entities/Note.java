package entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "notes")
public class Note {
    @PrimaryKey (autoGenerate = true)
    public Integer noteID;
    @ColumnInfo (name = "course_ID")
    public Integer courseID;
    @ColumnInfo (name = "title")
    public String title;
    @ColumnInfo (name = "note")
    public String note;

    public Note(Integer noteID, Integer courseID, String title, String note) {
        this.noteID = noteID;
        this.courseID = courseID;
        this.title = title;
        this.note = note;
    }


    public Integer getNoteID() {
        return noteID;
    }

    public void setNoteID(Integer noteID) {
        this.noteID = noteID;
    }

    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "title='" + title + '\'' +
                '}';
    }
}
