package entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "notes")
public class Note implements Parcelable {
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


    protected Note(Parcel in) {
        if (in.readByte() == 0) {
            noteID = null;
        } else {
            noteID = in.readInt();
        }
        if (in.readByte() == 0) {
            courseID = null;
        } else {
            courseID = in.readInt();
        }
        title = in.readString();
        note = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (noteID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(noteID);
        }
        if (courseID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(courseID);
        }
        dest.writeString(title);
        dest.writeString(note);
    }
}
