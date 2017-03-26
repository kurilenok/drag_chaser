package org.arcoiris.dragchaser.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.models.Queen;
import org.arcoiris.dragchaser.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class EditQueenFragment extends Fragment {

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etHometown)
    EditText etHometown;

    @BindView(R.id.bSubmit)
    Button bSubmit;

    private String key;
    private String name;
    private ProgressDialog dialog;

    public EditQueenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_queen, container, false);
        ButterKnife.bind(this, view);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Connecting..");

        return view;
    }

    @OnClick(R.id.bSubmit)
    public void onSubmitClick() {
        if (!isValidInput()) return;

        dialog.show();

        name = etName.getText().toString().trim();
        String hometown = etHometown.getText().toString().trim();

        Queen queen = new Queen(name, hometown);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference("queens");

        if (key == null || key.isEmpty()) {
            ref.push().setValue(queen).addOnSuccessListener(new UpdateSuccessListner());
        } else {
            ref.child(key).setValue(queen).addOnSuccessListener(new UpdateSuccessListner());
        }
    }

    private boolean isValidInput() {
        if (etName.length() == 0 || etHometown.length() == 0) {
            Utils.snack(getView(), "Both text fields should be filled in");
            return false;
        } else {
            return true;
        }
    }

    private class UpdateSuccessListner implements OnSuccessListener<Void> {
        @Override
        public void onSuccess(Void aVoid) {
            dialog.dismiss();
            String toastText = (key == null ? " was added" : " was updated");
            Utils.toast(getContext(), name + toastText);
            getActivity().finish();
        }
    }

    public void propagateQueen(Queen queen) {
        key = queen.getKey();
        name = queen.getName();

        etName.setText(queen.getName());
        etHometown.setText(queen.getHometown());
        bSubmit.setText("Update");
    }
}
