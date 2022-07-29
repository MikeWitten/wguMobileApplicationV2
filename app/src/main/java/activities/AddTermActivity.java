package activities;

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
import java.util.Objects;

import database.AppDatabase;
import entities.Term;

public class AddTermActivity extends AppCompatActivity {
    Term newTerm;
    String termName;
    LocalDate termStartDate;
    LocalDate termEndDate;
    AppDatabase db;
    EditText termNameInput;
    Button termStartSelectBtn;
    Button termEndSelectBtn;
    Button addTermBtn;
    DatePickerDialog picker;
    Calendar myCalender;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        //This line creates a back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get an instance of the database.
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        //Populate the view
        populateTheView();
    }

    private void populateTheView() {
        termNameInput = findViewById(R.id.termNameInput);
        //Set up the start date picker
        startDatePicker();
        //Set up the end date picker
        endDatePicker();
        //Set up the save button
        addTerm();
    }

    private void addTerm() {
        addTermBtn = findViewById(R.id.addTermBTN);
        //Set an on-click listener
        addTermBtn.setOnClickListener(v -> {
            termName = termNameInput.getText().toString();
            //check for any null values
            if (isNull()){
                return;
            }
            //If all fields are filled in, add the new term to the database.
            newTerm = new Term(null, termName, termStartDate, termEndDate );
            db.termDAO().insertAll(newTerm);
            Toast.makeText(AddTermActivity.this, "Term added successfully", Toast.LENGTH_SHORT).show();
            //Send the user back to the terms list.
            Intent intent = new Intent(AddTermActivity.this, TermsList.class);
            startActivity(intent);
        });
    }

    public boolean isNull(){
        if (termNameInput.getText().toString().isEmpty()){
            Toast.makeText(this, "You must enter a Name for the Term", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (termStartDate == null){
            Toast.makeText(this, "You must select a start date ", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (termEndDate == null){
            Toast.makeText(this, "You must select an end date", Toast.LENGTH_SHORT).show();
            return true;
        }
        else return false;
    }

    private void endDatePicker() {
        termEndSelectBtn = findViewById(R.id.termEndSelectBTN);
        //Set an on-click listener
        termEndSelectBtn.setOnClickListener(v -> {
            myCalender = Calendar.getInstance();
            int day = myCalender.get(Calendar.DAY_OF_MONTH);
            int month = myCalender.get(Calendar.MONTH);
            int year = myCalender.get(Calendar.YEAR);
            //Create a date picker dialog
            picker = new DatePickerDialog(AddTermActivity.this, (view, year1, month1, dayOfMonth) -> {
                termEndDate = LocalDate.of(year1, (month1 + 1), dayOfMonth);
                termEndSelectBtn.setText(formatter.format(termEndDate));
            }, year, month, day);
            picker.show();
        });
    }

    private void startDatePicker() {
        termStartSelectBtn = findViewById(R.id.termStartSelectBTN);
        //Set an on-click listener
        termStartSelectBtn.setOnClickListener(v -> {
            //Set values for the calendar
            myCalender = Calendar.getInstance();
            int day = myCalender.get(Calendar.DAY_OF_MONTH);
            int month = myCalender.get(Calendar.MONTH);
            int year = myCalender.get(Calendar.YEAR);
            //Create a date picker dialog
            picker = new DatePickerDialog(AddTermActivity.this, (view, year1, month1, dayOfMonth) -> {
                termStartDate = LocalDate.of(year1, (month1 +1), dayOfMonth);
                termStartSelectBtn.setText(formatter.format(termStartDate));
            }, year, month, day);
            picker.show();
        });
    }
}