package de.fh_zwickau.tm.db_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.fh_zwickau.tm.domain.CustomTime;
import de.fh_zwickau.tm.domain.Priority;
import de.fh_zwickau.tm.domain.Task;

public class DB_Helper extends SQLiteOpenHelper implements IDatabaseHandler{


    private static final int DATABASE_VERSION = 6;
    private static final String DATABASE_NAME = "taskManager";
    private static final String TABLE_TASKS = "tasks";
    private static final String _ID = "id";
    private static final String TITLE = "title";
    private static final String DESC = "desc";
    private static final String CATEG = "category";
    private static final String _DATE = "date_";
    private static final String START_HOUR = "start_hour";
    private static final String START_MIN = "start_min";

    private static final String END_HOUR = "end_hour";
    private static final String END_MIN = "end_min";
    private static final String COMPLETED = "completed";



    private static final String TABLE_SCORE = "score_table";
    private static final String ID_SC = "id";
    private static final String SC_VALUE = "value";


    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT NOT NULL,"
                + DESC + " TEXT,"
                + CATEG +" TEXT NOT NULL,"
                +_DATE +" DATE NOT NULL,"
                +START_HOUR+" INTEGER NOT NULL,"
                +START_MIN+" INTEGER NOT NULL,"
                +END_HOUR+" INTEGER NOT NULL,"
                +END_MIN+" INTEGER NOT NULL,"
                +COMPLETED+" TEXT NOT NULL);";

        String CREATE_SCORE_TABLE = "CREATE TABLE " + TABLE_SCORE + "("
                + ID_SC + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + SC_VALUE + " INTEGER NOT NULL);";

        db.execSQL(CREATE_SCORE_TABLE);
        db.execSQL(CREATE_TASKS_TABLE);
        String insertScore = "INSERT INTO "+TABLE_SCORE+" ("+SC_VALUE+") VALUES (23)";
        db.execSQL(insertScore);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORE);
        onCreate(db);
    }

    @Override
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
       // values.put(_ID,task.getId());
        values.put(TITLE,task.getTitle());
        values.put(DESC,task.getDescription());
        values.put(CATEG,task.getPriority().toString());
        values.put(_DATE,task.getDate().toString());
        values.put(START_HOUR,task.getBegin().getHour());
        values.put(START_MIN,task.getBegin().getMinute());
        values.put(END_HOUR,task.getEnd().getHour());
        values.put(END_MIN,task.getEnd().getMinute());
        values.put(COMPLETED,String.valueOf(task.isCompleted()));
        db.insert(TABLE_TASKS,null,values);
        db.close();
    }

    @Override
    public Task getTask(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[] { _ID,
                        TITLE, DESC,CATEG,_DATE, START_HOUR,START_MIN,END_HOUR,END_MIN,COMPLETED }, _ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null){
            cursor.moveToFirst();
        }


        Task task = new Task(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(4),
                new CustomTime(cursor.getInt(5),cursor.getInt(6)),
                new CustomTime(cursor.getInt(7),cursor.getInt(8)),
                Priority.valueOf( cursor.getString(3)),
                Boolean.valueOf(cursor.getString(9))
        );



        return task;

    }

    @Override
    public List<Task> getAllTasks(String choosenDate) {
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS+" WHERE "+_DATE +" = '"+choosenDate+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {

                Task task = new Task(

                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(4),
                        new CustomTime(cursor.getInt(5),cursor.getInt(6)),
                        new CustomTime(cursor.getInt(7),cursor.getInt(8)),
                        Priority.valueOf( cursor.getString(3)),
                        Boolean.valueOf(cursor.getString(9))
                );

                taskList.add(task);

            } while (cursor.moveToNext());
        }

        return taskList;

    }

    @Override
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TITLE, task.getTitle());
        values.put(DESC, task.getDescription());
        values.put(CATEG, task.getPriority().toString());
        values.put(_DATE,task.getDate());
        values.put(START_HOUR,task.getBegin().getHour());
        values.put(START_MIN,task.getBegin().getMinute());
        values.put(END_HOUR,task.getEnd().getHour());
        values.put(END_MIN,task.getEnd().getMinute());
        values.put(COMPLETED,String.valueOf(task.isCompleted()));

        return db.update(TABLE_TASKS, values, _ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
    }

    @Override
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, _ID + " = ?", new String[] { String.valueOf(task.getId()) });
        db.close();
    }

    public int updateScore(int score){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SC_VALUE, score);
        sqLiteDatabase.insert(TABLE_SCORE,null,contentValues);
        return sqLiteDatabase.update(TABLE_SCORE,contentValues,ID_SC+" = ?",new String[] { String.valueOf(1) });
     }

     public int getScoreValue(){
         SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
         String selectQuery = "SELECT  "+SC_VALUE+" FROM " + TABLE_SCORE+" WHERE "+ID_SC +" = "+1+"";
         Cursor cursor = sqLiteDatabase.rawQuery(selectQuery, null);
         cursor.moveToFirst();
         int score = cursor.getInt(0);
         return score;
     }

}
