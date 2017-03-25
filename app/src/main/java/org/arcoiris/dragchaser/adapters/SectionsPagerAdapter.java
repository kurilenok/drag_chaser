package org.arcoiris.dragchaser.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import org.arcoiris.dragchaser.activities.MainActivity;
import org.arcoiris.dragchaser.fragments.QueensFragment;

/**
 * Created by kukolka on 3/25/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 1) return new QueensFragment();


        return MainActivity.PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
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
