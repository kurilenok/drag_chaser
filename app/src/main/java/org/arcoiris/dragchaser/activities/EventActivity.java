package org.arcoiris.dragchaser.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.EventFragment;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.models.Queen;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends AppCompatActivity
        implements EventFragment.OnEventQueenClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Event event;
    private String key;
    private String title;
    private ProgressDialog dialog;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Connecting..");

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        getEventByKey(key);

        setupToolbar();
    }

    private void getEventByKey(String key) {
        DatabaseReference eventsRef = db.getReference("events").child(key);
        eventsRef.addValueEventListener(new EventValueEventListner());
    }

    @OnClick(R.id.fabEdit)
    public void onFabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Event Title");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private class EventValueEventListner implements ValueEventListener {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            event = dataSnapshot.getValue(Event.class);
            if (event == null) return;

            event.setKey(key);
            title = event.getTitle();

            EventFragment fragment = (EventFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_event);
            fragment.fillEventData(event);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }


    @Override
    public void onEventQueenClick(Queen queen) {
        Intent intent = new Intent(this, QueenActivity.class);
        intent.putExtra("key", queen.getKey());
        startActivity(intent);
    }
}
