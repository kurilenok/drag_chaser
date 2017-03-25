package org.arcoiris.dragchaser.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.arcoiris.dragchaser.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewQueenActivityFragment extends Fragment {

    public NewQueenActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_queen, container, false);
    }
}
