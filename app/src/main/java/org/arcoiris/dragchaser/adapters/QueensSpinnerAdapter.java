package org.arcoiris.dragchaser.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.models.Queen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kukolka on 4/1/2017.
 */

@Deprecated
public class QueensSpinnerAdapter extends ArrayAdapter<Queen> {

    private Context context;
    private List<Queen> queens = new ArrayList<>();

    public QueensSpinnerAdapter(Context context, int textViewResourceId, List<Queen> queens) {
        super(context, textViewResourceId, queens);
        this.context = context;
        this.queens = queens;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.item_spinner_queen, parent, false);
        TextView label = (TextView) row.findViewById(R.id.tvName);
        label.setText(queens.get(position).getName());

        return row;
    }
}
