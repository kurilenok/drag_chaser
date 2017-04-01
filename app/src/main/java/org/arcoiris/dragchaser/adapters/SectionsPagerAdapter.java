package org.arcoiris.dragchaser.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.arcoiris.dragchaser.fragments.EventsListFragment;
import org.arcoiris.dragchaser.fragments.QueensListFragment;

/**
 * Created by kukolka on 3/25/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EventsListFragment.newInstance(1);
            case 1:
                return QueensListFragment.newInstance(1);
            case 2:
                return QueensListFragment.newInstance(1);
        }
        return EventsListFragment.newInstance(1);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Events";
            case 1:
                return "Queens";
            case 2:
                return "Venues";
        }
        return null;
    }
}
