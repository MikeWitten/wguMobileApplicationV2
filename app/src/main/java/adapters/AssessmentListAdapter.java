package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wittenPortfolio.R;

import java.time.format.DateTimeFormatter;
import java.util.List;

import activities.AssessmentDetail;
import entities.Assessment;

public class AssessmentListAdapter extends RecyclerView.Adapter<AssessmentListAdapter.assessmentsViewHolder> {

    private Context context;
    private List<Assessment> assessmentList;
    private OnAssessmentListener mOnAssessmentListener;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Create a constructor for the adapter.
     */
    public AssessmentListAdapter(Context context, List<Assessment> assessmentList, OnAssessmentListener onAssessmentListener) {
        this.context = context;
        this.assessmentList = assessmentList;
        this.mOnAssessmentListener = onAssessmentListener;
    }

    @NonNull
    @Override
    public assessmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.assessment_recycler_row, parent, false);
        //Pass the listener with the view.
        return new assessmentsViewHolder(view, mOnAssessmentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull assessmentsViewHolder holder, int position) {
        holder.columnAssTitle.setText(this.assessmentList.get(position).assTitle);
        holder.columnAssType.setText(this.assessmentList.get(position).assType.name());
        holder.columnAssStart.setText(this.assessmentList.get(position).startDate.format(formatter));
        holder.columnAssEnd.setText(this.assessmentList.get(position).endDate.format(formatter));
    }

    @Override
    public int getItemCount() {
        return this.assessmentList.size();
    }

    public class assessmentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView columnAssTitle;
        TextView columnAssType;
        TextView columnAssStart;
        TextView columnAssEnd;
        OnAssessmentListener onAssessmentListener;

        public assessmentsViewHolder(View view, OnAssessmentListener onAssessmentListener) {
            super(view);
            columnAssTitle = view.findViewById(R.id.columnAssTitle);
            columnAssType = view.findViewById(R.id.columnAssType);
            columnAssStart = view.findViewById(R.id.columnAssStart);
            columnAssEnd = view.findViewById(R.id.columnAssEnd);
            this.onAssessmentListener = onAssessmentListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAssessmentListener.onAssessmentClick(getAdapterPosition());

        }
    }

        public interface OnAssessmentListener {
        void onAssessmentClick(int position);
    }

}
