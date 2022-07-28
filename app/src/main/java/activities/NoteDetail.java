package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.wittenPortfolio.R;

import entities.Course;

public class NoteDetail extends AppCompatActivity {
    TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        //get course object
        Course course = getIntent().getExtras().getParcelable("class");

        temp = findViewById(R.id.textViewTemp);
        temp.setText(course.courseName);
    }
}