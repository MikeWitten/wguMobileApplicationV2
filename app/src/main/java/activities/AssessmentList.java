package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wittenPortfolio.R;

import java.util.List;
import java.util.Objects;

import adapters.AssessmentListAdapter;
import database.AppDatabase;
import entities.Assessment;

/**
 * This class implements a listener to allow the user to select a list item in the recycler view.
 */
public class AssessmentList extends AppCompatActivity implements AssessmentListAdapter.OnAssessmentListener {
    List<Assessment> assessments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        //Create a back button to the main activity.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get an instance of the database and a full list of 'Assessment" objects.
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        assessments = db.AssessmentDAO().getAllAssessments();
        //Initialize the recycler view.
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView rcView = findViewById(R.id.assessmentRecyclerView);
        //Set the layout manager.
        rcView.setLayoutManager(new LinearLayoutManager(this));
        //Create a visual divider to separate the assessment objects.
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcView.addItemDecoration(decoration);
        //Initialize the adapter.
        AssessmentListAdapter assessmentListAdapter = new AssessmentListAdapter(this, assessments, this);
        rcView.setAdapter(assessmentListAdapter);

    }

    /**
     * Navigation to the add assessment page.
     */
    public void toAddAssessmentView(View view) {
        Intent intent = new Intent(AssessmentList.this, activities.AddAssessmentActivity.class);
        startActivity(intent);
    }

    /**
     * On click listener to migrate the assessment to the details page using the 'Parcelable' implementation.
     */
    @Override
    public void onAssessmentClick(int position) {
        Assessment a = assessments.get(position);
        Intent intent = new Intent(AssessmentList.this, activities.AssessmentDetail.class);
        intent.putExtra ("assessment", a);
        startActivity(intent);
    }
}