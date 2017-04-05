package org.arcoiris.dragchaser.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.QueenFragment;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.models.Queen;
import org.arcoiris.dragchaser.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QueenActivity extends AppCompatActivity
        implements QueenFragment.OnQueenEventClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Queen queen;
    private String key;
    private String name;
    private ProgressDialog dialog;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queen);
        ButterKnife.bind(this);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Connecting..");

        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        getQueenByKey(key);

        setupToolbar();
    }

    private void getQueenByKey(final String key) {
        DatabaseReference queensRef = db.getReference("queens").child(key);
        queensRef.addValueEventListener(new QueenValueEventListner());
    }

    private class QueenValueEventListner implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            queen = dataSnapshot.getValue(Queen.class);
            if (queen == null) return;

            queen.setKey(key);
            name = queen.getName();

            QueenFragment fragment = (QueenFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_queen);
            fragment.fillQueenData(queen);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Queen Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onQueenEventClick(Event event) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("key", event.getKey());
        startActivity(intent);
    }

    @OnClick(R.id.fabEdit)
    public void onFabClick(View view) {
        Intent intent = new Intent(QueenActivity.this, EditQueenActivity.class);
        intent.putExtra("key", queen.getKey());
        startActivity(intent);

    }

    @OnClick(R.id.fabDelete)
    public void onFabDelete(View view) {
        Snackbar.make(view, "Delete " + name + "?", Snackbar.LENGTH_LONG)
                .setAction("yeap!", new onActionListner()).show();
    }

    private class onActionListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            dialog.show();
            DatabaseReference ref = db.getReference("queens");
            ref.child(key).removeValue().addOnSuccessListener(new RemoveSuccessListener());
        }
    }

    private class RemoveSuccessListener implements OnSuccessListener<Void> {
        @Override
        public void onSuccess(Void aVoid) {
            dialog.dismiss();
            Utils.toast(QueenActivity.this, name + " was removed");
            finish();
        }
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
