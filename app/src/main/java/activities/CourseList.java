package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wittenPortfolio.R;

import java.util.List;
import java.util.Objects;

import adapters.CourseListAdapter;
import database.AppDatabase;
import entities.Course;

/**
 * This class implements a listener to allow the user to select a list item in the recycler view.
 */
public class CourseList extends AppCompatActivity implements CourseListAdapter.OnCourseListener {
    private CourseListAdapter courseListAdapter;
    List<Course> courses;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        //Create a back button to the main activity
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get an instance of the database and a full list of 'Course' objects.
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        courses = db.courseDAO().getAllCourses();
        //Initialize the recycler view.
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView courseListRecyclerView = findViewById(R.id.courseListRecyclerView);
        //Set the layout manager.
        courseListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Create a visual divider to separate course objects.
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        courseListRecyclerView.addItemDecoration(decoration);
        //Initialize the adapter.
        courseListAdapter = new CourseListAdapter(this, courses, this);
        courseListRecyclerView.setAdapter(courseListAdapter);
    }

    /**
     * Navigate to the add course page.
     */
    public void toAddCourseView(View view) {
        Intent intent = new Intent(CourseList.this, CourseAdd.class);
        startActivity(intent);
    }

    /**
     * On click listener to migrate the course data to the details page using the 'Parcelable' implementation.
     */
    @Override
    public void onCourseClick(int position){
        Course course = courses.get(position);
        Intent intent = new Intent(CourseList.this, CourseDetail.class);
        intent.putExtra("class", course);
        Toast.makeText(this, course.courseName, Toast.LENGTH_SHORT).show();
    }
}