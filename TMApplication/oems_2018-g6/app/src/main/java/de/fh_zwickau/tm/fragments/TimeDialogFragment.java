package de.fh_zwickau.tm.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimeDialogFragment extends DialogFragment {
    private boolean startTime = true;
    private Calendar calendar;
    private int hour;
    private int minute;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private OnBeginAndEndTimeSetListener onBeginAndEndTimeSetListener;
 //   private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    public interface OnBeginAndEndTimeSetListener{
        void onBeginOrEntTimeSet(int hourOfDay, int minute, boolean isBeginTime);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        if(!startTime){
            hour+=1;
        }

        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay_, int minute_) {
                setTimeLabelOnActivity(hourOfDay_, minute_);
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                timeSetListener,
                hour,
                minute,
                true);

        return timePickerDialog;
    }

    public boolean isStartTime() {
        return startTime;
    }

    public void setStartTime(boolean startTime) {
        this.startTime = startTime;
    }

    public void setTimeLabelOnActivity(int hourOfDay, int minute){
        onBeginAndEndTimeSetListener.onBeginOrEntTimeSet(hourOfDay, minute, startTime);
    }

    public void setOnBeginAndEndTimeSetListener(OnBeginAndEndTimeSetListener onBeginAndEndTimeSetListener) {
        this.onBeginAndEndTimeSetListener = onBeginAndEndTimeSetListener;
    }
}
