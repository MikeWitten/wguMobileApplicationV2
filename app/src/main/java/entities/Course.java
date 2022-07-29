package entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import database.Converters;

@Entity (tableName = "courses")
public class Course implements Parcelable {



    @PrimaryKey (autoGenerate = true)
    public Integer courseID;
    @ColumnInfo
    public Integer termID;
    @ColumnInfo
    public Integer mentorID;
    @ColumnInfo
    public String courseName;
    @ColumnInfo
    public LocalDate startDate;
    @ColumnInfo
    public LocalDate endDate;
    @ColumnInfo
    public CourseStatus status;

    public Course(Integer courseID, int termID, int mentorID,
                  String courseName, LocalDate startDate, LocalDate endDate, CourseStatus status) {
        this.courseID = courseID;
        this.termID = termID;
        this.mentorID = mentorID;
        this.courseName = courseName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }


    protected Course(Parcel in) {
        if (in.readByte() == 0) {
            courseID = null;
        } else {
            courseID = in.readInt();
        }
        if (in.readByte() == 0) {
            termID = null;
        } else {
            termID = in.readInt();
        }
        if (in.readByte() == 0) {
            mentorID = null;
        } else {
            mentorID = in.readInt();
        }
        courseName = in.readString();
        startDate = LocalDate.parse(in.readString());
        endDate = LocalDate.parse(in.readString());
        status = Converters.fromStatusString(in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        if (courseID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(courseID);
        }
        if (termID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(termID);
        }
        if (mentorID == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(mentorID);
        }
        //Convert non-parcelable parameters into strings so that they can be parsed.
        dest.writeString(courseName);
        dest.writeString(startDate.format(formatter));
        dest.writeString(endDate.format(formatter));
        dest.writeString(Converters.fromCourseStatus(status));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public Integer getCourseID() {
        return courseID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @NonNull
    @Override
    public String toString() {
        return this.courseName;
    }
}
