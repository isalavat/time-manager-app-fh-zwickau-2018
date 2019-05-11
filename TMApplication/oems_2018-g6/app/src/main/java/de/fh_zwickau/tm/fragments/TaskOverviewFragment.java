package de.fh_zwickau.tm.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import de.fh_zwickau.tm.R;
import de.fh_zwickau.tm.domain.Priority;
import de.fh_zwickau.tm.domain.Task;

public class TaskOverviewFragment extends DialogFragment implements View.OnClickListener{

    private OnTaskDeleteAndEditButtonsClicked onTaskDeleteAndEditButtonsClicked;
    private TextView title;
    private EditText description;
    private TextView priority;
    private TextView dateAndTime;
    private Button editButton;
    private Button deleteButton;
    private Map<Priority, String> priorityStringMap = new HashMap<>();
    private Task task;
    public interface OnTaskDeleteAndEditButtonsClicked{
        void onEditButtonClicked(Task task);
        void onDeleteButtonClicked(Task task);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_overview, null);
        priorityStringMap.put(Priority.URGENT_AND_NOT_IMPORTANT, "Urgent and not important");
        priorityStringMap.put(Priority.NOT_URGENT_AND_IMPORTANT, "Not urgent and important");
        priorityStringMap.put(Priority.URGENT_AND_IMPORTANT, "Urgent and important");
        priorityStringMap.put(Priority.URGENT_AND_NOT_IMPORTANT, "Urgent and not important");
        editButton = view.findViewById(R.id.edit_button);
        editButton.setOnClickListener(this);
        deleteButton = view.findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(this);
        title =  view.findViewById(R.id.title_text_view);
        title.setTextSize(24);
        description = view.findViewById(R.id.create_task_description_text_view);
        description.setKeyListener(null);
        priority = view.findViewById(R.id.priority_text_view);
        priority.setTextSize(20);
        dateAndTime = view.findViewById(R.id.date_text_view);
        dateAndTime.setTextSize(20);
        title.setText(task.getTitle());
        description.setText(task.getDescription());

        priority.setText(priorityStringMap.get(task.getPriority()));
        dateAndTime.setText(task.getDate()+" | "
                +task.getBegin().getHour()+":"+task.getBegin().getMinute()+" - "
                +task.getEnd().getHour()+":"+task.getEnd().getMinute());
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onTaskDeleteAndEditButtonsClicked = (OnTaskDeleteAndEditButtonsClicked) context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_button:
                onTaskDeleteAndEditButtonsClicked.onEditButtonClicked(task);
                dismiss();
                break;
            case R.id.delete_button:
                onTaskDeleteAndEditButtonsClicked.onDeleteButtonClicked(task);
                dismiss();
                break;
        }

    }

    public void setTask(Task task) {
        this.task = task;
    }
}

