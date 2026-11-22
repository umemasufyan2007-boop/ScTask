package todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all task operations: add, view, update and delete.
 * @author Zainab Khalid
 */
public class TaskManager {
    private final List<Task> tasks = new ArrayList<>();
    private int nextId = 1;

    /**
     * Adds a new task with status Pending after checking the title.
     * @param title the task title (must not be empty)
     * @param description the task description
     * @return the newly created task
     * @throws IllegalArgumentException if the title is empty
     */
    public Task addTask(String title, String description) {
        validateTitle(title);
        Task task = new Task(nextId++, title.trim(),
                description == null ? "" : description.trim());
        tasks.add(task);
        return task;
    }

    /**
     * Adds a new task with a chosen status after checking the title.
     * @param title the task title (must not be empty)
     * @param description the task description
     * @param status the starting status of the task
     * @return the newly created task
     * @throws IllegalArgumentException if the title is empty
     */
    public Task addTask(String title, String description, Task.Status status) {
        Task task = addTask(title, description);
        if (status != null) {
            task.setStatus(status);
        }
        return task;
    }

    /** @return a copy of the list of all tasks */
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    /**
     * Finds a task by its id (linear search).
     * @param id the task id
     * @return the task, or null if not found
     */
    public Task findById(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    /**
     * Updates an existing task.
     * @param id id of the task to update
     * @param title new title (must not be empty)
     * @param description new description
     * @param status new status
     * @return true if updated, false if the task does not exist
     * @throws IllegalArgumentException if the title is empty
     */
    public boolean updateTask(int id, String title, String description, Task.Status status) {
        Task task = findById(id);
        if (task == null) return false;
        validateTitle(title);
        task.setTitle(title.trim());
        task.setDescription(description == null ? "" : description.trim());
        task.setStatus(status);
        return true;
    }

    /**
     * Deletes a task.
     * @param id id of the task to delete
     * @return true if deleted, false if the task does not exist
     */
    public boolean deleteTask(int id) {
        Task task = findById(id);
        return task != null && tasks.remove(task);
    }

    /**
     * Checks that the title is not empty.
     * @param title the title to check
     * @throws IllegalArgumentException if the title is null or empty
     */
    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
    }
}