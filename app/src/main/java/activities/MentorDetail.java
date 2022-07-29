package activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.wittenPortfolio.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import database.AppDatabase;
import entities.Course;
import entities.Mentor;

public class MentorDetail extends AppCompatActivity {
    ListView listView;
    TextView tvMentorName;
    TextView tvMentorPhone;
    TextView tvMentorEmail;
    AppDatabase db;
    Button deleteMentorBtn;
    Mentor currentMentor;
    ArrayList<String> classNames = new ArrayList<>();
    List<Course> courseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_detail);
        //Create back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get database instance
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        //Get object from intent.
        currentMentor = getIntent().getExtras().getParcelable("professor");
        //Set text for text views
        setTexts();
        //Set list view
        setListView();
        //Setup the delete button
        deleteSetup();
    }

    private void deleteSetup() {
        deleteMentorBtn = findViewById(R.id.deleteMentorBTN);
        deleteMentorBtn.setOnClickListener(v -> {
            //Confirm the delete with the user.
            AlertDialog.Builder builder = new AlertDialog.Builder(MentorDetail.this);
            builder.setCancelable(true);
            builder.setTitle("Are You Sure?");
            builder.setMessage("Deleting a mentor will delete all of their associated classes, this cannot be undone\n" +
                    "\n" +
                    "We recommend updating class information before deleting a mentor.\n" +
                    "\n" +
                    "Would you like to continue?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        //Delete associated courses.
                        db.courseDAO().deleteMentorCourses(currentMentor.id);
                        //Delete the Mentor
                        db.mentorDAO().delete(currentMentor);
                        //Return to mentor list
                        Intent intent = new Intent(MentorDetail.this, MentorList.class);
                        startActivity(intent);
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                //Return without doing anything.
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        });
    }

    private void setListView() {
        listView = findViewById(R.id.associatedClassListView);
        //Get course list from database.
        courseList = db.courseDAO().getAllCourses();
        //Find associated courses.
        for(Course c: courseList){
            if(Objects.equals(c.mentorID, currentMentor.id)){
                classNames.add(c.courseName);
            }
        }
        //Define an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>
                (this, R.layout.course_name_row, classNames);
        listView.setAdapter(adapter);
    }

    private void setTexts() {
        tvMentorName = findViewById(R.id.tvMentorName);
        tvMentorPhone = findViewById(R.id.tvMentorPhone);
        tvMentorEmail = findViewById(R.id.tvMentorEmail);

        tvMentorName.setText(currentMentor.name);
        tvMentorPhone.setText(currentMentor.phone);
        tvMentorEmail.setText(currentMentor.email);
    }
}