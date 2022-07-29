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

import entities.Note;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.notesViewHolder> {

    private final Context context;
    private final List<Note> noteList;
    private final OnNoteListener mOnNoteListener;

    /**
     * Create a constructor for the adapter.
     */
    public NoteListAdapter(Context context, List<Note> noteList, OnNoteListener onNoteListener) {
        this.context = context;
        this.noteList = noteList;
        this.mOnNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public notesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.note_recycler_row, parent, false);
        //Pass the listener with the view.
        return new notesViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull notesViewHolder holder, int position) {
        holder.noteTitle.setText(this.noteList.get(position).getTitle());
        holder.noteContent.setText(this.noteList.get(position).note);
    }

    @Override
    public int getItemCount() {
        return this.noteList.size();
    }

    public static class notesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView noteTitle;
        TextView noteContent;
        OnNoteListener onNoteListener;

        public notesViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);
            noteTitle = view.findViewById(R.id.noteTitle);
            noteContent = view.findViewById(R.id.noteContent);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

}