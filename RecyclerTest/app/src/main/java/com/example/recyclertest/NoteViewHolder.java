package com.example.recyclertest;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView NoteTitle;
    TextView NoteDescription;
    TextView NoteDateTime;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        NoteTitle = itemView.findViewById(R.id.Title);
        NoteDescription = itemView.findViewById(R.id.NoteDescr);
        NoteDateTime = itemView.findViewById(R.id.datetime);
    }
}
