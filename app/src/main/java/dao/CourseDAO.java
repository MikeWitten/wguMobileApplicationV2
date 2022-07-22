package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Course;

@Dao
public interface CourseDAO {
    @Query("SELECT * FROM courses")
    List<Course> getAllCourses();

    @Query("DELETE FROM courses")
    void deleteAllRows();

    @Query("SELECT courseName FROM courses")
    String[] getCoursesArray();

    @Query("SELECT courseID FROM courses WHERE courseName IS :inputName")
    Integer getClassID(String inputName);

    @Insert
    void insertAll(Course... courses);

    @Update
    void update(Course course);

    @Delete
    void delete(Course course);
}
