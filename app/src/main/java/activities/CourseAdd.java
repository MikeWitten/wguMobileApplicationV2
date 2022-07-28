package activities;

import static database.Converters.fromStatusString;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.wittenPortfolio.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import database.AppDatabase;
import entities.Course;
import entities.CourseStatus;
import entities.Mentor;
import entities.Term;

public class CourseAdd extends AppCompatActivity {
    EditText courseNameET;
    Button termNameSpnrBtn;
    Button mentorNameSpnrBtn;
    Button courseStatusSpnrBtn;
    Button courseStartDateSelector;
    Button courseEndDateSelector;
    Button addCourseBtn;
    Course newCourse;
    Mentor currentMentor = null;
    Term currentTerm = null;
    Integer newTermID;
    Integer newMentorID;
    String newCourseName;
    LocalDate newStartDate;
    LocalDate newEndDate;
    CourseStatus newStatus;
    AppDatabase db;
    List<Term> termList;
    List<Mentor> mentorList;
    String[] termNames;
    String[] mentorNames;
    String[] statusArray;
    int selected = -1;
    AlertDialog termDialog;
    AlertDialog mentorDialog;
    AlertDialog statusDialog;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);
        //Create a back button to the course list.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get an instance of the database
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        termList = db.termDAO().getAllTerms();
        mentorList = db.mentorDAO().getAllMentors();
        //Set up the spinner buttons
        setUpSpinners();
        //Initialize the edit text field.
        courseNameET = findViewById(R.id.courseNameET2);
        //Set up the Save button
        saveSetUp();
    }

    private void saveSetUp() {
        addCourseBtn = findViewById(R.id.addCoursebutton2);
        //set an on-click listener
        addCourseBtn.setOnClickListener(v -> {
            if (isNull()) {
                return;
            }
            newCourseName = courseNameET.getText().toString();
            //Create a new course object
            newCourse = new Course(null, newTermID, newMentorID, newCourseName,
                    newStartDate, newEndDate, newStatus);
            //Create the dialog to confirm with user
            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CourseAdd.this);
            builder.setCancelable(true);
            builder.setTitle("Are You Sure?");
            builder.setMessage("Would you like to add this course?");
            builder.setPositiveButton("Confirm",
                    (dialog, which) -> {
                        db.courseDAO().insertAll(newCourse);
                        Toast.makeText(CourseAdd.this, "Course Added.", Toast.LENGTH_SHORT).show();
                        //Navigate to the courses list.
                        Intent intent = new Intent(CourseAdd.this, CourseList.class);
                        startActivity(intent);
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                //Return without doing anything.
            });
            androidx.appcompat.app.AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private boolean isNull() {
        if (courseNameET.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please add a course name", Toast.LENGTH_SHORT).show();
            return true;
        } else if (newTermID == null) {
            Toast.makeText(this, "Please select a term", Toast.LENGTH_SHORT).show();
            return true;
        } else if (newMentorID == null) {
            Toast.makeText(this, "Please select a mentor", Toast.LENGTH_SHORT).show();
            return true;
        } else if (newStatus == null) {
            Toast.makeText(this, "Please select a course status", Toast.LENGTH_SHORT).show();
            return true;
        } else if (newStartDate == null) {
            Toast.makeText(this, "Please select a start date", Toast.LENGTH_SHORT).show();
            return true;
        } else if (newEndDate == null) {
            Toast.makeText(this, "Please select and end date", Toast.LENGTH_SHORT).show();
            return true;
        } else
            return false;
    }

    private void setUpSpinners() {
        //Term Name. Populate the spinner button.
        populateTermSpinner();
        //Mentor Name. Populate the spinner button.
        populateMentorSpinner();
        //Course Status. Populate the status spinner.
        populateStatusSpinner();
        //Set up start date picker
        startDatePicker();
        //Set up end date picker
        endDatePicker();
    }

    private void populateTermSpinner() {
        termNameSpnrBtn = findViewById(R.id.termNameSpnrBTN2);
        //Populate spinner by database generated string array
        termNames = db.termDAO().getTermsArray();
        //Create a dialog.
        termDialog = new AlertDialog.Builder(CourseAdd.this).setSingleChoiceItems(termNames, selected,
                (dialog, which) -> {
                    termNameSpnrBtn.setText(termNames[which]);
                    selected = which;
                    //Find the selected term and set it to the current term.
                    for (Term t : termList) {
                        if (termNames[which].equalsIgnoreCase(t.termName)) {
                            currentTerm = t;
                            newTermID = currentTerm.termID;
                        }
                    }
                    termDialog.dismiss();
                }).setTitle("termName").create();
        //Set an on click listener.
        termNameSpnrBtn.setOnClickListener(v -> {
            termDialog.getListView().setSelection(selected);
            termDialog.show();
        });
        selected = -1;
    }

    private void populateMentorSpinner() {
        mentorNameSpnrBtn = findViewById(R.id.mentorNameSpnrBTN2);
        //Populate the spinner with a database generated string array
        mentorNames = db.mentorDAO().getMentorsArray();
        //Create a dialog.
        mentorDialog = new AlertDialog.Builder(CourseAdd.this).setSingleChoiceItems(mentorNames, selected,
                (dialog, which) -> {
                    mentorNameSpnrBtn.setText(mentorNames[which]);
                    selected = which;
                    //Find the selected mentor and set it to current mentor.
                    for (Mentor m : mentorList) {
                        if (mentorNames[which].equalsIgnoreCase(m.name)) {
                            currentMentor = m;
                            newMentorID = m.id;
                        }
                    }
                    mentorDialog.dismiss();
                }).setTitle("mentorName").create();
        //Set an on click listener
        mentorNameSpnrBtn.setOnClickListener(v -> {
            mentorDialog.getListView().setSelection(selected);
            mentorDialog.show();
        });
        selected = -1;
    }

    private void populateStatusSpinner() {
        //Populate the spinner with an array from the resources folder.
        courseStatusSpnrBtn = findViewById(R.id.courseStatusSpnrBTN2);
        statusArray = getResources().getStringArray(R.array.course_status_array);
        //Create a dialog.
        statusDialog = new AlertDialog.Builder(CourseAdd.this).setSingleChoiceItems(statusArray, selected,
                (dialog, which) -> {
                    courseStatusSpnrBtn.setText(statusArray[which]);
                    selected = which;
                    newStatus = fromStatusString(statusArray[which]);
                    statusDialog.dismiss();
                }).setTitle("Stati").create();
        //Set an on click listener.
        courseStatusSpnrBtn.setOnClickListener(v -> {
            statusDialog.getListView().setSelection(selected);
            statusDialog.show();
        });
        selected = -1;
    }

    private void startDatePicker() {
        courseStartDateSelector = findViewById(R.id.courseStartDateSelector2);
        //Set an on click listener
        courseStartDateSelector.setOnClickListener(v -> {
            final Calendar cal1 = Calendar.getInstance();
            int day = cal1.get(Calendar.DAY_OF_MONTH);
            int month = cal1.get(Calendar.MONTH);
            int year = cal1.get(Calendar.YEAR);
            //Create a dialog
            DatePickerDialog start = new DatePickerDialog(CourseAdd.this,
                    (view, year1, month1, dayOfMonth) -> {
                        newStartDate = LocalDate.of(year1, (month1 + 1), dayOfMonth);
                        courseStartDateSelector.setText(formatter.format(newStartDate));
                    }, year, month, day);
            start.show();
        });
    }

    private void endDatePicker() {
        courseEndDateSelector = findViewById(R.id.courseEndDateSelector2);
        //Set an on click listener
        courseEndDateSelector.setOnClickListener(v -> {
            final Calendar cal2 = Calendar.getInstance();
            int day = cal2.get(Calendar.DAY_OF_MONTH);
            int month = cal2.get(Calendar.MONTH);
            int year = cal2.get(Calendar.YEAR);
            //Create a dialog.
            DatePickerDialog end = new DatePickerDialog(CourseAdd.this,
                    (view, year1, month1, dayOfMonth) -> {
                        newEndDate = LocalDate.of(year1, (month1 + 1), dayOfMonth);
                        courseEndDateSelector.setText(formatter.format(newEndDate));
                    }, year, month, day);
            end.show();
        });
    }
}