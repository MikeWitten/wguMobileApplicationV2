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

import entities.Term;

public class TermListAdapter extends RecyclerView.Adapter<TermListAdapter.termViewHolder> {

    private final Context context;
    private final List<Term> termsList;
    private final OnTermListener mOnTermListener;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    public TermListAdapter(Context context, List<Term> termsList, OnTermListener mOnTermListener) {
        this.context = context;
        this.termsList = termsList;
        this.mOnTermListener = mOnTermListener;
    }

    @NonNull
    @Override
    public termViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.term_recycler_row, parent, false);

        return new termViewHolder(view, mOnTermListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TermListAdapter.termViewHolder holder, int position) {
        holder.columnTermName.setText(this.termsList.get(position).termName);
        holder.columnStartDate.setText(this.termsList.get(position).startDate.format(formatter));
        holder.columnEndDate.setText(this.termsList.get(position).endDate.format(formatter));
    }

    @Override
    public int getItemCount() {
        return this.termsList.size();
    }

    public static class termViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView columnTermName;
        TextView columnStartDate;
        TextView columnEndDate;
        OnTermListener onTermListener;

       public termViewHolder(View view, OnTermListener onTermListener){
           super(view);
           columnTermName = view.findViewById(R.id.columnTermName);
           columnStartDate = view.findViewById(R.id.columnStartDate);
           columnEndDate = view.findViewById(R.id.columnEndDate);
           this.onTermListener = onTermListener;

           itemView.setOnClickListener(this);
       }

        @Override
        public void onClick(View v) {
            onTermListener.onTermClick(getAdapterPosition());
        }
    }

    public interface OnTermListener{
        void onTermClick(int position);
    }
}//FIXME
