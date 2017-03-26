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
import org.arcoiris.dragchaser.fragments.EditQueenFragment;
import org.arcoiris.dragchaser.models.Queen;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditQueenActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Queen queen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_queen);
        ButterKnife.bind(this);

        getQueenByKeyIfAny();

        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add new Queen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void getQueenByKeyIfAny() {
        Intent intent = getIntent();
        final String key = intent.getStringExtra("key");

        if (key == null || key.isEmpty()) return;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference("queens").child(key);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queen = dataSnapshot.getValue(Queen.class);
                queen.setKey(key);
                getSupportActionBar().setTitle("Edit: " + queen.getName());
                EditQueenFragment nqf = (EditQueenFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_new_queen);
                nqf.propagateQueen(queen);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
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
