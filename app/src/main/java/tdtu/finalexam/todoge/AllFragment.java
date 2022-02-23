package tdtu.finalexam.todoge;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tdtu.finalexam.todoge.Adapter.ToDoAdapter;
import tdtu.finalexam.todoge.Model.ToDoModel;
import tdtu.finalexam.todoge.Utils.DatabaseHandler;

public class AllFragment extends Fragment implements DialogCloseListener{

    private static RecyclerView tasksRecyclerView;
    private static ToDoAdapter tasksAdapter;
    private static List<ToDoModel> taskList;
    private static DatabaseHandler db;
    private static Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all, container, false);
        super.onCreate(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        db = new DatabaseHandler(getContext());
        db.openDatabase();

        activity = getActivity();

        taskList = new ArrayList<>();

        tasksRecyclerView = rootView.findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tasksAdapter = new ToDoAdapter(db, activity);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        taskList =  db.getAllTasks();
        tasksAdapter.setTasks(taskList);

        return rootView;
    }

    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

}