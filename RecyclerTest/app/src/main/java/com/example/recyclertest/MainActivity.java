package com.example.recyclertest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter; // Data to recyclerview adapter
    private final List<Note> noteList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private static final int Add_REQUEST_CODE = 1;
    private static final int Edit_REQUEST_CODE = 2;
    private Note note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);

        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //read json array object here
        List<Note> noteList = new ArrayList<>();
        noteList = loadjsonfilehere();
        this.setTitle("Multi Notes (" + noteList.size() + ")");
        Log.d(TAG, "onCreate: Load Json");

    }


    private List<Note> loadjsonfilehere() {
        try {
            InputStream is = getApplicationContext().
                    openFileInput(getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));
            reader.beginArray();
            while (reader.hasNext()) {
                reader.beginObject();
                Note obj = new Note();
                while (reader.hasNext()) {
                    String nameRead = reader.nextName();
                    switch (nameRead) {
                        case "name":
                            obj.setTitle(reader.nextString());
                            break;
                        case "NoteDateTime":
                            obj.setDate(reader.nextString());
                            break;
                        case "NoteDescription":
                            obj.setDescription(reader.nextString());
                            break;
                    }
                }
                reader.endObject();
                noteList.add(obj);
            }
            reader.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noteList;
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: saving note");
        saveNote();
        this.setTitle("Multi Notes (" + noteList.size() + ")");
        super.onPause();
    }

    private void saveNote() {

        try {
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginArray();
            for (int j = 0; j < noteList.size(); j++) {
                writer.beginObject(); //{ opening tag
                writer.name("name").value(noteList.get(j).getTitle());
                writer.name("NoteDateTime").value(noteList.get(j).getDate());
                writer.name("NoteDescription").value(noteList.get(j).getDescription());
                writer.endObject();  //} closing tag
            }
            writer.endArray();
            writer.close();
            Log.d(TAG, "saveNote: in the saving note");

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    @Override
    protected void onResume() {
        //saveNote();
        this.setTitle("Multi Notes (" + noteList.size() + ")");
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildAdapterPosition(v);
        Note s1 = noteList.get(pos);
        Intent intent_editNotes = new Intent(this, Edit.class);
        note = new Note();
        note.setTitle(noteList.get(pos).getTitle().toString());
        note.setDescription(noteList.get(pos).getDescription().toString());
        intent_editNotes.putExtra("note", note);
        intent_editNotes.putExtra("pos", pos);
        this.setTitle("Multi Notes (" + noteList.size() + ")");
        startActivityForResult(intent_editNotes, Edit_REQUEST_CODE);
    }

    @Override
    public boolean onLongClick(View v) {
        final int pos = recyclerView.getChildAdapterPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialog);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Note s = noteList.get(pos);
                noteList.remove(pos);
                setTitlenote();
                noteAdapter.notifyDataSetChanged();
                //saveNote();

            }


        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setMessage("Delete note \'" + noteList.get(pos).getTitle().toString() + "\'?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public void setTitlenote() {
        this.setTitle("Multi Notes (" + noteList.size() + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent_addNotes = new Intent(this, Edit.class);
                startActivityForResult(intent_addNotes, Add_REQUEST_CODE);
                return true;
            case R.id.info:
                Intent intent_info = new Intent(this, InfoPage.class);
                startActivity(intent_info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Add_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            note = (Note) data.getSerializableExtra("note");
            if (note != null) {
                noteList.add(0, note);
                noteAdapter.notifyDataSetChanged();
            }
        } else if (requestCode == Edit_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            note = (Note) data.getSerializableExtra("note");
            if (note != null) {
                int pos = data.getIntExtra("pos", -1);
                noteList.set(pos, note);
                Collections.sort(noteList);
                noteAdapter.notifyDataSetChanged();
            }
        } else if (resultCode == -22) {
            Toast.makeText(this, " The un-titled activity was not saved", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
