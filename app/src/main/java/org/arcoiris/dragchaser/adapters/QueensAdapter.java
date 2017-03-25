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
import org.arcoiris.dragchaser.fragments.QueensListFragment.OnQueensFragmentClickListener;
import org.arcoiris.dragchaser.models.Queen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


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
        for (Queen q : queens) {
            if (key.equals(q.getKey())) {
                queens.remove(q);
                break;
            }
        }
    }

    private void addQueen(DataSnapshot dataSnapshot) {
        Queen queen = dataSnapshot.getValue(Queen.class);
        queen.setKey(dataSnapshot.getKey());
        queens.add(queen);
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
        if (events != null) eventsCount = events.size();
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

        @BindView(R.id.queen_name)
        TextView tvQueenName;

        @BindView(R.id.queen_hometown)
        TextView tvQueenHometown;

        @BindView(R.id.queen_events)
        TextView tvQueenEvents;

        public final View view;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
