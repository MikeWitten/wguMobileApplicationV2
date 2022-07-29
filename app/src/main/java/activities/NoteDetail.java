package activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wittenPortfolio.R;

import java.util.List;
import java.util.Objects;

import adapters.AssessmentListAdapter;
import adapters.NoteListAdapter;
import database.AppDatabase;
import entities.Course;
import entities.Note;

/**
 * This class implements a listener to allow the user to select a list item in the recycler view.
 */
public class NoteDetail extends AppCompatActivity implements NoteListAdapter.OnNoteListener {
    private NoteListAdapter noteListAdapter;
    List<Note> notes;
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        //Enable the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //get course object
        course = getIntent().getExtras().getParcelable("class");
        //Get an instance of the database and a full list of 'Assessment" objects.
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        notes = db.noteDAO().getAllNotes(course.courseID);
        //Initialize the recycler view.
        initRecyclerView();
        //Initialize the add note button
        addNoteButton();
        //Initialize the back button
        backButton();
    }

    private void backButton() {
        Button backButton = findViewById(R.id.backToCourseBTN);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetail.this, CourseDetail.class);
                intent.putExtra("class", course);
                startActivity(intent);
            }
        });
    }

    private void addNoteButton() {
        FloatingActionButton addButton = findViewById(R.id.addNoteFloatingBTN);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteDetail.this, NoteAdd.class);
                intent.putExtra("class", course);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView rcView = findViewById(R.id.noteListRecyclerView);
        //Set the layout manager.
        rcView.setLayoutManager(new LinearLayoutManager(this));
        //Create a visual divider to separate the assessment objects.
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcView.addItemDecoration(decoration);
        //Initialize the adapter.
        noteListAdapter = new NoteListAdapter(this, notes, this);
        rcView.setAdapter(noteListAdapter);
    }

    @Override
    public void onNoteClick(int position) {
        Note note = notes.get(position);
        Intent intent = new Intent(NoteDetail.this, SendSMS.class);
        intent.putExtra("note", note);
        intent.putExtra("class", course);
        startActivity(intent);
    }


}