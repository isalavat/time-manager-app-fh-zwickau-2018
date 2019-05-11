package de.fh_zwickau.tm.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.fh_zwickau.tm.MainActivity;
import de.fh_zwickau.tm.R;

public class TMQResultDialogFragment extends DialogFragment implements View.OnClickListener {
    private int tmqscore;
    private TextView tmqscoreTextView;
    private Button okButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tmq_result_layout, null);
        tmqscoreTextView = view.findViewById(R.id.tmqscore_text_view);
        okButton = view.findViewById(R.id.ok_button);
        String scoreText = tmqscore+" %";
        tmqscoreTextView.setText(scoreText);
        okButton.setOnClickListener(this);
        return view;
    }

    public void setTmqscore(int tmqscore) {
        this.tmqscore = tmqscore;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(intent);
    }
}
