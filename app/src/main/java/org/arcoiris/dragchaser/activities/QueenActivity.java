package org.arcoiris.dragchaser.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.QueenFragment;
import org.arcoiris.dragchaser.models.Queen;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QueenActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Queen queen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queen);
        ButterKnife.bind(this);

        setupToolbar();

        findQueenInFirebase();

    }

    private void findQueenInFirebase() {
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference("queens").child(key);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                queen = dataSnapshot.getValue(Queen.class);
                getSupportActionBar().setTitle(queen.getName());
                QueenFragment queenFragment = (QueenFragment) getSupportFragmentManager()
                        .findFragmentByTag("fragment_queen");
                queenFragment.fillData(queen);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Queen Name");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
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
