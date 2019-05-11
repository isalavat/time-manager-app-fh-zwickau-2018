package de.fh_zwickau.tm.db_helper;

import java.util.List;

import de.fh_zwickau.tm.domain.Task;

public interface IDatabaseHandler {
    public void addTask(Task task);
    public Task getTask(int id);
    public List<Task> getAllTasks(String date);
    public int updateTask(Task task);

    public void deleteTask(Task task);
}
