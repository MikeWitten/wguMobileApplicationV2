package activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.wittenPortfolio.R;

import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;

import database.AppDatabase;
import entities.Assessment;
import entities.AssessmentType;

public class AddAssessmentActivity extends AppCompatActivity {
    Integer assID;
    Integer classID;
    String assTitle;
    String className;
    String typeName;
    AssessmentType assType;
    LocalDate startDate;
    LocalDate endDate;
    AppDatabase db;
    Button courseSelectBTN;
    Button selectType;
    Button startSelectButton;
    Button endSelectButton;
    AlertDialog ad;
    AlertDialog ad2;
    String[] courses;
    String[] types;
    int selected = -1;
    int selected2 = -1;
    EditText title;
    Calendar myCalendar;
    DatePickerDialog picker;


    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assessment);
        //This line creates a back button.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get an instance of the database.
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        //Define the edit text field.
        title = findViewById(R.id.assessmentTitleInput);
        //Populate the 'Course Select' spinner.
        courseSpinnerPopulate();
        //Populate the 'Assessment Type' spinner.
        typeSpinnerPopulate();
        //Set up the date-picker for the start date.
        startDatePicker();
        //Set up the date-picker for the end date.
        endDatePicker();

    }

    private void endDatePicker() {
        endSelectButton = findViewById(R.id.endSelectBTN);
        //Create an on click listener.
        endSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set values for the calendar.
                myCalendar = Calendar.getInstance();
                int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                int month = myCalendar.get(Calendar.MONTH);
                int year = myCalendar.get(Calendar.YEAR);
                //Create a date-picker dialog.
                picker = new DatePickerDialog(AddAssessmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate = LocalDate.of(year, month + 1, dayOfMonth);
                        endSelectButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }

    private void startDatePicker() {
        startSelectButton = findViewById(R.id.startSelectBTN);
        //Set an on click listener.
        startSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set values for the calendar.
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                //Create a date picker dialog.
                picker = new DatePickerDialog(AddAssessmentActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate = LocalDate.of(year, month + 1, dayOfMonth);
                        startSelectButton.setText((month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });
    }

    private void typeSpinnerPopulate() {
        selectType = findViewById(R.id.typeSelectBTN);
        //Get an array of strings from resources.
        types = getResources().getStringArray(R.array.assessment_types_array);
        //Create a dialog.
        ad2 = new AlertDialog.Builder(AddAssessmentActivity.this).setSingleChoiceItems(types, selected2,
                (dialog, which) -> {
                    selectType.setText(types[which]);
                    selected2 = which;
                    ad2.dismiss();
                }).setTitle("Choose an Assessment Type").create();
        //Create an on click listener.
        selectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad2.getListView().setSelection(selected2);
                ad2.show();
            }
        });

    }

    private void courseSpinnerPopulate() {
        courseSelectBTN = findViewById(R.id.courseSelectBTN);
        //Get course array from the database.
        courses = db.courseDAO().getCoursesArray();
        //Create a dialog.
        ad = new AlertDialog.Builder(AddAssessmentActivity.this).setSingleChoiceItems(courses, selected,
                (dialog, which) -> {
                    courseSelectBTN.setText(courses[which]);
                    selected = which;
                    ad.dismiss();
                }).setTitle("Choose A Course").create();
        //Create an on click listener.
        courseSelectBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.getListView().setSelection(selected);
                ad.show();
            }
        });
    }

    public void addAssessmentToDatabase(View view) {
        assID = null;
        className = courseSelectBTN.getText().toString();
        classID = db.courseDAO().getClassID(className);
        assTitle = title.getText().toString();
        typeName = selectType.getText().toString();
        switch (typeName.toLowerCase()) {
            case "test":
                assType = AssessmentType.Test;
                break;
            case "project":
                assType = AssessmentType.Project;
                break;
            case "practical":
                assType = AssessmentType.Practical;
                break;
            case "paper":
                assType = AssessmentType.Paper;
                break;
            default:
                assType = AssessmentType.Test;
        }
        //Check that all fields are filled out.
        if (isNull()){
            return;
        }
        Assessment newAssessment = new Assessment(assID, classID, assTitle, assType, startDate, endDate);
        db.AssessmentDAO().insertAll(newAssessment);
        Toast.makeText(this, "Assessment Added", Toast.LENGTH_SHORT).show();
        //Send user back to previous screen.
        Intent intent = new Intent(AddAssessmentActivity.this, AssessmentList.class);
        startActivity(intent);
    }

    private boolean isNull() {
        if(className.isEmpty()){
            Toast.makeText(this, "You must select a class for your assessment", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(assTitle.isEmpty()){
            Toast.makeText(this, "You must give your assessment a name", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (startDate == null){
            Toast.makeText(this, "You must select a start date", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (endDate == null){
            Toast.makeText(this, "You must select an end date", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
            return false;
    }


}