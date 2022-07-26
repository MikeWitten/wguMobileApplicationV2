package activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wittenPortfolio.R;

import java.util.Objects;

import adapters.MentorListAdapter;
import database.AppDatabase;
import entities.Mentor;

public class MentorAdd extends AppCompatActivity {
    EditText editTextTextMentorName;
    EditText editTextMentorPhone;
    EditText editTextMentorEmailAddress;
    Button addMentorSaveButton;
    AppDatabase db;
    Mentor newMentor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_add);
        //Create a back button to the 'Mentor List' page.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get an instance of the database.
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        //Add an on-click listener for the save button
        createSaveButton();
    }

    private void createSaveButton() {
        editTextTextMentorName = findViewById(R.id.editTextMentorName);
        editTextMentorPhone = findViewById(R.id.editTextMentorPhone);
        editTextMentorEmailAddress = findViewById(R.id.editTextMentorEmailAddress);
        addMentorSaveButton = findViewById(R.id.addMentorSaveButton);
        addMentorSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check for empty fields.
                if(isNull()){
                    return;
                }
                String Name = editTextTextMentorName.getText().toString();
                String Phone = editTextMentorPhone.getText().toString();
                String email = editTextMentorEmailAddress.getText().toString();
                //Confirm the data with the user
                AlertDialog.Builder builder = new AlertDialog.Builder(MentorAdd.this);
                builder.setCancelable(true);
                builder.setTitle("Is This Information Correct?");
                builder.setMessage("Professor Name: " + Name +"\n" +
                        "Professor Phone: " + Phone + "\n" +
                        "Professor Email: " + email);
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Create and add the new mentor.
                                newMentor = new Mentor(null, Name, Phone, email);
                                db.mentorDAO().insertAll(newMentor);
                                //Direct user to the mentor list.
                                Intent intent = new Intent(MentorAdd.this, MentorList.class);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private boolean isNull() {
        if (editTextTextMentorName.getText().toString().isEmpty()){
            Toast.makeText(this, "Mentor Name is Required.", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(editTextMentorPhone.getText().toString().isEmpty()){
            Toast.makeText(this, "Mentor Phone Number is Required", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(editTextMentorEmailAddress.getText().toString().isEmpty()){
            Toast.makeText(this, "Email address is Required", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}