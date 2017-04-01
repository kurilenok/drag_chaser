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
import org.arcoiris.dragchaser.activities.EventActivity;
import org.arcoiris.dragchaser.adapters.EventQueensAdapter;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.models.Queen;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventFragment extends Fragment {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvVenue)
    TextView tvVenue;

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.rvEventQueens)
    RecyclerView rvEventQueens;

    private OnEventQueenClickListener listener;


    public EventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    public void fillEventData(Event event) {
        ((EventActivity) getActivity()).getSupportActionBar().setTitle(event.getTitle());

        tvTitle.setText(event.getTitle());
        tvVenue.setText(event.getVenue());
        tvDate.setText(event.getDate());

        Map<String, String> queens = event.getEventQueens();

        EventQueensAdapter adapter = new EventQueensAdapter(queens, listener);
        rvEventQueens.setAdapter(adapter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventQueenClickListener) {
            listener = (OnEventQueenClickListener) context;
        } else {
            throw new RuntimeException(context.toString() + " is OnEventQueenClickListener?");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnEventQueenClickListener {
        void onEventQueenClick(Queen queen);
    }
}
