package org.arcoiris.dragchaser.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.arcoiris.dragchaser.R;
import org.arcoiris.dragchaser.models.Queen;
import org.arcoiris.dragchaser.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewQueenFragment extends Fragment {

    public NewQueenFragment() {
    }

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etHometown)
    EditText etHometown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_queen, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.bSubmit)
    public void onSubmitClick() {
        if (etName.length() == 0 || etHometown.length() == 0) {
            Utils.showSnackbar(getView(), "Both text fields should be filled in");
            return;
        }

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.show();

        final String name = etName.getText().toString().trim();
        String hometown = etHometown.getText().toString().trim();

        Queen queen = new Queen(name, hometown);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference reference = db.getReference("queens");
        reference.push().setValue(queen)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Utils.showSnackbar(getView(), "Please welcome " + name);
                        etName.setText("");
                        etHometown.setText("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Utils.showSnackbar(getView(), "Please try again");
                    }
                });
    }
}
