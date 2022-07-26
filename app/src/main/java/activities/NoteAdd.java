package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wittenPortfolio.R;

import java.util.Objects;

import database.AppDatabase;
import entities.Course;
import entities.Note;

public class NoteAdd extends AppCompatActivity {
    Course course;
    EditText title;
    EditText content;
    Button save;
    Button cancel;
    String titleString;
    String contentString;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_add);
        //Enable the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get object from intent
        course = getIntent().getExtras().getParcelable("class");
        //Get an instance of the database
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        //Set up cancel button
        cancelBTN();
        //Set up save button
        saveBTN();

    }

    private void saveBTN() {
        save = findViewById(R.id.saveNoteBTN);
        save.setOnClickListener(v -> {
            content = findViewById(R.id.noteContentET);
            title = findViewById(R.id.noteTitleET);
            if(title.getText().toString().isEmpty()){
                titleString = "Note that must not be named";
            }
            else titleString = title.getText().toString();
            if(content.getText().toString().isEmpty()){
                Toast.makeText(NoteAdd.this, "Please add a note.... to your .... note.", Toast.LENGTH_SHORT).show();
                return;
            }else contentString = content.getText().toString();
            //Create a new Note object for this course.
            Integer courseID = course.courseID;
            Note newNote = new Note(null, courseID, titleString, contentString);
            db.noteDAO().insertAll(newNote);
            Toast.makeText(NoteAdd.this, "Note Added.", Toast.LENGTH_SHORT).show();
            //Send user back to the course details page
            Intent intent = new Intent(NoteAdd.this, CourseDetail.class);
            intent.putExtra("class", course);
            startActivity(intent);
        });
    }

    private void cancelBTN() {
        cancel = findViewById(R.id.cancelNote);
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(NoteAdd.this, CourseDetail.class);
            intent.putExtra("class", course);
            startActivity(intent);
        });
    }
}