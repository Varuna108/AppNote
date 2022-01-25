package com.android.notesapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class EditNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        String old_title = getIntent().getExtras().getString("Key_title");
        String description = getIntent().getExtras().getString("Key_description");

        EditText titleInput = findViewById(R.id.title_input);
        EditText descriptionInput = findViewById(R.id.description_input);
        MaterialButton saveBtn = findViewById(R.id.save_btn);

        titleInput.setText(old_title);
        descriptionInput.setText(description);

        Realm realm = Realm.getDefaultInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleInput.getText().toString();
                String description = descriptionInput.getText().toString();
                long createdTime = System.currentTimeMillis();
                Note note = realm.where(Note.class).equalTo("title", old_title).findFirst();

                realm.beginTransaction();
                note.setTitle(title);
                note.setDescription(description);
                realm.commitTransaction();
                Toast.makeText(getApplicationContext(), "Note edited", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
}
