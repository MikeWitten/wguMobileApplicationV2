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

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import adapters.AssociatedCoursesAdapter;
import database.AppDatabase;
import entities.Course;
import entities.Term;

//This class implements a listener to allow the user to select a list item in the recycler view.
public class TermDetails extends AppCompatActivity implements AssociatedCoursesAdapter.OnAssociatedCourseListener {
    private AssociatedCoursesAdapter associatedCoursesAdapter;
    List<Course> associatedCourses;
    public FloatingActionButton toAddCourseBtn;
    AppDatabase db;
    Term term;
    TextView termName;
    TextView termStart;
    TextView termEnd;
    Button deleteTermBtn;
    RecyclerView    associatedClassesRecyclerView;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        //Get object from intent extra.
        term = getIntent().getExtras().getParcelable("term");
        //Populate the text views.
        populateTheTextViews();
        //Get list of courses associated with this term.
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        associatedCourses = db.courseDAO().getAssociatedCourses(term.getTermID());
        //set up a back button.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //set up floating button
        setUpFloatingButton();
        //Initialize the recycler view.
        initRecyclerView();
        //Set up the delete button.
        setUpDeleteButton();
    }

    private void setUpDeleteButton() {
        deleteTermBtn = findViewById(R.id.deleteTermButton);
        deleteTermBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirm the delete with the user.
                AlertDialog.Builder builder = new AlertDialog.Builder(TermDetails.this);
                builder.setCancelable(true);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Deleting a term will Delete all associated courses within the term\n" +
                        "\n" +
                        "We recommend updating course information before deleting a Term.\n" +
                        "\n" +
                        "Would you like to continue?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete associated courses.
                                db.courseDAO().deleteTermCourses(term.getTermID());
                                //Delete the Term
                                db.termDAO().delete(term);
                                //Return to Terms list.
                                Intent intent = new Intent(TermDetails.this, TermsList.class);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Return without doing anything.
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private void initRecyclerView() {
        associatedClassesRecyclerView = findViewById(R.id.associatedClassesRecyclerView);
        associatedClassesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Create a visual separation between items.
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        associatedClassesRecyclerView.addItemDecoration(decoration);
        associatedCoursesAdapter = new AssociatedCoursesAdapter(this, associatedCourses, this);
        associatedClassesRecyclerView.setAdapter(associatedCoursesAdapter);
    }

    private void populateTheTextViews() {
        termName = findViewById(R.id.termNameTV);
        termStart = findViewById(R.id.termStartDateTV);
        termEnd = findViewById(R.id.termEndDateTV);
        termName.setText(term.getTermName());
        termStart.setText("Start date: " + formatter.format(term.startDate));
        termEnd.setText("End Date: " + formatter.format(term.endDate));
    }

    private void setUpFloatingButton() {
        toAddCourseBtn = findViewById(R.id.toAddCourse);
        //set an on-click listener
        toAddCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermDetails.this, CourseAdd.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onAssociatedCourseClick(int position) {
        Course course = associatedCourses.get(position);
        Intent intent = new Intent(TermDetails.this, CourseDetail.class);
        intent.putExtra("class", course);
        startActivity(intent);

    }
}