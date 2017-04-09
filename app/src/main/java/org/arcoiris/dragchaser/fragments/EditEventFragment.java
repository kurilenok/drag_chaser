package org.arcoiris.dragchaser.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.models.Event;
import org.arcoiris.dragchaser.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditEventFragment extends Fragment {

    @BindView(R.id.etDate)
    TextView etDate;

    @BindView(R.id.etTime)
    TextView etTime;

    @BindView(R.id.etTitle)
    EditText etTitle;

    @BindView(R.id.etQueens)
    MultiAutoCompleteTextView etQueens;

    @BindView(R.id.etVenue)
    MultiAutoCompleteTextView etVenue;

    @BindView(R.id.bSubmit)
    Button bSubmit;

    private String key;
    private String title;
    private FirebaseDatabase database;

    private List<String> queenNames = new ArrayList<>();
    private ArrayAdapter<String> queenNameAdapter;
    private Map<String, String> eventQueens = new HashMap<>();
    private Map<String, String> queenDirectory = new HashMap<>();

    public EditEventFragment() {
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);
        ButterKnife.bind(this, view);

        database = FirebaseDatabase.getInstance();

        initQueenNameAdapter();

        return view;
    }

    private void initQueenNameAdapter() {
        DatabaseReference reference = database.getReference("queenNames");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    queenDirectory.put(snapshot.getKey(), snapshot.getValue(String.class));

//                    String name = snapshot.getValue(String.class);
//                    queenNames.add(name);
                }

                queenNames.addAll(queenDirectory.values());

                queenNameAdapter = new ArrayAdapter<>(
                        getContext(), android.R.layout.simple_list_item_1, queenNames);
                etQueens.setAdapter(queenNameAdapter);
                etQueens.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @OnClick(R.id.bSubmit)
    public void onSubmitClick() {
        Event event = new Event();
        event.setDate(etDate.getText().toString().trim());
        event.setTitle(etTitle.getText().toString().trim());
        event.setVenue(etVenue.getText().toString().trim());

        String eventQueenNames = etQueens.getText().toString().trim();
        String[] names = eventQueenNames.split(",");
        for (String name : names) {
            String value = name.trim();
            String queenKey = Utils.getKeyByValue(value, queenDirectory);
            if (queenKey != null)
                eventQueens.put(queenKey, value);
        }
        event.setEventQueens(eventQueens);

        DatabaseReference refEvents = database.getReference("events");

        if (key == null || key.isEmpty()) {
            DatabaseReference refNewEvent = refEvents.push();
            refNewEvent.setValue(event).addOnSuccessListener(new UpdateSuccessListner());
            propagateEventToQueens(event, refNewEvent.getKey());
        } else {
            refEvents.child(key).setValue(event).addOnSuccessListener(new UpdateSuccessListner());
            propagateEventToQueens(event, key);
        }
    }

    private void propagateEventToQueens(Event event, String eventKey) {
        DatabaseReference refQueens = database.getReference("queens");
        for (String queenKey : event.getEventQueens().keySet()) {
            refQueens.child(queenKey)
                    .child("queenEvents")
                    .child(eventKey)
                    .setValue(event.toString());
        }
    }

    public void updateViewForExistingEvent(Event existingEvent) {
        key = existingEvent.getKey();
        title = existingEvent.getTitle();

        etDate.setText(existingEvent.getDate());
        etTitle.setText(existingEvent.getTitle());
        etVenue.setText(existingEvent.getVenue());
        bSubmit.setText("Update");

        Map<String, String> queens = existingEvent.getEventQueens();
        StringBuilder eventQueenNames = new StringBuilder();
        for (Map.Entry<String, String> entry : queens.entrySet()) {
            eventQueenNames.append(entry.getValue()).append(", ");
        }
        if (eventQueenNames.length() > 0) {
            etQueens.setText(eventQueenNames.toString());
        }
    }

    private class UpdateSuccessListner implements OnSuccessListener<Void> {

        @Override
        public void onSuccess(Void aVoid) {
            Utils.toast(getContext(), "New event added");
            getActivity().finish();
        }
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
                        if (minutes < 10) {
                            min = "0" + minutes;
                        } else {
                            min = String.valueOf(minutes);
                        }
                        String aTime = new StringBuilder().append(hour).append(':')
                                .append(min).append(" ").append(timeSet).toString();

                        etTime.setText(aTime);
                    }
                },
                21, 0, false);

        timeDialog.show();
    }
}
