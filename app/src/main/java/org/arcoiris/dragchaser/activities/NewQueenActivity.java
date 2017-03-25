package org.arcoiris.dragchaser.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.arcoiris.dragchaser.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewQueenActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_queen);
        ButterKnife.bind(this);

        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add new Queen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
