package org.arcoiris.dragchaser.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.adapters.QueensSpinnerAdapter;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.models.Queen;
import org.arcoiris.dragchaser.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditEventFragment extends Fragment {

    @BindView(R.id.etDate)
    EditText etDate;

    @BindView(R.id.etTime)
    EditText etTime;

    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etVenue)
    MultiAutoCompleteTextView etVenue;

    @BindView(R.id.bSubmit)
    Button bSubmit;

    @BindView(R.id.sQueen1)
    Spinner spinner1;

    @BindView(R.id.sQueen2)
    Spinner spinner2;

    @BindView(R.id.sQueen3)
    Spinner spinner3;

    @BindView(R.id.tvQueen1)
    TextView tvQueen1;

    @BindView(R.id.tvQueen2)
    TextView tvQueen2;

    @BindView(R.id.tvQueen3)
    TextView tvQueen3;

    @BindView(R.id.bReset1)
    ImageButton bReset1;

    private String key;
    private String title;
    private Event event;

    final List<Queen> queens = new ArrayList<>();
    private Queen emptyQueen = new Queen(">> CHANGE");
    private Map<String, String> eventQueens = new HashMap<>();

    private QueensSpinnerAdapter adapter;

    public EditEventFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);
        ButterKnife.bind(this, view);

        initSpinnerAdapter(event);

        String[] countries = getResources().getStringArray(R.array.list_of_countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, countries);
        etVenue.setAdapter(adapter);
        etVenue.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        return view;
    }

    private void initSpinnerAdapter(final Event event) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("queenNames");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.getValue(String.class);
                    Queen queen = new Queen(name);
                    queen.setKey(snapshot.getKey());
                    queens.add(queen);
                }
                queens.add(emptyQueen);
                Collections.sort(queens);


                adapter = new QueensSpinnerAdapter(getContext(), R.layout.item_spinner_queen, queens);

                spinner1.setAdapter(adapter);
                spinner2.setAdapter(adapter);
                spinner3.setAdapter(adapter);

                spinner1.setOnItemSelectedListener(new OnSpinnerChange(tvQueen1, 1));
                spinner2.setOnItemSelectedListener(new OnSpinnerChange(tvQueen2, 2));
                spinner3.setOnItemSelectedListener(new OnSpinnerChange(tvQueen3, 3));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class OnSpinnerChange implements AdapterView.OnItemSelectedListener {

        private TextView tvQueen;
        private int number;

        public OnSpinnerChange(TextView tvQueen, int number) {
            this.tvQueen = tvQueen;
            this.number = number;
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i > 0)
                tvQueen.setText("Queen " + number + ": " + queens.get(i).getName());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }


    @OnClick(R.id.bSubmit)
    public void onSubmitClick() {
        Event event = new Event();
        event.setDate(etDate.getText().toString().trim());
        event.setTitle(etTitle.getText().toString().trim());
        event.setVenue(etVenue.getText().toString().trim());

        if (spinner3.getSelectedItemPosition() > 0) {
            Queen queen3 = (Queen) spinner3.getSelectedItem();
            eventQueens.put(queen3.getKey(), queen3.getName());
        }

        if (spinner2.getSelectedItemPosition() > 0) {
            Queen queen2 = (Queen) spinner2.getSelectedItem();
            eventQueens.put(queen2.getKey(), queen2.getName());
        }

        if (spinner1.getSelectedItemPosition() > 0) {
            Queen queen1 = (Queen) spinner1.getSelectedItem();
            eventQueens.put(queen1.getKey(), queen1.getName());
        }

        event.setEventQueens(eventQueens);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("events");

        if (key == null || key.isEmpty()) {
            reference.push().setValue(event).addOnSuccessListener(new UpdateSuccessListner());
        } else {
            reference.child(key).setValue(event).addOnSuccessListener(new UpdateSuccessListner());
        }
    }

    public void propagateEvent(Event event) {
        this.event = event;
        key = event.getKey();
        title = event.getTitle();

        etDate.setText(event.getDate());
        etTitle.setText(event.getTitle());
        etVenue.setText(event.getVenue());
        bSubmit.setText("Update");

        Map<String, String> queens = event.getEventQueens();
        TextView[] textViews = {tvQueen1, tvQueen2, tvQueen3};

        int i = 0;

        for (Map.Entry<String, String> entry : queens.entrySet()) {
            textViews[i].setText("Queen " + ++i + ": " + entry.getValue());
        }
    }

    private class UpdateSuccessListner implements OnSuccessListener<Void> {

        @Override
        public void onSuccess(Void aVoid) {
            Utils.toast(getContext(), "New event added");
            getActivity().finish();
        }
    }

    @OnClick(R.id.bReset1)
    public void onReset1Click() {
        tvQueen1.setText("Queen 1: ");
        spinner1.setSelection(0);
    }

    @OnClick(R.id.bReset2)
    public void onReset2Click() {
        tvQueen2.setText("Queen 2: ");
        spinner2.setSelection(0);
    }

    @OnClick(R.id.bReset3)
    public void onReset3Click() {
        tvQueen3.setText("Queen 3: ");
        spinner3.setSelection(0);
    }

    @OnClick({R.id.bDate, R.id.etDate})
    public void selectDate() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year1, int month1, int day1) {
                        String year = String.valueOf(year1);
                        String month = String.valueOf(month1 + 1);
                        String day = String.valueOf(day1);
                        etDate.setText(month + "/" + day + "/" + year);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        dateDialog.show();
    }

    @OnClick({R.id.bTime, R.id.etTime})
    public void selectTime() {
        TimePickerDialog timeDialog = new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        int hour = selectedHour;
                        int minutes = selectedMinute;
                        String timeSet = "";
                        if (hour > 12) {
                            hour -= 12;
                            timeSet = "PM";
                        } else if (hour == 0) {
                            hour += 12;
                            timeSet = "AM";
                        } else if (hour == 12) {
                            timeSet = "PM";
                        } else {
                            timeSet = "AM";
                        }

                        String min = "";
                        if (minutes < 10)
                            min = "0" + minutes;
                        else
                            min = String.valueOf(minutes);

                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min).append(" ").append(timeSet).toString();

                        etTime.setText(aTime);
                    }
                },
                21, 0, false);

        timeDialog.show();
    }

}
