package org.arcoiris.dragchaser.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.EventsListFragment;
import org.arcoiris.dragchaser.fragments.QueensListFragment;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.models.Queen;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        QueensListFragment.OnQueenClickListener,
        EventsListFragment.OnEventClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupToolbar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectFragment(item);
                return false;
            }
        });
        pushFragment(EventsListFragment.newInstance(1));
        toolbar.setTitle("Events");
    }


    protected void selectFragment(MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()) {
            case R.id.bottom_navigation_events:
                pushFragment(EventsListFragment.newInstance(1));
                toolbar.setTitle("Events");
                break;
            case R.id.bottom_navigation_queens:
                pushFragment(QueensListFragment.newInstance(1));
                toolbar.setTitle("Queens");
                break;
            case R.id.bottom_navigation_venues:
//                pushFragment(new VenuesListFragment());
                toolbar.setTitle("Venues");
                break;
        }
    }

    protected void pushFragment(Fragment fragment) {
        if (fragment == null)
            return;

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager != null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (ft != null) {
                ft.replace(R.id.content, fragment);
                ft.commit();
            }
        }
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Drag Chaser");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onEventClick(Event event) {

    }

    @Override
    public void onQueenClick(Queen queen) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                drawer.openDrawer(GravityCompat.END);
                return true;
        }
        return true;
    }
}