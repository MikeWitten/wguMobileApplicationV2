package entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity (tableName = "courses")
public class Course {


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


    public Integer getCourseID() {
        return courseID;
    }

    public void setCourseID(Integer courseID) {
        this.courseID = courseID;
    }

    public Integer getTermID() {
        return termID;
    }

    public void setTermID(Integer termID) {
        this.termID = termID;
    }

    public Integer getMentorID() {
        return mentorID;
    }

    public void setMentorID(Integer mentorID) {
        this.mentorID = mentorID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return this.courseName;
    }
}
