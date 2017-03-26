package org.arcoiris.dragchaser.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.models.Queen;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class QueenFragment extends Fragment {

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvHometown)
    TextView tvHometown;

    public QueenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queen, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void fillData(Queen queen) {
        tvName.setText(queen.getName());
        tvHometown.setText(queen.getHometown());
    }
}
