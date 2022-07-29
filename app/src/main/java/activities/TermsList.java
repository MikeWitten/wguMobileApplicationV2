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

import adapters.TermListAdapter;
import database.AppDatabase;
import entities.Term;

public class TermsList extends AppCompatActivity implements TermListAdapter.OnTermListener {
    List<Term> terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        terms = db.termDAO().getAllTerms();

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView rcView = findViewById(R.id.termsRecyclerView);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcView.addItemDecoration(decoration);
        TermListAdapter termListAdapter = new TermListAdapter(this, terms, this);
        rcView.setAdapter(termListAdapter);
    }

    public void toAddTermView(View view) {
        Intent intent = new Intent(TermsList.this, AddTermActivity.class);
        startActivity(intent);
    }

    //Send user to the details page.
    @Override
    public void onTermClick(int position) {
        Term t = terms.get(position);
       Intent intent = new Intent(TermsList.this, TermDetails.class);
       intent.putExtra("term", t);
       startActivity(intent);

       //FIXME do more here



    }
}