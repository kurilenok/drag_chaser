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
import org.arcoiris.dragchaser.fragments.QueensFragment.OnQueensFragmentClickListener;
import org.arcoiris.dragchaser.models.Queen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


public class QueensAdapter extends RecyclerView.Adapter<QueensAdapter.ViewHolder> {

    private final List<Queen> queens = new ArrayList<>();
    private final OnQueensFragmentClickListener queensClickListner;

    public QueensAdapter(OnQueensFragmentClickListener listener) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference queensRef = db.getReference("queens");

        queensRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addQueen(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                removeQueen(dataSnapshot.getKey());
                addQueen(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeQueen(dataSnapshot.getKey());
                notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        queensClickListner = listener;
    }

    private void removeQueen(String key) {
        for (Queen v : queens) {
            if (key.equals(v.getKey())) {
                queens.remove(v);
                break;
            }
        }
    }

    private void addQueen(DataSnapshot dataSnapshot) {
        Queen venue = dataSnapshot.getValue(Queen.class);
        venue.setKey(dataSnapshot.getKey());
        queens.add(venue);
        Collections.sort(queens);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_queen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Queen queen = queens.get(position);
        holder.tvQueenName.setText(queen.getName());
        holder.tvQueenHometown.setText(queen.getHometown());

        int eventsCount = 0;
        Map<String, Boolean> events = queen.getQueenEvents();

        if (events != null) {
            eventsCount = events.size();
        }

        holder.tvQueenEvents.setText(Integer.toString(eventsCount));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != queensClickListner) {
                    queensClickListner.OnQueensFragmentClick(queen);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return queens.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView tvQueenName;
        public final TextView tvQueenHometown;
        public final TextView tvQueenEvents;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            tvQueenName = (TextView) view.findViewById(R.id.queen_name);
            tvQueenHometown = (TextView) view.findViewById(R.id.queen_hometown);
            tvQueenEvents = (TextView) view.findViewById(R.id.queen_events);
        }
    }
}
