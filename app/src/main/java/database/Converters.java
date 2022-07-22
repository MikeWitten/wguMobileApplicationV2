package database;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import entities.AssessmentType;
import entities.CourseStatus;

public class Converters {

    //Formatter for readability, consistency, data conversion.
    static DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    @TypeConverter
    public static LocalDate fromDateString(String string){
        return LocalDate.parse(string);
    }

    @TypeConverter
    public static String fromLocalDate(LocalDate date){
        return formatter.format(date);
    }

    @TypeConverter
    public static String fromAssessmentType(AssessmentType type){
        switch (type){
            case Project:
                return AssessmentType.Project.name();
            case Paper:
                return AssessmentType.Paper.name();
            case Practical:
                return AssessmentType.Practical.name();
            case Test:
                return AssessmentType.Test.name();
            default:
                return null;
        }
    }

    @TypeConverter
    public static AssessmentType fromAssessmentString(String s){
        switch (s.toLowerCase()){
            case "test":
                return AssessmentType.Test;
            case "paper":
                return AssessmentType.Paper;
            case "project":
                return AssessmentType.Project;
            case "practical":
                return AssessmentType.Practical;
            default:
                return null;
        }
    }

    @TypeConverter
    public static String fromCourseStatus(CourseStatus status){
        return status.name();
    }

    @TypeConverter
    public static CourseStatus fromStatusString(String s){
        switch (s.toLowerCase()){
            case "inprogress":
                return CourseStatus.InProgress;
            case "completed":
                return CourseStatus.Completed;
            case "dropped":
                return CourseStatus.Dropped;
            case "plantotake":
                return CourseStatus.PlanToTake;
            default:
                return null;
        }
    }
}
