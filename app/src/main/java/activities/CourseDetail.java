package activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.wittenPortfolio.R;

import entities.Course;

public class CourseDetail extends AppCompatActivity {
    Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        //Get Object from the intent.
        course = getIntent().getExtras().getParcelable("class");
        //FIXME START HERE
    }
}