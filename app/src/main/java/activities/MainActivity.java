package activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.wittenPortfolio.R;

import java.time.LocalDate;

import database.AppDatabase;
import entities.Assessment;
import entities.AssessmentType;
import entities.Course;
import entities.CourseStatus;
import entities.Mentor;
import entities.Note;
import entities.Term;

public class MainActivity extends AppCompatActivity {

    //Start main screen.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Create an overflow menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void toTermsView(View view) {
        Intent intent = new Intent(MainActivity.this, TermsList.class);
        startActivity(intent);
    }

    public void toCoursesView(View view) {
    }

    public void toAssessmentsView(View view) {
        Intent intent = new Intent(MainActivity.this, AssessmentList.class);
        startActivity(intent);
    }

    public void toMentorsView(View view) {
    }

    public void populateTestData(MenuItem item) throws InterruptedException {
        System.out.println("testing" );
        AppDatabase db= AppDatabase.getDbInstance(this.getApplicationContext());
        //empty database
        db.clearAllTables();

        //Create test data.
        LocalDate firstStart= LocalDate.of(2001,1,1);
        LocalDate firstEnd=LocalDate.of(2001,12,31);
        LocalDate secondStart= LocalDate.of(2002,1,1);
        LocalDate secondEnd=LocalDate.of(2002,12,31);
        LocalDate thirdStart= LocalDate.of(2004,1,1);
        LocalDate thirdEnd=LocalDate.of(2004,12,31);
        LocalDate fourthStart= LocalDate.of(2001,1,1);
        LocalDate fourthEnd=LocalDate.of(2001,12,31);

        Term a = new Term(1,"Sorcerers Stone", firstStart, firstEnd);
        Term b = new Term(2, "Chamber of Secrets", secondStart, secondEnd);
        Term c = new Term(3, "Prisoner of Azkaban" , thirdStart, thirdEnd);
        Term d = new Term(4,"Order of the Phoenix", fourthStart, fourthEnd);

        Mentor dumbledore = new Mentor(1, "Albus Dumbledore", "123-456-7890", "a.d@hogwarts.wiz");
        Mentor mcGonagall = new Mentor(2, "Minerva McGonagall", "123-321-1234", "mcGonagall@hogwarts.wiz");
        Mentor hagrid = new Mentor(3,"Rubius Hagrid", "123-098-7654", "dogswallop@hogwarts.wiz");
        Mentor snape = new Mentor(4, "Severus Snape", "123-567-9642", "halfbloodprince@hogwarts.wiz");

        Course aa = new Course(1, 1, 1,
                "Intro to Wizardy", firstStart, firstEnd, CourseStatus.Completed);
        Course bb = new Course(2, 1, 2,
                "Transmogrification", firstStart, firstEnd, CourseStatus.Completed);
        Course cc = new Course(3, 1, 3,
                "Intro to Minor Beasts", firstStart, firstEnd, CourseStatus.Dropped);
        Course dd = new Course(4, 1, 4,
                "Intro to Potions", firstStart, firstEnd, CourseStatus.Completed);

        Course ee = new Course(5, 2, 1,
                "Wizardly Stereotypes 101", secondStart, secondEnd, CourseStatus.Completed);
        Course ff = new Course(6, 2, 2,
                "Torturing Pets 101", secondStart, secondEnd, CourseStatus.PlanToTake);

        Course gg = new Course(7, 3, 3,
                "Caring for Critters 215", thirdStart, thirdEnd, CourseStatus.InProgress);
        Course hh = new Course(8, 3, 4,
                "Lesser Potions and Brewery", thirdStart, thirdEnd, CourseStatus.Completed);
        Course ii = new Course(9, 3, 1,
                "Long Beards 101", thirdStart, thirdEnd, CourseStatus.Completed);

        Course jj = new Course(10, 4, 2,
                "Liability Insurance for Witches", fourthStart, fourthEnd, CourseStatus.Completed);
        Course kk = new Course(11, 4, 3,
                "Fantastic Beasts", fourthStart, fourthEnd, CourseStatus.InProgress);
        Course ll = new Course(12, 4, 4,
                "Defense Against the Dark Arts", fourthStart, fourthEnd, CourseStatus.Dropped);
        Course mm = new Course(13, 4, 1,
                "My wand is better.... Or is it?", fourthStart, fourthEnd, CourseStatus.PlanToTake);

        Assessment aaa = new Assessment(null, 1, "Wands Exam",
                AssessmentType.Test, firstStart, firstEnd);
        Assessment bbb = new Assessment(null, 1, "Paper: 'White privilege for magical orphans'",
                AssessmentType.Paper, firstStart, firstEnd);
        Assessment ccc = new Assessment(null, 1, "Project: Make a magical Friend",
                AssessmentType.Project, firstStart, firstEnd);
        Assessment ddd = new Assessment(null, 1, "Practical: Winguardium Levi'osa",
                AssessmentType.Practical, firstStart, firstEnd);

        Assessment eee = new Assessment(null, 9, "Manly Grooming Exam",
                AssessmentType.Test, thirdStart, thirdEnd);
        Assessment fff = new Assessment(null, 9,
                "Project: Women and Beards(inclusion matters)", AssessmentType.Project,
                thirdStart, thirdEnd);

        Note abc = new Note(null, 2, "Note 1", "I like making notes");
        Note def = new Note(null, 2, "Note2", "This teacher is a Witch!" );

        //Add test data to the database.
        db.courseDAO().insertAll(aa,bb,cc,dd,ee,ff,gg,hh,ii,jj,kk,ll,mm);
        db.AssessmentDAO().insertAll(aaa,bbb,ccc,ddd,eee,fff);
        db.termDAO().insertAll(a,b,c,d);
        db.mentorDAO().insertAll(dumbledore, mcGonagall, hagrid, snape);
        db.noteDAO().insertAll(abc,def);
    }

    public void deleteDataBase(MenuItem item){
        System.out.println("testingDelete");
        AppDatabase db = AppDatabase.getDbInstance(this.getApplicationContext());
        db.termDAO().deleteAllRows();
        db.courseDAO().deleteAllRows();
        db.mentorDAO().deleteAllRows();
        db.AssessmentDAO().deleteAllRows();
        db.noteDAO().deleteAllRows();

    }
}