package activities;

import static database.Converters.fromAssessmentString;

import android.app.AlarmManager;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.wittenPortfolio.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import database.AppDatabase;
import entities.Assessment;
import entities.AssessmentType;
import entities.Course;
import utilities.MyBroadcastReceiver;

public class AssessmentDetail extends AppCompatActivity {
    Assessment assessment;
    Assessment updatedAssessment;
    EditText titleText;
    TextView assIdTv;
    Button spinnerCourseBtn;
    Button spinnerAssTypeBtn;
    Button startDateSelector;
    Button endDateSelector;
    Button editBtn;
    Button cancelBtn;
    Button alarmBtn;
    Button cancelAlarmBtn;
    Button deleteAssessmentBtn;
    TextView alarmConfirmationTv;
    AppDatabase db;
    Integer assID;
    Integer classID;
    AssessmentType assType;
    LocalDate startDate;
    LocalDate endDate;
    String[] courses;
    String[] types;
    List<Course> courseList;
    ArrayList<Course> courseArrayList;
    boolean courseExists;
    Course currentCourse = null;
    AlertDialog courseDialog;
    AlertDialog typeDialog;
    AlertDialog.Builder saveDialog;
    DatePickerDialog datePickerDialog;
    AlarmManager alarmManager;
    PendingIntent pendingIntentStart;
    PendingIntent pendingIntentEnd;
    Intent alarmIntent;
    int selected = -1;
    boolean alarmSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_detail);
        //enable back button.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get instance of the database.
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        //Get Object from intent extra.
        assessment = getIntent().getExtras().getParcelable("assessment");
        //set up an alarm manager and intent.
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmIntent = new Intent(this, MyBroadcastReceiver.class);
        //set the fields to represent the assessment object.
        setFields();
        //Check for valid class.
        checkForValidClass();
        //Set up delete button
        setUpDeleteButton();
    }

    private void setUpDeleteButton() {
        deleteAssessmentBtn = findViewById(R.id.deleteAssessmentBTN);
        deleteAssessmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirm the delete with the user.
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentDetail.this);
                builder.setCancelable(true);
                builder.setTitle("Are You Sure?");
                builder.setMessage("Would you like to delete this assesment?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Delete the assessment
                                db.AssessmentDAO().delete(assessment);
                                //Return to assessments list
                                Intent intent = new Intent(AssessmentDetail.this, AssessmentList.class);
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

    private void checkForValidClass() {
        if (!courseExists) {
            db.AssessmentDAO().delete(assessment);
            Toast.makeText(this, "Sorry, this course no longer exists.", Toast.LENGTH_SHORT).show();
            //Return to Assessment List
            Intent intent = new Intent(AssessmentDetail.this, AssessmentList.class);
            startActivity(intent);
        }
    }

    private void setFields() {
        //Title
        titleText = findViewById(R.id.assTitleET);
        titleText.setText(assessment.assTitle);
        titleText.setEnabled(false);

        //ID
        assIdTv = findViewById(R.id.assIDTV);
        assIdTv.setText(assessment.assID.toString());
        assID = assessment.assID;

        //Course ID.  Populate Course Spinner
        populateCourseSpinner();

        //Assessment Type.  Populate Type spinner
        populateAssessmentType();

        //StartDate. Create a date-picker.
        startDatePicker();

        //EndDate. Create a date picker.
        endDatePicker();

        //Set up alarm button or cancel alarm button.
        alarmBtnCheck();
    }

    private void alarmBtnCheck() {
        //initialize the buttons and text view.
        alarmBtn = findViewById(R.id.alarmBTN);
        cancelAlarmBtn = findViewById(R.id.cancelAlarmBTN);
        alarmConfirmationTv = findViewById(R.id.alarmConfirmationTV);

        //Check to see if an alarm is already set.
        alarmSet = PendingIntent.getBroadcast(this,
                assessment.assID,
                alarmIntent,
                PendingIntent.FLAG_NO_CREATE) != null;
        if (alarmSet) {
            //Show the cancel button instead of the set alarm button.
            cancelAlarmBtn.setVisibility(View.VISIBLE);
            alarmBtn.setVisibility(View.GONE);
            alarmConfirmationTv.setText("Alarm has been set.");
            //Set the pending intents to send on to the cancel button.
            pendingIntentStart = PendingIntent.getBroadcast(this,
                    assessment.assID,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntentEnd = PendingIntent.getBroadcast(this,
                    assessment.assID + 10000,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            cancelAlarm(pendingIntentStart, pendingIntentEnd);
        } else {
            alarmButtonSetup();
        }
    }

    private void cancelAlarm(PendingIntent pendingIntentStart, PendingIntent pendingIntentEnd) {
        cancelAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Cancel alarms and pending intents.
                alarmManager.cancel(pendingIntentStart);
                alarmManager.cancel(pendingIntentEnd);
                pendingIntentStart.cancel();
                pendingIntentEnd.cancel();
                alarmBtnCheck();
            }
        });
    }

    private void alarmButtonSetup() {
        //Ensure the correct button is displayed.
        alarmBtn.setVisibility(View.VISIBLE);
        cancelAlarmBtn.setVisibility(View.GONE);
        alarmConfirmationTv.setText("Alarm turned off.");
        //Set an on click listener
        alarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set Calender for start date alarm
                int year = assessment.startDate.getYear();
                int month = assessment.startDate.getMonthValue();
                int day = assessment.startDate.getDayOfMonth();
                int hour = 16;
                int minute = 55;
                Calendar startCal = Calendar.getInstance();
                startCal.set(year, month - 1, day, hour, minute);
                //Set Calender for end date alarm
                int endY = assessment.endDate.getYear();
                int endM = assessment.endDate.getMonthValue();
                int endD = assessment.endDate.getDayOfMonth();
                Calendar endCal = Calendar.getInstance();
                endCal.set(endY, endM - 1, endD, hour, minute);
                //Pass the Long value of the calendars to the 'setAlarm' method.
                setAlarm(startCal.getTimeInMillis(), endCal.getTimeInMillis());
            }
        });
    }

    private void setAlarm(long startTimeInMillis, Long endTimeInMillis) {
        //Create a start date alarm with unique ID that matches the assessment ID.
        pendingIntentStart = PendingIntent.getBroadcast(this,
                assessment.assID,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTimeInMillis, pendingIntentStart);
        //Create an end date alarm with a unique ID that matches the assessment ID plus 10,000
        pendingIntentEnd = PendingIntent.getBroadcast(this,
                assessment.assID + 10000,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, endTimeInMillis, pendingIntentEnd);

        //Confirm the alarm action.
        Toast.makeText(this, "Alarm Set to 6AM on the Start and End dates.", Toast.LENGTH_SHORT).show();
        alarmBtnCheck();
    }

    private void endDatePicker() {
        endDateSelector = findViewById(R.id.endDateSelector);
        //Set date to current value and set default value.
        endDateSelector.setText(assessment.endDate.toString());
        endDate = assessment.endDate;
        //Create an on-click listener.
        endDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set values for the calendar.
                final Calendar myCalendar = Calendar.getInstance();
                int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                int month = myCalendar.get(Calendar.MONTH);
                int year = myCalendar.get(Calendar.YEAR);
                //Create a dialog.
                datePickerDialog = new DatePickerDialog(AssessmentDetail.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                endDate = LocalDate.of(year, (month + 1), dayOfMonth);
                                endDateSelector.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        endDateSelector.setClickable(false);
    }

    private void startDatePicker() {
        startDateSelector = findViewById(R.id.startDateSelector);
        //Set text for current date and default value.
        startDateSelector.setText(assessment.startDate.toString());
        startDate = assessment.startDate;
        //Set an on-click listener.
        startDateSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set values for the calendar.
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                //Create a dialog.
                datePickerDialog = new DatePickerDialog(AssessmentDetail.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                startDate = LocalDate.of(year, (month + 1), dayOfMonth);
                                startDateSelector.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
        startDateSelector.setClickable(false);
    }

    private void populateAssessmentType() {
        //Set the assessment type to default
        assType = assessment.assType;

        //Populate the spinner with an array from resource files.
        spinnerAssTypeBtn = findViewById(R.id.spinnerAssTypeBTN);
        spinnerAssTypeBtn.setText(assessment.assType.name());
        types = getResources().getStringArray(R.array.assessment_types_array);
        //Create a dialog.
        typeDialog = new AlertDialog.Builder(AssessmentDetail.this).setSingleChoiceItems(types, selected,
                (dialog, which) -> {
                    spinnerAssTypeBtn.setText(types[which]);
                    selected = which;
                    assType = fromAssessmentString(types[which]);
                    typeDialog.dismiss();
                }).setTitle("Ass Types").create();
        //Set an on click listener.
        spinnerAssTypeBtn.setOnClickListener(v -> {
            typeDialog.getListView().setSelection(selected);
            typeDialog.show();
        });
        spinnerAssTypeBtn.setClickable(false);
        selected = -1;
    }

    private void populateCourseSpinner() {
        //set value for boolean.
        courseExists = false;
        //Find the current course association.
        courseList = db.courseDAO().getAllCourses();
        for (Course c : courseList) {
            if (assessment.classID == c.courseID) {
                currentCourse = c;
                courseExists = true;
            }
        }
        if (currentCourse == null) {
            System.out.println("current course not found");
            return;
        }
        //Set a default value for 'Class ID'
        classID = currentCourse.courseID;
        //populate spinner by database generated array.
        spinnerCourseBtn = findViewById(R.id.spinnerCourseBTN);
        spinnerCourseBtn.setText(currentCourse.courseName);
        courses = db.courseDAO().getCoursesArray();
        //This course arrat list can be used to get the current course, however because it is not synchronized,
        // it is preferable to use a regular list.
        courseArrayList.addAll(courseList);

        //Create a dialog.
        courseDialog = new AlertDialog.Builder(AssessmentDetail.this).setSingleChoiceItems(courses, selected,
                (dialog, which) -> {
                    spinnerCourseBtn.setText(courses[which]);
                    selected = which;
                    //Find the selected course and set it to currentCourse.
                    for (Course c : courseArrayList) {
                        if (courses[which].equalsIgnoreCase(c.courseName)) {
                            currentCourse = c;
                        }
                    }
                    courseDialog.dismiss();
                }).setTitle(currentCourse.courseName).create();
        //Set an on click listener.
        spinnerCourseBtn.setOnClickListener(v -> {
            courseDialog.getListView().setSelection(selected);
            courseDialog.show();
        });
        spinnerCourseBtn.setClickable(false);
        selected = -1;
    }

    public void makeEditable(View view) {
        titleText.setEnabled(true);
        spinnerCourseBtn.setClickable(true);
        spinnerAssTypeBtn.setClickable(true);
        startDateSelector.setClickable(true);
        endDateSelector.setClickable(true);
        alarmBtn.setVisibility(View.GONE);
        cancelAlarmBtn.setVisibility(View.GONE);

        //Replace the edit button with a cancel button
        editBtn = findViewById(R.id.editBTN);
        editBtn.setVisibility(View.GONE);
        cancelBtn = findViewById(R.id.cancelBTN);
        cancelBtn.setVisibility(View.VISIBLE);
    }

    public void saveChanges(View view) {
        //Create a new Assessment object to replace the old one.
        updatedAssessment = new Assessment(assID, currentCourse.getCourseID(), titleText.getText().toString(),
                assType, startDate, endDate);
        //Confirm the changes made
        saveDialog = new AlertDialog.Builder(AssessmentDetail.this);
        saveDialog.setCancelable(true);
        saveDialog.setTitle("Update Assessment");
        saveDialog.setMessage("Would you like to save the changes you've made to this Assessment?");
        saveDialog.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.AssessmentDAO().update(updatedAssessment);
                        //If changes are made to the assessment, automatically cancel any associated alarms.
                        if (alarmSet) {
                            pendingIntentStart = PendingIntent.getBroadcast(AssessmentDetail.this,
                                    assessment.assID,
                                    alarmIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            pendingIntentEnd = PendingIntent.getBroadcast(AssessmentDetail.this,
                                    assessment.assID + 10000,
                                    alarmIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT);
                            alarmManager.cancel(pendingIntentStart);
                            alarmManager.cancel(pendingIntentEnd);
                            pendingIntentStart.cancel();
                            pendingIntentEnd.cancel();
                        }
                        Intent intent = new Intent(AssessmentDetail.this, AssessmentList.class);
                        startActivity(intent);
                    }
                });
        saveDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelChanges(null);
            }
        });
        saveDialog.create();
        saveDialog.show();
    }

    public void cancelChanges(View view) {
        Intent intent = new Intent(AssessmentDetail.this, AssessmentDetail.class);
        intent.putExtra("assessment", assessment);
        startActivity(intent);
    }

}