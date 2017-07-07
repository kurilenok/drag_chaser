package org.arcoiris.dragchaser.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.adapters.SectionsPagerAdapter;
import org.arcoiris.dragchaser.fragments.EventsListFragment;
import org.arcoiris.dragchaser.fragments.QueensListFragment;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.models.Queen;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        QueensListFragment.OnQueenClickListener,
        EventsListFragment.OnEventClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.container)
    ViewPager viewPager;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    private SectionsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        ButterKnife.bind(this);

        setupToolbar();
        setupNavigationDrawer();
        setupTabPager();
    }

    private void setupTabPager() {
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
//        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Drag Chaser");
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        drawer.openDrawer(Gravity.END);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawer.isDrawerOpen(Gravity.END)) {
                    drawer.closeDrawer(Gravity.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
                return true;
            case R.id.action_settings:
                drawer.openDrawer(GravityCompat.END);
                return true;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle bottom_navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    public void onQueenClick(Queen queen) {
        Intent intent = new Intent(this, QueenActivity.class);
        intent.putExtra("key", queen.getKey());
        startActivityForResult(intent, 1);
    }

    @Override
    public void onEventClick(Event event) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("key", event.getKey());
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        viewPager.setCurrentItem(requestCode);
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Intent intent;
        switch (viewPager.getCurrentItem()) {
            case 0:
                intent = new Intent(MainActivity.this, EditEventActivity.class);
                startActivityForResult(intent, 0);
                break;
            case 1:
                intent = new Intent(MainActivity.this, EditQueenActivity.class);
                startActivityForResult(intent, 1);
                break;
            case 2:
                intent = new Intent(MainActivity.this, BottomActivity.class);
                startActivityForResult(intent, 2);
                break;
        }
    }
}
