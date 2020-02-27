package com.example.recyclertest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Edit extends AppCompatActivity {
    private EditText Title;
    private EditText NoteDes;
    private Note note;
    private static final String TAG = "Edit";
    public int pos = -1;
    private int NO_Title = -22;
    String past_title;
    String past_descr;
    DateFormat df = new SimpleDateFormat("EEE MMM d, hh:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Title = findViewById(R.id.Title);
        NoteDes = findViewById(R.id.NoteDes);
        NoteDes.setMovementMethod(new ScrollingMovementMethod());
        NoteDes.setTextIsSelectable(true);

        Intent intent = getIntent();
        if (getIntent().hasExtra("note")) {
            note = (Note) intent.getSerializableExtra("note");
            pos = intent.getIntExtra("pos", -1);
            if (note != null) {
                Title.setText(note.getTitle());
                NoteDes.setText(note.getDescription());
            }
        }
        //past Title and Description content after getting intent content
        past_title = Title.getText().toString();
        past_descr = NoteDes.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.savenote, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                String titletext = Title.getText().toString();
                String notedescr = NoteDes.getText().toString();
                if (!titletext.trim().isEmpty()) {
                    if (note != null) {
                        note.setDate(df.format(Calendar.getInstance().getTime()));
                    } else {
                        note = new Note();
                        //set time here
                        note.setDate(df.format(Calendar.getInstance().getTime()));
                    }
                    note.setTitle(titletext);
                    note.setDescription(notedescr);
                    Intent data = new Intent();
                    data.putExtra("note", note);
                    data.putExtra("pos", pos);
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    Toast.makeText(this, " The un-titled activity was not saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if (!past_title.equals(Title.getText().toString()) || !past_descr.equals(NoteDes.getText().toString())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialog);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String titletext = Title.getText().toString();
                    String notedescr = NoteDes.getText().toString();
                    Intent data = new Intent();
                    if (!titletext.trim().isEmpty()) {
                        if (note != null) {
                            note.setDate(df.format(Calendar.getInstance().getTime()));
                        } else {
                            note = new Note();
                            //set time here
                            note.setDate(df.format(Calendar.getInstance().getTime()));
                        }
                        note.setTitle(titletext);
                        note.setDescription(notedescr);
                        data.putExtra("note", note);
                        data.putExtra("pos", pos);
                        setResult(RESULT_OK, data);
                    } else {
                        setResult(NO_Title, data);
                    }
                    finish();
                    Edit.super.onBackPressed();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Edit.super.onBackPressed();
                }
            });
            builder.setMessage("Your note is not saved!\nSave note \'" + Title.getText() + "\'?");
            AlertDialog dialog = builder.create();
            dialog.show();

        } else {
            super.onBackPressed();
        }
    }
}
