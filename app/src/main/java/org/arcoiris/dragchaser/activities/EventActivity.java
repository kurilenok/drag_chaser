package org.arcoiris.dragchaser.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.EventFragment;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.models.Queen;
import org.arcoiris.dragchaser.utils.Utils;

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
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);

        database = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setTitle("Connecting..");

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        getEventByKey(key);

        setupToolbar();
    }

    private void getEventByKey(String key) {
        DatabaseReference eventsRef = database.getReference("events").child(key);
        eventsRef.addValueEventListener(new EventValueEventListner());
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

    @OnClick(R.id.fabEdit)
    public void onFabClick(View view) {
        Intent intent = new Intent(this, EditEventActivity.class);
        intent.putExtra("key", event.getKey());
        startActivity(intent);

    }

    @OnClick(R.id.fabDelete)
    public void onFabDelete(View view) {
        Snackbar.make(view, "Delete " + title + "?", Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.YELLOW)
                .setAction("yeap!", new onActionListner()).show();
    }

    private class onActionListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            dialog.show();

            DatabaseReference refQueens = database.getReference("queens");
            for (String queenKey : event.getEventQueens().keySet()) {
                refQueens.child(queenKey).child("queenEvents").child(key).removeValue();
            }

            DatabaseReference refEvents = database.getReference("events");
            refEvents.child(key).removeValue().addOnSuccessListener(new RemoveSuccessListener());
        }
    }

    private class RemoveSuccessListener implements OnSuccessListener<Void> {
        @Override
        public void onSuccess(Void aVoid) {
            dialog.dismiss();
            Utils.toast(EventActivity.this, title + " was removed");
            finish();
        }
    }
}
