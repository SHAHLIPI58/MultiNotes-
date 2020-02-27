package com.example.recyclertest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder> {
    private static final String TAG = "NoteAdapter";
    private List<Note> noteList;
    private MainActivity ma;

    public NoteAdapter(List<Note> noteList, MainActivity ma) {
        this.noteList = noteList;
        this.ma = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_entry, parent, false);

        itemView.setOnClickListener(ma);
        itemView.setOnLongClickListener(ma);

        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note s = noteList.get(position);
        holder.NoteTitle.setText(s.getTitle());
        String descr = s.getDescription();
        if (descr.length() > 80) {
            descr = descr.substring(0, 79);
            descr = descr + "...";
        }
        holder.NoteDescription.setText(descr);
        holder.NoteDateTime.setText(s.getDate().toString());

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
