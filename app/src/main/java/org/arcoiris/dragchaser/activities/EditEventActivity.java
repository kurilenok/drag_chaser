package org.arcoiris.dragchaser.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.EditEventFragment;
import org.arcoiris.dragchaser.models.Event;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditEventActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        if (key != null && !key.isEmpty()) {
            getEventByKeyIfAny(key);
        }

        setupToolbar();
    }

    private void getEventByKeyIfAny(final String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("events").child(key);
        if (reference == null) return;

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                event = dataSnapshot.getValue(Event.class);
                event.setKey(key);
                getSupportActionBar().setTitle("Edit: " + event.getTitle());
                EditEventFragment fragment = (EditEventFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_edit_event);
                fragment.propagateEvent(event);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add new Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
