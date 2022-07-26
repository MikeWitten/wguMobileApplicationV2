package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wittenPortfolio.R;

import java.util.List;

import entities.Mentor;

public class MentorListAdapter extends RecyclerView.Adapter<MentorListAdapter.mentorViewHolder> {

    private Context context;
    private List<Mentor> mentorsList;
    private OnMentorListener mOnMentorListener;

    public MentorListAdapter(Context context, List<Mentor> mentorsList, OnMentorListener onMentorListener){
        this.context = context;
        this.mentorsList = mentorsList;
        this.mOnMentorListener = onMentorListener;
    }

    @NonNull
    @Override
    public mentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mentor_recycler_row, parent, false);

        return new mentorViewHolder(view, mOnMentorListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorListAdapter.mentorViewHolder holder, int position) {
        holder.mentorNameColumn2.setText(this.mentorsList.get(position).name);
        holder.mentorPhoneColumn.setText(this.mentorsList.get(position).phone);
        holder.mentorEmailColumn.setText(this.mentorsList.get(position).email);
    }

    @Override
    public int getItemCount() {
        return this.mentorsList.size();
    }

    public class mentorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mentorNameColumn2;
        TextView mentorPhoneColumn;
        TextView mentorEmailColumn;
        OnMentorListener onMentorListener;

        public mentorViewHolder(@NonNull View view, OnMentorListener onMentorListener) {
            super(view);
            mentorNameColumn2 = view.findViewById(R.id.mentorNameColumn2);
            mentorPhoneColumn = view.findViewById(R.id.mentorPhoneColumn);
            mentorEmailColumn = view.findViewById(R.id.mentorEmailColumn);
            this.onMentorListener = onMentorListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onMentorListener.onMentorClick(getAdapterPosition());
        }
    }
    public interface OnMentorListener{
        void onMentorClick(int position);
    }
}
