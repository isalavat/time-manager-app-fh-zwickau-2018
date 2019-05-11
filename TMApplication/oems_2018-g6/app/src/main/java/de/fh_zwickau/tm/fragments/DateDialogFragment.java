package de.fh_zwickau.tm.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DateDialogFragment extends DialogFragment {
    private boolean beginTime;
    private Calendar calendar;
    private int year;
    private int month;
    private int dayOfWeek;
    private DatePickerDialog.OnDateSetListener onDateSetListener;



    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfWeek = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                onDateSetListener,
                year,
                month,
                dayOfWeek);
        return datePickerDialog;
    }

    public boolean isBeginTime() {
        return beginTime;
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }
}
