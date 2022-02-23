package tdtu.finalexam.todoge.Model;

public class ToDoModel {
    private int id;
    private int status;
    private int duestatus;
    private int important;
    private String task;
    private String taskdes;
    private String duedate;
    private long datetime;
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

    public String getTaskdes() {
        return taskdes;
    }

    public void setTaskdes(String taskdes) {
        this.taskdes = taskdes;
    }

    public String getDuedate() {
        return duedate;
    }

    public void setDuedate(String duedate) {
        this.duedate = duedate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getDuestatus() {
        return duestatus;
    }

    public void setDuestatus(int duestatus) {
        this.duestatus = duestatus;
    }

    public int getImportant() {
        return important;
    }

    public void setImportant(int important) {
        this.important = important;
    }
}
