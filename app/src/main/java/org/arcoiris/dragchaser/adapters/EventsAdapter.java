package org.arcoiris.dragchaser.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.EventsListFragment;
import org.arcoiris.dragchaser.models.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private final List<Event> events = new ArrayList<>();
    private final EventsListFragment.OnEventClickListener eventsClickListner;


    public EventsAdapter(EventsListFragment.OnEventClickListener listener) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference queensRef = db.getReference("events");

        queensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addEvent(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                removeEvent(dataSnapshot.getKey());
                addEvent(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeEvent(dataSnapshot.getKey());
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        eventsClickListner = listener;
    }

    private void addEvent(DataSnapshot dataSnapshot) {
        Event event = dataSnapshot.getValue(Event.class);
        event.setKey(dataSnapshot.getKey());
        events.add(event);
        Collections.sort(events);
    }

    private void removeEvent(String key) {
        for (Event e : events) {
            if (key.equals(e.getKey())) {
                events.remove(e);
                break;
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Event event = events.get(position);

        String[] dateArray = event.getDate().split("/");

        holder.tvMonth.setText(resolveMonth(dateArray[0]));
        holder.tvDate.setText(dateArray[1]);
        holder.tvTitle.setText(event.getTitle());

        Map<String, String> eventQueens = event.getEventQueens();
        if (eventQueens != null && !eventQueens.isEmpty()) {
            String queens = "";
            for (Map.Entry<String, String> entry : eventQueens.entrySet()) {
                queens += entry.getValue() + ", ";
            }
            queens = queens.substring(0, queens.length() - 2);
            holder.tvEventQueens.setText(queens);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != eventsClickListner) {
                    eventsClickListner.onEventClick(event);
                }
            }
        });
    }

    private String resolveMonth(String s) {
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return months[Integer.parseInt(s)-1];
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDate)
        TextView tvDate;

        @BindView(R.id.tvMonth)
        TextView tvMonth;

        @BindView(R.id.tvTitle)
        TextView tvTitle;

        @BindView(R.id.tvEventQueens)
        TextView tvEventQueens;

        public final View view;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
