package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wittenPortfolio.R;

import entities.Course;
import entities.Note;

public class SendSMS extends AppCompatActivity {
    Course course;
    Note note;
    EditText phoneNumber;
    TextView message;
    String content;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);
        //Get objects from intent
        note = getIntent().getExtras().getParcelable("note");
        course = getIntent().getExtras().getParcelable("class");
        //Get SMS Permission
        ActivityCompat.requestPermissions(SendSMS.this,
                new String[]{Manifest.permission.SEND_SMS},
                PackageManager.PERMISSION_GRANTED);
        //Set message text
        message = findViewById(R.id.currentMessage);
        message.setText(note.note);
        //Identify edit text field
        phoneNumber = findViewById(R.id.getPhoneNumber);
        //Set up cancel button
        setUpCancel();
        //Set up send button
        setUpSend();
    }

    private void setUpSend() {
        Button send = findViewById(R.id.sendSMSBTN);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneNumber.getText().toString().isEmpty()){
                    Toast.makeText(SendSMS.this, "Phone Number is Required", Toast.LENGTH_SHORT).show();
                    return;
                }
                content = note.getNote();
                phone = phoneNumber.getText().toString();

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone, null, content, null, null);
                Toast.makeText(SendSMS.this, "Message Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpCancel() {
        Button cancelButton = findViewById(R.id.cancelSMSBTN);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendSMS.this, CourseDetail.class);
                intent.putExtra("class", course);
                startActivity(intent);
            }
        });
    }
}