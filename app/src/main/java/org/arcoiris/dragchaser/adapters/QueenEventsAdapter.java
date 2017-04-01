package org.arcoiris.dragchaser.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.QueenFragment;
import org.arcoiris.dragchaser.models.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class QueenEventsAdapter extends RecyclerView.Adapter<QueenEventsAdapter.ViewHolder> {

    private List<Event> events = new ArrayList<>();
    private final QueenFragment.OnQueenEventClickListener listener;

    public QueenEventsAdapter(Map<String, String> eventsMap, QueenFragment.OnQueenEventClickListener listener) {
        if (eventsMap != null && !eventsMap.isEmpty()) {
            for (Map.Entry<String, String> entry : eventsMap.entrySet()) {
                events.add(Event.decode(entry));
            }
            Collections.sort(events);
        }
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_queen_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Event event = events.get(position);
        holder.tvDate.setText(event.getDate());
        holder.tvTitle.setText(event.getTitle());
        holder.tvVenue.setText(event.getVenue());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onQueenEventClick(event);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvVenue)
        TextView tvVenue;

        public final View view;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
