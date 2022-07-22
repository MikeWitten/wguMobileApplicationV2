package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entities.Term;

@Dao
public interface TermDAO {
    @Query("SELECT * FROM terms")
    List<Term> getAllTerms();

    @Query("DELETE FROM terms")
    void deleteAllRows();

    @Insert
    void insertAll(Term... terms);

    @Update
    void update(Term term);

    @Delete
    void delete(Term term);
}
