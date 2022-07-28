package activities;

import static database.Converters.fromStatusString;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wittenPortfolio.R;

import java.sql.SQLOutput;
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
import utilities.MyBroadcastReceiver;

public class CourseDetail extends AppCompatActivity {
    Course course;
    EditText courseNameET;
    Button termNameSpnrBtn;
    Button mentorNameSpnrBtn;
    Button courseStatusSpnrBtn;
    Button setCourseAlarmBtn;
    Button deleteAlarmBtn;
    Button courseStartDateSelector;
    Button courseEndDateSelector;
    Button editCourseBtn;
    Button saveChangesBtn;
    Button cancelChangesBtn;
    Button deleteCourseBtn;
    TextView alarmNotificationTV;
    Course newCourse;
    Integer courseID;
    Integer newTermID;
    Integer newMentorID;
    String newCourseName;
    LocalDate newStartDate;
    LocalDate newEndDate;
    CourseStatus newStatus;
    AppDatabase db;
    AlarmManager alarmManager;
    Intent alarmIntent;
    List<Term> termList;
    List<Mentor> mentorList;
    Term currentTerm;
    Mentor currentMentor;
    String[] termNames;
    String[] mentorNames;
    String[] statusArray;
    int selected = -1;
    AlertDialog termDialog;
    AlertDialog mentorDialog;
    AlertDialog statusDialog;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
    Boolean alarmSet;
    PendingIntent pendingIntentStart;
    PendingIntent pendingIntentEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        //Enable the back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get instance of the database.
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        //Get Object from the intent.
        course = getIntent().getExtras().getParcelable("class");
        courseID = course.courseID;
        //Set up an alarm manager and intent.
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(this, MyBroadcastReceiver.class);
        //Set fields to represent the currently selected course.
        setFields();
        //Check for alarms
        alarmCheck();
        //Allow editing
        makeEditable();
        //Set up Delete button
        setUpDeleteButton();

    }

    private void setUpDeleteButton() {
        deleteCourseBtn = findViewById(R.id.deleteCourseBTN);
        deleteCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirm the delete with the user.
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CourseDetail.this);
                builder.setCancelable(true);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Would you like to delete this course?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete the course.
                                db.courseDAO().delete(course);
                                //Confirm with user
                                Toast.makeText(CourseDetail.this, "Course Deleted", Toast.LENGTH_SHORT).show();
                                //Navigate to the courses list.
                                Intent intent = new Intent(CourseDetail.this, CourseList.class);
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
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void makeEditable() {
        editCourseBtn = findViewById(R.id.editCourseBTN);
        cancelChangesBtn = findViewById(R.id.invisibleCancelChangesBTN);
        saveChangesBtn = findViewById(R.id.invisibleSaveChangesBTN);
        //Set an on click listener
        editCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCourseBtn.setVisibility(View.GONE);
                cancelChangesBtn.setVisibility(View.VISIBLE);
                //Create a cancel button.
                setUpCancelButton();
                saveChangesBtn.setVisibility(View.VISIBLE);
                //Set up the save feature
                setUpSaveFeature();

                //Allow the user to change attributes
                courseNameET.setEnabled(true);
                termNameSpnrBtn.setClickable(true);
                mentorNameSpnrBtn.setClickable(true);
                courseStatusSpnrBtn.setClickable(true);
                courseStartDateSelector.setClickable(true);
                courseEndDateSelector.setClickable(true);

                setCourseAlarmBtn.setVisibility(View.GONE);
                deleteAlarmBtn.setVisibility(View.GONE);
                deleteCourseBtn.setVisibility(View.GONE);
            }
        });
    }

    private void setUpSaveFeature() {
        saveChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirm the update with the user.
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(CourseDetail.this);
                builder.setCancelable(true);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Would you like to update this course?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Get new course name
                                if(courseNameET.getText().toString().isEmpty()){
                                    newCourseName = "The course that must not be named";
                                }
                                newCourseName = courseNameET.getText().toString();
                                //Create the new course.
                                newCourse = new Course(courseID, newTermID, newMentorID,
                                        newCourseName, newStartDate, newEndDate, newStatus);
                                //Add the new course to the database.
                                db.courseDAO().update(newCourse);
                                Toast.makeText(CourseDetail.this, "Course updated.", Toast.LENGTH_SHORT).show();
                                //Navigate to the courses list.
                                Intent intent = new Intent(CourseDetail.this, CourseList.class);
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
                androidx.appcompat.app.AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private void setUpCancelButton() {
        cancelChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseDetail.this, CourseDetail.class);
                intent.putExtra("class", course);
                startActivity(intent);
            }
        });
    }

    private void alarmCheck() {
        //Initialize buttons
        setCourseAlarmBtn = findViewById(R.id.setCourseAlarmBTN);
        deleteAlarmBtn = findViewById(R.id.invisibleDeleteAlarmBTN);
        alarmNotificationTV = findViewById(R.id.courseAlarmNotificationTV);

        //Check to see if an alarm is already set.
        alarmSet = PendingIntent.getBroadcast(this,
                courseID + 1000,
                alarmIntent,
                PendingIntent.FLAG_NO_CREATE) != null;
        if(alarmSet){
            //Show cancel button instead of set button.
            deleteAlarmBtn.setVisibility(View.VISIBLE);
            setCourseAlarmBtn.setVisibility(View.GONE);
            alarmNotificationTV.setText("An alarm has been set.");
            //Set pending intents to send on to the cancel button.
            pendingIntentStart = PendingIntent.getBroadcast(this,
                    courseID + 1000,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntentEnd = PendingIntent.getBroadcast(this,
                    courseID + 5000,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            //Pass the pending intents
            deleteAlarms(pendingIntentStart, pendingIntentEnd);
        } else{
            alarmButtonSetUp();
        }

    }

    private void deleteAlarms(PendingIntent pendingIntentStart, PendingIntent pendingIntentEnd) {
        deleteAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cancel alarms and pending intents.
                alarmManager.cancel(pendingIntentStart);
                alarmManager.cancel(pendingIntentEnd);
                pendingIntentStart.cancel();
                pendingIntentEnd.cancel();
                //Go back to the alarm check to ensure successful deletion.
                alarmCheck();
            }
        });
    }

    private void alarmButtonSetUp() {
        //Ensure the correct button is displayed.
        setCourseAlarmBtn.setVisibility(View.VISIBLE);
        deleteAlarmBtn.setVisibility(View.GONE);
        alarmNotificationTV.setText("Alarm turned off.");
        //Set an on-click listener
        setCourseAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set Calender for start date alarm
                int year = course.startDate.getYear();
                int month = course.startDate.getMonthValue();
                int day = course.startDate.getDayOfMonth();
                int hour = 6;
                int minute = 0;
                Calendar startCal = Calendar.getInstance();
                startCal.set(year, month - 1, day, hour, minute);
                //Set Calender for end date alarm
                int endY = course.endDate.getYear();
                int endM = course.endDate.getMonthValue();
                int endD = course.endDate.getDayOfMonth();
                Calendar endCal = Calendar.getInstance();
                endCal.set(endY, endM - 1, endD, hour, minute);
                //Pass the Long value of the calendars to the 'setAlarm' method.
                setAlarm(startCal.getTimeInMillis(), endCal.getTimeInMillis());
            }
        });
    }

    private void setAlarm(long startTimeInMillis, long endTimeInMillis1) {
        //Create a start date alarm with a unique ID that matches the course ID plus 1000.
        pendingIntentStart = PendingIntent.getBroadcast(this,
                courseID + 1000,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTimeInMillis, pendingIntentStart);
        //Create an end date alarm with a unique ID that matches the course ID plus 5000.
        pendingIntentEnd = PendingIntent.getBroadcast(this,
                courseID + 5000,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTimeInMillis1, pendingIntentEnd);
        //Confirm the alarm action
        Toast.makeText(this, "Alarms have been set for 6AM on the Start and End Dates", Toast.LENGTH_SHORT).show();
        alarmCheck();
    }

    private void setFields() {
        //Course Title
        courseNameET = findViewById(R.id.courseNameET);
        courseNameET.setText(course.courseName);
        courseNameET.setEnabled(false);
        //Set Default name
        newCourseName = course.courseName;

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

    private void endDatePicker() {
        courseEndDateSelector = findViewById(R.id.courseEndDateSelector);
        courseEndDateSelector.setText(formatter.format(course.endDate));
        newEndDate = course.endDate;
        //Set an on click listener
        courseEndDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal2 = Calendar.getInstance();
                int day = cal2.get(Calendar.DAY_OF_MONTH);
                int month = cal2.get(Calendar.MONTH);
                int year = cal2.get(Calendar.YEAR);
                //Create a dialog.
                DatePickerDialog end = new DatePickerDialog(CourseDetail.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                newEndDate = LocalDate.of(year, (month + 1), dayOfMonth);
                                courseEndDateSelector.setText(formatter.format(newEndDate));
                            }
                        },year, month, day);
                end.show();
            }
        });
        courseEndDateSelector.setClickable(false);
    }

    private void startDatePicker() {
        courseStartDateSelector = findViewById(R.id.courseStartDateSelector);
        courseStartDateSelector.setText(formatter.format(course.startDate));
        newStartDate = course.startDate;
        //Set an on click listener
        courseStartDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal1 = Calendar.getInstance();
                int day = cal1.get(Calendar.DAY_OF_MONTH);
                int month = cal1.get(Calendar.MONTH);
                int year = cal1.get(Calendar.YEAR);
                //Create a dialog
                DatePickerDialog start = new DatePickerDialog(CourseDetail.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                newStartDate = LocalDate.of(year,(month + 1), dayOfMonth);
                                courseStartDateSelector.setText(formatter.format(newStartDate));
                            }
                        }, year, month, day);
                start.show();
                }
        });
        courseStartDateSelector.setClickable(false);
    }

    private void populateStatusSpinner() {
        //Set the default value of the status.
        newStatus = course.status;
        //Populate the spinner with an array from the resources folder.
        courseStatusSpnrBtn = findViewById(R.id.courseStatusSpnrBTN);
        courseStatusSpnrBtn.setText(newStatus.toString());
        statusArray = getResources().getStringArray(R.array.course_status_array);
        //Create a dialog.
        statusDialog = new AlertDialog.Builder(CourseDetail.this).setSingleChoiceItems(statusArray, selected,
                (dialog, which) ->{
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
        courseStatusSpnrBtn.setClickable(false);
        selected = -1;
    }

    private void populateMentorSpinner() {
        mentorNameSpnrBtn = findViewById(R.id.mentorNameSpnrBTN);
        //Set the default
        newMentorID = course.mentorID;
        //Find the current association.
        mentorList = db.mentorDAO().getAllMentors();
        for (Mentor m: mentorList){
            if(Objects.equals(course.mentorID, m.id)){
                currentMentor = m;
            }
        }
        //Populate the spinner with a database generated string array
        mentorNameSpnrBtn.setText(currentMentor.name);
        mentorNames = db.mentorDAO().getMentorsArray();
        //Create a dialog.
        mentorDialog = new AlertDialog.Builder(CourseDetail.this). setSingleChoiceItems(mentorNames, selected,
                (dialog, which) -> {
                    mentorNameSpnrBtn.setText(mentorNames[which]);
                    selected = which;
                    //Find the selected mentor and set it to current mentor.
                    for (Mentor m: mentorList){
                        if(mentorNames[which].toString().equalsIgnoreCase(m.name)){
                            currentMentor = m;
                            newMentorID = m.id;
                        }
                    }
                    mentorDialog.dismiss();
                }).setTitle(currentMentor.name).create();
        //Set an on click listener
        mentorNameSpnrBtn.setOnClickListener(v -> {
            mentorDialog.getListView().setSelection(selected);
            mentorDialog.show();
        });
        mentorNameSpnrBtn.setClickable(false);
        selected = -1;
    }

    private void populateTermSpinner() {
        termNameSpnrBtn = findViewById(R.id.termNameSpnrBTN);
        //Set the default.
        newTermID = course.termID;
        //Find the current association.
        termList = db.termDAO().getAllTerms();
        for (Term t: termList){
            if(Objects.equals(course.termID, t.termID)){
                currentTerm = t;
            }
        }
        //Populate spinner by database generated string array
        termNameSpnrBtn.setText(currentTerm.getTermName());
        termNames = db.termDAO().getTermsArray();
        //Create a dialog.
        termDialog = new AlertDialog.Builder(CourseDetail.this).setSingleChoiceItems(termNames, selected,
                (dialog, which) -> {
                    termNameSpnrBtn.setText(termNames[which]);
                    selected = which;
                    //Find the selected term and set it to the current term.
                    for (Term t: termList){
                        if(termNames[which].toString().equalsIgnoreCase(t.termName)){
                            currentTerm = t;
                            newTermID = currentTerm.termID;
                        }
                    }
                    termDialog.dismiss();
                }).setTitle(currentTerm.termName).create();
        //Set an on click listener.
        termNameSpnrBtn.setOnClickListener(v -> {
            termDialog.getListView().setSelection(selected);
            termDialog.show();
        });
        termNameSpnrBtn.setClickable(false);
        selected = -1;
    }

    public void toAssociatedNotes(View view) {
        Intent intent = new Intent(CourseDetail.this, NoteDetail.class);
        intent.putExtra("class", course);
        startActivity(intent);
    }
}