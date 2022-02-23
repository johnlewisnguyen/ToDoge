package tdtu.finalexam.todoge;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import tdtu.finalexam.todoge.Model.ToDoModel;
import tdtu.finalexam.todoge.Utils.DatabaseHandler;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText;
    private EditText newTaskDes;
    private Button newTaskSaveButton;
    private DatabaseHandler db;
    private EditText duePicker;
    private CheckBox pickerCheckBox;

    String inputDue;
    Long inputDateTime;
    String inputDate;
    int reminder;

    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newTaskText = requireView().findViewById(R.id.newTaskText);
        newTaskDes = requireView().findViewById(R.id.newTaskDes);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);
        duePicker = getView().findViewById(R.id.duePicker);
        pickerCheckBox = getView().findViewById(R.id.pickerCheckBox);
        duePicker.setInputType(InputType.TYPE_NULL);
        boolean isUpdate = false;
        final Bundle bundle = getArguments();

        if(bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task");
            String taskDes = bundle.getString("taskdes");
            int dueStatus = bundle.getInt("duestatus");
            String dueDate = bundle.getString("duedate");

            newTaskText.setText(task);
            newTaskDes.setText(taskDes);
            
            if (dueStatus == 1) {
                duePicker.setText(duedate);
                pickerCheckBox.setChecked(true);
                duePicker.setEnabled(true);
            } else if (dueStatus == 0 && dueDate != null) {
                inputDue = dueDate;
                inputDateTime = bundle.getLong("datetime");

                duePicker.setText(dueDate);
                pickerCheckBox.setChecked(false);
                duePicker.setEnabled(false);
            }

            if(task.length()>0){
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.design_default_color_primary_dark));
            }
        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.design_default_color_primary_dark));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        duePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(duePicker);
            }
        });

        pickerCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    duePicker.setEnabled(true);
                    duePicker.setHint("Pick Date and Time");
                } else{
                    duePicker.setEnabled(false);
                    duePicker.setHint("");
                }
            }
        });

        final boolean finalIsUpdate = isUpdate;
        if (finalIsUpdate) {
            newTaskSaveButton.setText("Save");
        }

        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                String textDes = newTaskDes.getText().toString();
                String dueDate = inputDue;
                Long dateTime = inputDateTime;
                String date = inputDate;
                int dueStatus;

                if (pickerCheckBox.isChecked()) {
                    dueStatus = 1;
                } else {
                    dueStatus = 0;
                }

                if (finalIsUpdate){
                    int oldStatus = bundle.getInt("duetatus");
                    if (dueStatus == 0) {
                        if (oldStatus == 1){
                            int id = bundle.getInt("id");
                            db.updateTask(bundle.getInt("id"), text, textDes, dueStatus, bundle.getString("duedate") , bundle.getLong("datetime"), bundle.getString("date"));
                            cancelAlarm(id);
                        } else {
                            db.updateTask(bundle.getInt("id"), text, textDes, dueStatus, dueDate, dateTime, date);
                        }
                    } else if (dueStatus == 1) {
                        if (inputDue != null) {
                            db.updateTask(bundle.getInt("id"), text, textDes, dueStatus, dueDate, dateTime, date);
                            setAlarm(text, dateTime, db.getLatestID());
                        } else if (inputDue == null) {
                            db.updateTask(bundle.getInt("id"), text, textDes, 0, dueDate, bundle.getLong("datetime"), bundle.getString("date"));
                        }
                    }
                } else{
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);
                    task.setTaskdes(textDes);
                    task.setStatus(0);
                    task.setDuestatus(dueStatus);

                    if (dueStatus == 0) {
                        task.setDuedate(null);
                        task.setDatetime(0);
                        task.setDate(null);
                        db.insertTask(task);
                    } else if (dueStatus == 1 && dateTime != null) {
                        task.setDuedate(dueDate);
                        task.setDatetime(dateTime);
                        task.setDate(inputDate);
                        db.insertTask(task);
                        setAlarm(text, dateTime, db.getLatestID());
                    } else if (dueStatus == 1 && dueDate == null) {
                        task.setDuestatus(0);
                        task.setDuedate(null);
                        task.setDatetime(0);
                        task.setDate(null);
                        db.insertTask(task);
                    }
                }
                dismiss();
            }
        });
    }

    private void showDateTimeDialog(EditText duePicker) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd MMM");
                inputDate = inputDateFormat.format(calendar.getTime());

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.set(Calendar.SECOND, 0);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm EEEE, dd MMM");

                        inputDue = "at " + simpleDateFormat.format(calendar.getTime());
                        duePicker.setText(inputDue);

                        inputDateTime = calendar.getTimeInMillis();
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE), true);
                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                            pickerCheckBox.setChecked(false);
                        }
                    }
                });
                timePickerDialog.show();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, calendar.get(Calendar.DAY_OF_WEEK),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    pickerCheckBox.setChecked(false);
                }
            }
        });
        datePickerDialog.show();

    }

    public void setAlarm(String task, Long datetime, int latestid) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
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

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener) {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}


