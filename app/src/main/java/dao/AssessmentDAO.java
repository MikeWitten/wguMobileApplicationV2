package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Assessment;

@Dao
public interface AssessmentDAO {

    @Query("SELECT * FROM assessments")
    List<Assessment> getAllAssessments();

    @Query("DELETE FROM assessments")
    void deleteAllRows();

    @Insert
    void insertAll(Assessment... assessments);

    @Update
    void update(Assessment assessment);

    @Delete
    void delete(Assessment assessment);
}
