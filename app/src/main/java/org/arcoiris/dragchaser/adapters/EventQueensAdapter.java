package org.arcoiris.dragchaser.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.fragments.EventFragment;
import org.arcoiris.dragchaser.models.Queen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by kukolka on 4/1/2017.
 */

public class EventQueensAdapter extends RecyclerView.Adapter<EventQueensAdapter.ViewHolder> {

    private List<Queen> queens = new ArrayList<>();
    private final EventFragment.OnEventQueenClickListener listener;

    public EventQueensAdapter(Map<String, String> queensMap, EventFragment.OnEventQueenClickListener listener) {
        if (queensMap != null && !queensMap.isEmpty()) {
            for (Map.Entry<String, String> entry : queensMap.entrySet()) {
                Queen queen = new Queen();
                queen.setKey(entry.getKey());
                queen.setName(entry.getValue());
                queens.add(queen);
            }
//            Collections.sort(queens);
        }
        this.listener = listener;
    }

    @Override
    public EventQueensAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_queen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventQueensAdapter.ViewHolder holder, int position) {
        final Queen queen = queens.get(position);
        holder.tvQueen.setText(queen.getName());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onEventQueenClick(queen);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return queens.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvQueen)
        TextView tvQueen;

        public final View view;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }
}
