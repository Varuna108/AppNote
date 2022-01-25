package com.android.notesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    MaterialButton addNoteBtn = findViewById(R.id.add_note);

    addNoteBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, AddNoteActivity.class));

        }
    });

        Realm realm = Realm.getDefaultInstance();

        RealmResults<Note> noteList = realm.where(Note.class).findAll().sort("createdTime", Sort.DESCENDING);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyAdapter myAdapter = new MyAdapter(new NoteClickListener() {
            @Override
            public void OnClick(Note note) {
                Toast.makeText(getApplicationContext(), note.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnDelete(Note note) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                note.deleteFromRealm();
                realm.commitTransaction();
              //  Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void OnEdit(Note note) {
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                intent.putExtra("Key_title", note.getTitle());
                intent.putExtra("Key_description", note.getDescription());
                startActivity(intent);
            }



        }, noteList);
        recyclerView.setAdapter(myAdapter);


        noteList.addChangeListener(new RealmChangeListener<RealmResults<Note>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChange(RealmResults<Note> notes) {
                myAdapter.notifyDataSetChanged();
            }
        });
    }
}