package de.fh_zwickau.tm;

import android.animation.TimeAnimator;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fh_zwickau.tm.alarm_receiver.AlarmReceiver;
import de.fh_zwickau.tm.custom_view.HorizontalTimeLine;
import de.fh_zwickau.tm.custom_view.HourCircle;
import de.fh_zwickau.tm.custom_view.TaskRectangle;
import de.fh_zwickau.tm.custom_view.VerticalHourLine;
import de.fh_zwickau.tm.db_helper.DB_Helper;
import de.fh_zwickau.tm.domain.CustomTime;
import de.fh_zwickau.tm.domain.Priority;
import de.fh_zwickau.tm.domain.Task;
import de.fh_zwickau.tm.fragments.CreateTaskDialogFragment;
import de.fh_zwickau.tm.fragments.DateDialogFragment;
import de.fh_zwickau.tm.fragments.EditTaskDialogFragment;
import de.fh_zwickau.tm.fragments.TaskOverviewFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        DatePickerDialog.OnDateSetListener,
        View.OnClickListener,
        TaskOverviewFragment.OnTaskDeleteAndEditButtonsClicked,
        CreateTaskDialogFragment.OnCreateTaskButtonListener,
        EditTaskDialogFragment.OnSaveTaskButtonClicked {

    private NestedScrollView taskTapeScrollView;
    private TextView choosenDateTextView;
    private RelativeLayout relativeLayout;
    private Display display;
    private Point size = new Point();
    private int taskRectangleWidth;
    private int taskTapeLength = 5760;
    private CreateTaskDialogFragment createTaskDialogFragment;
    private Map<Priority, Integer> colorsMap;
    private int currentDay;
    private int currentMonth;
    private int currentYear;
    private Map<Integer, String> monthMap;
    private  HorizontalTimeLine horizontalTimeLine;
    private RelativeLayout.LayoutParams horizontalTimelineLayParams;
    public  static DB_Helper db_helper ;
    private DateDialogFragment dateDialogFragment;
    private TimeAnimator animator;
    private AlarmManager alarm;
    private Map<Integer, PendingIntent> taskIdpendingIntentMap = new HashMap<>();
    private List<Task>tasks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Oncreate","YES!");
        initializeMainActivityGUI();
    }

    private void initializeMainActivityGUI(){
        display = getWindowManager().getDefaultDisplay();
        size = new Point();
        display.getSize(size);
        taskRectangleWidth = (int) (size.x*0.85);
        alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
        db_helper = new DB_Helper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTMQActivity();
            }
        });
        setTmqScoreToActionBarTitle();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        choosenDateTextView = findViewById(R.id.current_date_text_view);
        choosenDateTextView.setTextSize(24);
        dateDialogFragment = new DateDialogFragment();
        dateDialogFragment.setOnDateSetListener(this);

        choosenDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!dateDialogFragment.isAdded()) {
                    dateDialogFragment.show(getSupportFragmentManager(), "Date");
                }
            }
        });

        relativeLayout = findViewById(R.id.tasks_rel_layout);

        monthMap = new HashMap<>();
        fillMonthMap(monthMap);

        colorsMap = new HashMap<>() ;
        fillColorsMap(colorsMap);

        taskTapeScrollView = findViewById(R.id.task_tape_scroll_view);

        Button plusButton = findViewById(R.id.button);
        createTaskDialogFragment = new CreateTaskDialogFragment();
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!createTaskDialogFragment.isAdded()) {
                     createTaskDialogFragment.show(getSupportFragmentManager(), "Task");
                 }
            }
        });

        String presentDate;
        updateCurrentDate();
        presentDate = currentDay+"."+currentMonth+"."+currentYear;
        tasks = db_helper.getAllTasks(presentDate);
        List<Task> tasksOfToday = db_helper.getAllTasks(presentDate);
        initializeTaskTape(presentDate);
        addAllTasksToNotification(tasksOfToday);
    }

    private void fillMonthMap(Map<Integer, String> monthMap){
        monthMap.put(0,"January");
        monthMap.put(1,"February");
        monthMap.put(2,"March");
        monthMap.put(3,"April");
        monthMap.put(4,"Mai");
        monthMap.put(5,"June");
        monthMap.put(6,"July");
        monthMap.put(7,"August");
        monthMap.put(8,"September");
        monthMap.put(9,"October");
        monthMap.put(10,"November");
        monthMap.put(11,"December");
    }

    private void fillColorsMap(Map<Priority, Integer> colorsMap){
        colorsMap.put(Priority.URGENT_AND_IMPORTANT, Color.rgb(253, 170, 0));
        colorsMap.put(Priority.URGENT_AND_NOT_IMPORTANT, Color.rgb(124, 232, 0));
        colorsMap.put(Priority.NOT_URGENT_AND_IMPORTANT, Color.rgb(138, 131, 230));
        colorsMap.put(Priority.NOT_URGENT_AND_NOT_IMPORTANT, Color.rgb(254, 135, 117));
    }

    private void updateCurrentDate(){
        currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    private void setTmqScoreToActionBarTitle(){
       getSupportActionBar().setTitle(getResources().getString(R.string.tmq_score_text)+" "+db_helper.getScoreValue()+"%");
    }

    private void initializeTaskTape(String choosenDate){
        addVerticalHourLine();
        setCurrentDateTextView(choosenDate);
        addAllTasksOfGivenDate(choosenDate);

        if(isCurrentDate(choosenDate)) {
            addHorizontalTimeLine();
        }else{
            relativeLayout.removeView(horizontalTimeLine);
        }
        addHourCircles();
        addHourTextViews();
    }

    private void addVerticalHourLine(){
        int verticalHourLineWidth = 20;
        VerticalHourLine verticalHourLine = new VerticalHourLine(this, verticalHourLineWidth, taskTapeLength);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(20, taskTapeLength);
        layoutParams.topMargin = 0;
        layoutParams.leftMargin = 20;
        relativeLayout.addView(verticalHourLine, layoutParams);
    }

    private void setCurrentDateTextView(String choosenDate){
        Integer[] dateUnits = StringArrayToIntegerArray(choosenDate.split("\\."));
        String currentDateText = dateUnits[0]+"-"+monthMap.get(dateUnits[1]);
        choosenDateTextView.setText(currentDateText);
    }

    private void addAllTasksOfGivenDate(String choosenDate){
        List<Task> tasks = MainActivity.db_helper.getAllTasks(choosenDate);
        addTasks(tasks);
        updateCurrentDate();
        if(isCurrentDate(choosenDate)){
            deleteAllTasksFromAlarmNotification(tasks);
            addAllTasksToNotification(tasks);
        }

    }

    private void addHorizontalTimeLine(){

        horizontalTimelineLayParams = new RelativeLayout.LayoutParams(1200, 5);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        horizontalTimelineLayParams.topMargin = minuteToTapePosition(hour*60+minute);
        horizontalTimelineLayParams.leftMargin = 40;
        horizontalTimeLine = new HorizontalTimeLine(this, 1200, 5);
        horizontalTimeLine.setLayoutParams(horizontalTimelineLayParams);

        relativeLayout.addView(horizontalTimeLine, horizontalTimelineLayParams);

        animator = new TimeAnimator();
        animator.setTimeListener(new TimeAnimator.TimeListener() {
            private int delay = 60000;
            private int counter = 0;
            private int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            private int minute = Calendar.getInstance().get(Calendar.MINUTE);
            private int iterationCountForScroll = 0;
            private int neededIterationCountForScroll = 3;

            private int tempTapePosition = 2000;

            @Override
            public void onTimeUpdate(TimeAnimator a, long total, long dt) {
                if (horizontalTimeLine.getParent() != null) {
                    if (iterationCountForScroll < neededIterationCountForScroll) {
                        int minutes = hour * 60 + minute;
                        if (minutes > 60) {
                            minutes -= 60;
                        }
                        int currentTapePosition = minuteToTapePosition(minutes);
                        taskTapeScrollView.scrollTo(0, currentTapePosition);
                        iterationCountForScroll++;
                    }
                    counter += dt;
                    if(counter >= delay){
                        counter = 0;
                        Calendar currentCalendar = Calendar.getInstance();
                        hour = currentCalendar.get(Calendar.HOUR_OF_DAY);
                        minute = currentCalendar.get(Calendar.MINUTE);

                        int positionInTaskTape = minuteToTapePosition(hour*60+minute);
                        horizontalTimelineLayParams.topMargin = positionInTaskTape;
                        if(horizontalTimelineLayParams.topMargin < taskTapeLength) {
                            relativeLayout.updateViewLayout(horizontalTimeLine, horizontalTimelineLayParams);
                        }

                    }

//                    counter += dt;
//                    if (counter >= 500) {
//
//                        horizontalTimelineLayParams.topMargin = tempTapePosition++;
//                        if (horizontalTimelineLayParams.topMargin < taskTapeLength ) {
//                            relativeLayout.updateViewLayout(horizontalTimeLine, horizontalTimelineLayParams);
//                        }
//
//                    }


                }
            }
        });

        animator.start();

    }

    private void addHourCircles(){
        int topMargin = 0;
        int step = taskTapeLength /24;

        for(int hour = 0; hour <= 24; hour++) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(80, 80);
            layoutParams.topMargin = topMargin;
            topMargin+=step;
            layoutParams.leftMargin = 0;
            HourCircle hourCircle = new HourCircle(this);
            relativeLayout.addView(hourCircle, layoutParams);
        }
    }

    private void addHourTextViews(){
        int topMargin = 0;
        int step = taskTapeLength /24;
        String zero;
        for(int hour = 0; hour <= 24; hour++) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(80, 80);
            layoutParams.topMargin = topMargin;
            topMargin+=step;
            layoutParams.leftMargin = 0;
            TextView textView =  new TextView(this);
            if(hour<10){
                zero = "0";
            }else{
                zero = "";
            }
            String hourText = zero+Integer.toString(hour);
            textView.setText(hourText);
            textView.setTextSize(16);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            textView.setTextColor(Color.WHITE);
            relativeLayout.addView(textView, layoutParams);
        }
    }

    private void addTasks(List<Task> tasks){
        if (!tasks.isEmpty()) {
            for (Task task : tasks) {
                addTask(task);
            }
        }
    }

    private void addTask(Task task){
        int beginTimeInMinute = task.getBegin().getHour()*60+task.getBegin().getMinute();
        int endTimeInMinute = task.getEnd().getHour()*60+task.getEnd().getMinute();
        int taskRectangleHeight = (endTimeInMinute-beginTimeInMinute) *4;

        RelativeLayout.LayoutParams taskRectangleLayParams = new RelativeLayout.LayoutParams(taskRectangleWidth, taskRectangleHeight);
        int taskRectangleTopMargin = taskTapeLength /(24*60) * beginTimeInMinute;
        taskRectangleLayParams.topMargin = taskRectangleTopMargin;
        taskRectangleLayParams.leftMargin = 100;

        TextView taskTitleTextView = new TextView(this);
        taskTitleTextView.setText(task.getTitle());
        taskTitleTextView.setTextSize(30);
        taskTitleTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        taskTitleTextView.setTextColor(Color.WHITE);
        TaskRectangle taskRectangleCustomView = new TaskRectangle(this, taskRectangleWidth, taskRectangleHeight, task, colorsMap.get(task.getPriority()));
        taskRectangleCustomView.setOnClickListener(this);

        relativeLayout.addView(taskRectangleCustomView, taskRectangleLayParams);
        relativeLayout.addView(taskTitleTextView, taskRectangleLayParams);

        ToggleButton toggleButton = new ToggleButton(this);
        toggleButton.setTextOn("DONE");
        toggleButton.setTextOff("TODO");
        toggleButton.setChecked(task.isCompleted());
        toggleButton.setId(task.getId());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int taskId = buttonView.getId();
                Task task = db_helper.getTask(taskId);
                task.setCompleted(isChecked);
                db_helper.updateTask(task);
            }
        });
        int toggleWidth = 220;
        int toggleHeight = 110;
        RelativeLayout.LayoutParams togButLayParams = new RelativeLayout.LayoutParams(toggleWidth, toggleHeight);
        togButLayParams.leftMargin = taskRectangleLayParams.leftMargin+ taskRectangleWidth -toggleWidth;
        togButLayParams.topMargin = taskRectangleLayParams.topMargin+taskRectangleHeight-toggleHeight;

        relativeLayout.addView(toggleButton, togButLayParams);
    }


    private boolean isCurrentDate(String date){
        updateCurrentDate();
        String [] splitedDateUnits = date.split("\\.");
        Integer[] dateUnits = StringArrayToIntegerArray(splitedDateUnits);

        if(dateUnits[0] == currentDay && dateUnits[1] == currentMonth && currentYear == dateUnits[2]){
            return true;
        }
        return false;
    }

    private boolean isCurrentTime(CustomTime time){
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        minute = minute+(hour*60);
        if(minute>=(time.getMinute()+time.getHour()*60)){
            return false;
        }
        return true;
    }

    public void notifyByStartTime(Task task){
         if(!taskIdpendingIntentMap.containsKey(task.getId()) && isCurrentTime(task.getBegin())){
            addToAlarmNotificarion(task);
        }
    }

    public void addToAlarmNotificarion(Task task){
        int notificatonId=task.getId();
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        intent.putExtra("notificationId", notificatonId);
        intent.putExtra("todo", task.getTitle());
        PendingIntent alarmIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        taskIdpendingIntentMap.put(task.getId(),alarmIntent);
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, task.getBegin().getHour());
        startTime.set(Calendar.MINUTE, task.getBegin().getMinute());
        startTime.set(Calendar.SECOND, 0);
        long alarmStartTime = startTime.getTimeInMillis();
        alarm.set(AlarmManager.RTC_WAKEUP, alarmStartTime, alarmIntent);

    }



    private void deleteAllTasksFromAlarmNotification(List<Task>tasks){
        if(!taskIdpendingIntentMap.isEmpty()){
            for(Task task : tasks){
                PendingIntent pe = taskIdpendingIntentMap.get(task.getId());
                if(pe != null) {
                    alarm.cancel(pe);
                }
            }
            taskIdpendingIntentMap.clear();
        }
    }

    private void addAllTasksToNotification(List<Task> tasks){
        if (!tasks.isEmpty()) {
            deleteAllTasksFromAlarmNotification(tasks);
            for (Task task : tasks) {
                notifyByStartTime(task);
            }
        }
    }

    private void deleteTaskFromAlarmNotification(Task task) {
        if(taskIdpendingIntentMap.containsKey(task.getId())) {
            alarm.cancel(taskIdpendingIntentMap.get(task.getId()));
            taskIdpendingIntentMap.remove(task.getId());
        }
    }

    private void resetTaskTape(String choosenDate){
        relativeLayout.removeAllViewsInLayout();
        relativeLayout.removeAllViews();
        initializeTaskTape(choosenDate);
    }

    private Integer[] StringArrayToIntegerArray(String [] date ){
        return new Integer[]{Integer.valueOf(date[0]), Integer.valueOf(date[1]), Integer.valueOf(date[2])};
    }

    private int minuteToTapePosition(int minute){
        return (taskTapeLength /(24*60))*minute;
    }

    public void toTMQActivity(){
        Intent intent = new Intent(this, TMQActivity.class);
        startActivity(intent);
    }

    private void toTMQAboutActivity() {
        Intent intent = new Intent(this,AboutActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.tmq_test:
                toTMQActivity();
                break;
            case R.id.tmq_about:
                toTMQAboutActivity();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth+"."+month+"."+year;
        resetTaskTape(date);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String choosenDate;
        updateCurrentDate();
        setTmqScoreToActionBarTitle();
        choosenDate = currentDay+"."+currentMonth+"."+currentYear;
        resetTaskTape(choosenDate);
    }

    @Override
    public void onClick(View v) {
        TaskOverviewFragment taskOverviewFragment = new TaskOverviewFragment();
        taskOverviewFragment.setTask(((TaskRectangle)v).getTask());
        taskOverviewFragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onEditButtonClicked(Task task) {
       EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment();
       editTaskDialogFragment.setTaskToEdit(task);
       editTaskDialogFragment.show(getSupportFragmentManager(), "Edit");
    }

    @Override
    public void onDeleteButtonClicked(Task task) {
        deleteTaskFromAlarmNotification(task);
        String date = task.getDate();
        db_helper.deleteTask(task);
        resetTaskTape(date);
    }

    @Override
    public void onCreateTaskButtonClicked(String date) {
        resetTaskTape(date);
    }

    @Override
    public void onSaveTaskButtonClicked(String date) {

        resetTaskTape(date);
    }
}
