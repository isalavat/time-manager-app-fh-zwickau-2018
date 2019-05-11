package de.fh_zwickau.tm.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.fh_zwickau.tm.R;

public class TMQStartDialogFragment extends DialogFragment implements View.OnClickListener{

    private Button startButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tmq_start_layout, null);
        this.getDialog().setCancelable(false);
        this.getDialog().setTitle("TITITLe");
        startButton = view.findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
