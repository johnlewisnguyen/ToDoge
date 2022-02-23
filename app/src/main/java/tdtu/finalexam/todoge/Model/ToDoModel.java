package tdtu.finalexam.todoge.Model;

public class ToDoModel {
    private int id;
    private int status;
    private int dueStatus;
    private int important;
    private String task;
    private String taskSes;
    private String dueDate;
    private long dateTime;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTaskDes() {
        return taskdes;
    }

    public void setTaskDes(String taskDes) {
        this.taskDes = taskDes;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public int getDueStatus() {
        return dueStatus;
    }

    public void setDueStatus(int dueStatus) {
        this.dueStatus = dueStatus;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }
}
