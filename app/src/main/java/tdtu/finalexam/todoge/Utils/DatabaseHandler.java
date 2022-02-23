package tdtu.finalexam.todoge.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tdtu.finalexam.todoge.Model.ToDoModel;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String TASKDES = "taskdes";
    private static final String STATUS = "status";
    private static final String DUESTATUS = "duestatus";
    private static final String DUEDATE = "due";
    private static final String DATE = "date";
    private static final String DATETIME = "datetime";
    private static final String IMPORTANT = "important";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, " + TASKDES + " TEXT, "
            + DUESTATUS + " INTEGER, "+ DUEDATE + " TEXT, " + DATETIME + " TEXT, " + DATE + " TEXT, " + IMPORTANT + " INTEGER, " + STATUS + " INTEGER)";

    private SQLiteDatabase db;
    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        //Create new table
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(TASKDES, task.getTaskdes());
        cv.put(DUESTATUS, task.getDuestatus());
        cv.put(DUEDATE, task.getDuedate());
        cv.put(DATE, task.getDate());
        cv.put(DATETIME, task.getDatetime());
        cv.put(STATUS, 0);
        cv.put(IMPORTANT, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setTaskdes(cur.getString(cur.getColumnIndex(TASKDES)));
                        task.setDuestatus(cur.getInt(cur.getColumnIndex(DUESTATUS)));
                        task.setDuedate(cur.getString(cur.getColumnIndex(DUEDATE)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setDatetime(cur.getLong(cur.getColumnIndex(DATETIME)));
                        task.setImportant(cur.getInt(cur.getColumnIndex(IMPORTANT)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    @SuppressLint("Range")
    public List<ToDoModel> getImportantTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, "IMPORTANT = 1", null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setTaskdes(cur.getString(cur.getColumnIndex(TASKDES)));
                        task.setDuestatus(cur.getInt(cur.getColumnIndex(DUESTATUS)));
                        task.setDuedate(cur.getString(cur.getColumnIndex(DUEDATE)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setDatetime(cur.getLong(cur.getColumnIndex(DATETIME)));
                        task.setImportant(cur.getInt(cur.getColumnIndex(IMPORTANT)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    @SuppressLint("Range")
    public List<ToDoModel> getScheduledTasks() {

        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, "DUESTATUS = 1", null, null, null, "DATETIME", null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setTaskdes(cur.getString(cur.getColumnIndex(TASKDES)));
                        task.setDuestatus(cur.getInt(cur.getColumnIndex(DUESTATUS)));
                        task.setDuedate(cur.getString(cur.getColumnIndex(DUEDATE)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setDatetime(cur.getLong(cur.getColumnIndex(DATETIME)));
                        task.setImportant(cur.getInt(cur.getColumnIndex(IMPORTANT)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    @SuppressLint("Range")
    public List<ToDoModel> getTodayTasks() {

        Date current = Calendar.getInstance().getTime();

        SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM");
        String today = inputDateFormat.format(current.getTime());

        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, "DATE LIKE '%" + today + "%' OR DATE is null", null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setTaskdes(cur.getString(cur.getColumnIndex(TASKDES)));
                        task.setDuestatus(cur.getInt(cur.getColumnIndex(DUESTATUS)));
                        task.setDuedate(cur.getString(cur.getColumnIndex(DUEDATE)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setDatetime(cur.getLong(cur.getColumnIndex(DATETIME)));
                        task.setImportant(cur.getInt(cur.getColumnIndex(IMPORTANT)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    @SuppressLint("Range")
    public int getLatestID() {
        String selectQuery = "SELECT * FROM "+ TODO_TABLE + " ORDER BY "+ ID +" DESC LIMIT 1;";
        Cursor cur = db.rawQuery(selectQuery, null);
        cur.moveToLast();
        int ltid = cur.getInt(cur.getColumnIndex(ID));
        return ltid;
    }


    public void updateStatus(int id, int status, int duestatus){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        cv.put(DUESTATUS, duestatus);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateImportant(int id, int important){
        ContentValues cv = new ContentValues();
        cv.put(IMPORTANT, important);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task, String taskdes, int duestatus, String duedate, Long datetime, String date) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(TASKDES, taskdes);
        cv.put(DUESTATUS, duestatus);
        cv.put(DUEDATE, duedate);
        cv.put(DATE, date);
        cv.put(DATETIME, datetime);
        db.update(TODO_TABLE, cv, ID + "=?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "=?", new String[] {String.valueOf(id)});
        int hid = id;
    }
}
