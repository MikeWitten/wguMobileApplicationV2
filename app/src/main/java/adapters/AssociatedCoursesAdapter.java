package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wittenPortfolio.R;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import database.AppDatabase;
import entities.Course;
import entities.Mentor;
import entities.Term;

//Create an adapter for the recycler view.
public class AssociatedCoursesAdapter extends RecyclerView.Adapter<AssociatedCoursesAdapter.associatedCoursesViewHolder> {

    private final Context context;
    private final List<Course> associatedCoursesList;
    private final OnAssociatedCourseListener mOnAssociatedCourseListener;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;


    public AssociatedCoursesAdapter(Context context, List<Course> associatedCoursesList,
                                    OnAssociatedCourseListener onAssociatedCourseListener){
        this.context = context;
        this.associatedCoursesList = associatedCoursesList;
        this.mOnAssociatedCourseListener = onAssociatedCourseListener;
    }


    @NonNull
    @Override
    public associatedCoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_recycler_row, parent, false);

        return new associatedCoursesViewHolder(view, mOnAssociatedCourseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AssociatedCoursesAdapter.associatedCoursesViewHolder holder, int position) {
        //Get database lists in order to populate the names of Terms and Mentors.
        AppDatabase db = AppDatabase.getDbInstance(context);
        List<Term> termsList = db.termDAO().getAllTerms();
        List<Mentor>mentorsList = db.mentorDAO().getAllMentors();
        String termName = null;
        String mentorName = null;
        for (Term t: termsList){
            if(Objects.equals(this.associatedCoursesList.get(position).termID, t.termID)){
                termName = t.termName;
            }
        }
        for (Mentor m: mentorsList){
            if(Objects.equals(this.associatedCoursesList.get(position).mentorID, m.id)){
                mentorName = m.name;
            }
        }
        //Populate the columns.
        holder.courseNameColumn.setText(this.associatedCoursesList.get(position).courseName);
        holder.termNameColumn.setText(termName);
        holder.mentorNameColumn.setText(mentorName);
        holder.courseStatusColumn.setText(this.associatedCoursesList.get(position).status.name());
        holder.courseStartDateColumn.setText(formatter.format(this.associatedCoursesList.get(position).startDate));
        holder.courseEndDateColumn.setText(formatter.format(this.associatedCoursesList.get(position).endDate));
    }

    @Override
    public int getItemCount() {
        return  this.associatedCoursesList.size();
    }

    public static class associatedCoursesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView courseNameColumn;
        TextView termNameColumn;
        TextView mentorNameColumn;
        TextView courseStatusColumn;
        TextView courseStartDateColumn;
        TextView courseEndDateColumn;
        OnAssociatedCourseListener onAssociatedCourseListener;

        public associatedCoursesViewHolder(View view, OnAssociatedCourseListener onAssociatedCourseListener){
            super(view);
            courseNameColumn = view.findViewById(R.id.courseNameColumn);
            termNameColumn = view.findViewById(R.id.termNameColumn);
            mentorNameColumn = view.findViewById(R.id.mentorNameColumn);
            courseStatusColumn = view.findViewById(R.id.courseStatusColumn);
            courseStartDateColumn = view.findViewById(R.id.courseStartDateColumn);
            courseEndDateColumn = view.findViewById(R.id.courseEndDateColumn);
            this.onAssociatedCourseListener = onAssociatedCourseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAssociatedCourseListener.onAssociatedCourseClick(getAdapterPosition());
        }
    }


    //Create an interface for the listener.
    public interface OnAssociatedCourseListener{
        void onAssociatedCourseClick(int position);
    }
}
