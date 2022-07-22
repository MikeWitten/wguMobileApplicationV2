package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Mentor;

@Dao
public interface MentorDAO {

    @Query("SELECT * FROM mentors")
    List<Mentor> getAllMentors();

    @Query("DELETE FROM mentors")
    void deleteAllRows();

    @Insert
    void insertAll(Mentor... mentors);

    @Update
    void update(Mentor mentor);

    @Delete
    void delete(Mentor mentor);
}
