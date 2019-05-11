package de.fh_zwickau.tm.fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import de.fh_zwickau.tm.MainActivity;
import de.fh_zwickau.tm.R;
import de.fh_zwickau.tm.db_helper.DB_Helper;
import de.fh_zwickau.tm.domain.CustomTime;
import de.fh_zwickau.tm.domain.Priority;
import de.fh_zwickau.tm.domain.Task;

public class CreateTaskDialogFragment extends DialogFragment  implements DatePickerDialog.OnDateSetListener, TimeDialogFragment.OnBeginAndEndTimeSetListener{
    private static final String TAG="CreateTaskActivity";
    private TextView date,start_time,end_time;

    private TextView date_label, start_label, end_label;
    private EditText titleEdittext, descrip;
    private Button createButton;
    private RelativeLayout relativeLayout;
    private String prioritySelectedString;
    private Map<String,CustomTime> timeMap = new HashMap<>();
    private Spinner priority;
    private DateDialogFragment dateDialogFragment;
    private TimeDialogFragment startTimeDialogFragment;
    private TimeDialogFragment endTimeDialogFragment;
    private OnCreateTaskButtonListener onCreateTaskButtonListener;
    public static final String DATE_OF_CREATED_TASK = "de.fh-zwickau.tm.date-of-created-task";


    public interface OnCreateTaskButtonListener{
        void onCreateTaskButtonClicked(String date);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_task_layout, null);
        onCreateTaskButtonListener = (OnCreateTaskButtonListener)getActivity();
        date = view.findViewById(R.id.create_choose_date_text_view);
        start_time = view.findViewById(R.id.create_choose_st_time);
        end_time = view.findViewById(R.id.create_choose_entime);

        start_label = view.findViewById(R.id.create_start_time_text_view);
        end_label = view.findViewById(R.id.create_end_time_text_view);
        date_label = view.findViewById(R.id.create_date_text_view);

        titleEdittext = view.findViewById(R.id.create_task_title);
        descrip = view.findViewById(R.id.create_task_description_text_view);

        priority = view.findViewById(R.id.create_priority_spinner);
        createButton = view.findViewById(R.id.create_button);


        ArrayAdapter<?> priority_adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.priority, android.R.layout.simple_spinner_item);
        priority_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        priority.setAdapter(priority_adapter);

        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] choose = getResources().getStringArray(R.array.priority);
                prioritySelectedString = choose[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_Helper db_helper = MainActivity.db_helper;
                String title = titleEdittext.getText().toString();
                String descr = descrip.getText().toString();
                String date = date_label.getText().toString();
                if(date.equals("--.--.----")){
                    date = null;
                }
                String prior = prioritySelectedString.toUpperCase();
                Task task = new Task(title,descr,date,timeMap.get("start"), timeMap.get("end"), Priority.valueOf(prior));
                if(isTaskCorrect(task)) {
                    db_helper.addTask(task);
                    onCreateTaskButtonListener.onCreateTaskButtonClicked(date);
                    dismiss();
                }else{
                    Toast toast = Toast.makeText(getActivity(), "Some fields are missing", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        dateDialogFragment = new DateDialogFragment();
        dateDialogFragment.setOnDateSetListener(this);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             dateDialogFragment.show(getActivity().getSupportFragmentManager(),"Date");
            }
        });

        startTimeDialogFragment = new TimeDialogFragment();
        startTimeDialogFragment.setOnBeginAndEndTimeSetListener(this);
        startTimeDialogFragment.setStartTime(true);

        start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             startTimeDialogFragment.show(getActivity().getSupportFragmentManager(), "End time");
            }
        });

        endTimeDialogFragment = new TimeDialogFragment();
        endTimeDialogFragment.setOnBeginAndEndTimeSetListener(this);
        endTimeDialogFragment.setStartTime(false);
        end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimeDialogFragment.show(getActivity().getSupportFragmentManager(), "End time");
            }
        });

        return view;
    }

    private boolean isTaskCorrect(Task task){
        if(!task.getTitle().equals("") && !task.getDescription().equals("")
                && task.getBegin() != null && task.getEnd() != null
                && task.getDate() != null && task.getPriority() != null){
            return true;
        }

        return false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date_label.setText(dayOfMonth+"."+month+"."+year);
    }


    @Override
    public void onBeginOrEntTimeSet(int hourOfDay, int minute, boolean isBeginTime) {
        if (isBeginTime){
            start_label.setText(hourOfDay+":"+minute);
            timeMap.put("start", new CustomTime(hourOfDay, minute));
        }else {
            end_label.setText(hourOfDay+":"+minute);
            timeMap.put("end", new CustomTime(hourOfDay, minute));
        }
    }


}
