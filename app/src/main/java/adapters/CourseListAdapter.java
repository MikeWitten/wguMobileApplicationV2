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

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.coursesViewHolder> {

    private final Context context;
    private final List<Course> courseList;
    private final OnCourseListener mOnCourseListener;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Create a constructor for the adapter.
     */
    public CourseListAdapter(Context context, List<Course> courseList, OnCourseListener onCourseListener) {
        this.context = context;
        this.courseList = courseList;
        this.mOnCourseListener = onCourseListener;
    }

    @NonNull
    @Override
    public coursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.course_recycler_row, parent, false);
        //Pass the listener along with the view in order to select the items.
        return new coursesViewHolder(view, mOnCourseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListAdapter.coursesViewHolder holder, int position) {
        //Get database lists in order to populate term and mentor names.
        AppDatabase db = AppDatabase.getDbInstance(context);
        List<Term> termsList = db.termDAO().getAllTerms();
        List<Mentor> mentorsList = db.mentorDAO().getAllMentors();
        String termName = null;
        String mentorName = null;
        //populate correct names for each course.
        for(Term t: termsList){
            if(Objects.equals(this.courseList.get(position).termID, t.termID)){
                termName = t.termName;
            }
        }
        for(Mentor m: mentorsList){
            if (Objects.equals(this.courseList.get(position).mentorID, m.id)){
                mentorName = m.name;
            }
        }

        holder.courseNameColumn.setText(this.courseList.get(position).courseName);
        holder.termNameColumn.setText(termName);
        holder.mentorNameColumn.setText(mentorName);
        holder.courseStatusColumn.setText(this.courseList.get(position).status.toString());
        holder.courseStartDateColumn.setText(formatter.format(this.courseList.get(position).startDate));
        holder.courseEndDateColumn.setText(formatter.format(this.courseList.get(position).endDate));

    }

    @Override
    public int getItemCount() {
        return this.courseList.size();
    }

    /**
     * Create a view holder to pass the column information as well as the listener information.
     */
    public static class coursesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView courseNameColumn;
        TextView termNameColumn;
        TextView mentorNameColumn;
        TextView courseStatusColumn;
        TextView courseStartDateColumn;
        TextView courseEndDateColumn;
        OnCourseListener onCourseListener;

        public coursesViewHolder(@NonNull View itemView, OnCourseListener onCourseListener) {
            super(itemView);
            courseNameColumn = itemView.findViewById(R.id.courseNameColumn);
            termNameColumn = itemView.findViewById(R.id.termNameColumn);
            mentorNameColumn = itemView.findViewById(R.id.mentorNameColumn);
            courseStatusColumn = itemView.findViewById(R.id.courseStatusColumn);
            courseStartDateColumn = itemView.findViewById(R.id.courseStartDateColumn);
            courseEndDateColumn = itemView.findViewById(R.id.courseEndDateColumn);
            this.onCourseListener = onCourseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCourseListener.onCourseClick(getAdapterPosition());
        }
    }

    /**
     * Create an on click interface.
     */
    public interface OnCourseListener{
        void onCourseClick(int position);
    }
}
