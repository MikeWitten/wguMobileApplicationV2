package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wittenPortfolio.R;

import java.util.List;
import java.util.Objects;

import adapters.MentorListAdapter;
import database.AppDatabase;
import entities.Mentor;

//This class implements a listener to allow the user to select a list item in the recycler view.
public class MentorList extends AppCompatActivity implements MentorListAdapter.OnMentorListener {
    private MentorListAdapter mentorListAdapter;
    AppDatabase db;
    List<Mentor> mentorList;
    Mentor selectedMentor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_list);
        //Create a back button.
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Get an instance of the database and a list of mentors.
        db = AppDatabase.getDbInstance(this.getApplicationContext());
        mentorList = db.mentorDAO().getAllMentors();

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView mentorListRecyclerView = findViewById(R.id.mentorListRecyclerView);
        mentorListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Create a visual break between objects in the list.
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mentorListRecyclerView.addItemDecoration(decoration);
        //Connect the recycler view to the adapter and listener
        mentorListAdapter = new MentorListAdapter(this, mentorList, this);
        mentorListRecyclerView.setAdapter(mentorListAdapter);
    }

    public void toAddMentor(View view) {
        Intent intent = new Intent(MentorList.this, MentorAdd.class);
        startActivity(intent);
    }

    @Override
    public void onMentorClick(int position) {
        selectedMentor = mentorList.get(position);
        Intent intent = new Intent(MentorList.this, MentorDetail.class);
        intent.putExtra("professor", selectedMentor);
        startActivity(intent);
    }
}