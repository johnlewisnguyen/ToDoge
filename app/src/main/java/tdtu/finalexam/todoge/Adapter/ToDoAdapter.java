package tdtu.finalexam.todoge.Adapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tdtu.finalexam.todoge.AddNewTask;
import tdtu.finalexam.todoge.AlarmReceiver;
import tdtu.finalexam.todoge.MainActivity;
import tdtu.finalexam.todoge.Model.ToDoModel;
import tdtu.finalexam.todoge.R;
import tdtu.finalexam.todoge.Utils.DatabaseHandler;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, Activity activity) {
        this.db = db;
        this.activity = (MainActivity) activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (item.getDuedate() != null) {
                        db.updateStatus(item.getId(), 1, 0);
                        cancelAlarm(item.getId());
                    } else if (item.getDuedate() == null) {
                        db.updateStatus(item.getId(), 1, 0);
                    }
                } else {
                    if (item.getDuedate() != null) {
                        db.updateStatus(item.getId(), 0, 1);
                        setAlarm(item.getTask(), item.getDatetime(), db.getLatestID());
                    } else if (item.getDuedate() == null) {
                        db.updateStatus(item.getId(), 0, 0);
                    }
                }
            }
        });
        holder.important.setChecked(toBoolean(item.getImportant()));
        holder.important.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateImportant(item.getId(), 1);
                }
                else {
                    db.updateImportant(item.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    private boolean toBoolean(int n) {
        return n!=0;
    }

    public Context getContext() {
        return activity;
    }

    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        int id = item.getId();
        cancelAlarm(id);
        db.deleteTask(id);
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("taskDes", item.getTaskdes());
        bundle.putInt("dueStatus", item.getDuestatus());
        bundle.putString("dueDate", item.getDuedate());
        bundle.putString("date", item.getDate());
        bundle.putLong("dateTime", item.getDatetime());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox task;
        CheckBox important;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            important = view.findViewById(R.id.importanceCheckBox);
        }
    }
    public void setAlarm(String task, Long datetime, int latestid) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("TASK", task);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), latestid, intent, 0);

        alarmManager.setExact(AlarmManager.RTC, datetime, pendingIntent);
    }

    public void cancelAlarm(int id) {

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
