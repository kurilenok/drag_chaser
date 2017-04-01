package org.arcoiris.dragchaser.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.activities.QueenActivity;
import org.arcoiris.dragchaser.adapters.QueenEventsAdapter;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.models.Queen;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QueenFragment extends Fragment {

    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvHometown)
    TextView tvHometown;

    @BindView(R.id.rvQueenEvents)
    RecyclerView rvQueenEvents;

    private OnQueenEventClickListener listener;

    public QueenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_queen, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void fillQueenData(Queen queen) {
        ((QueenActivity) getActivity()).getSupportActionBar().setTitle(queen.getName());

        tvName.setText(queen.getName());
        tvHometown.setText(queen.getHometown());

        Map<String, String> events = queen.getQueenEvents();

        QueenEventsAdapter adapter = new QueenEventsAdapter(events, listener);
        rvQueenEvents.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnQueenEventClickListener) {
            listener = (OnQueenEventClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " is OnQueenEventClickListener?");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnQueenEventClickListener {
        void onQueenEventClick(Event event);
    }
}
