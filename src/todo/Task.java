package todo;

/**
 * Represents one task in the Task Management System.
 * @author Zainab Khalid
 */
public class Task {

    /** The allowed states of a task. */
    public enum Status {
        PENDING("Pending"), IN_PROGRESS("In Progress"), COMPLETED("Completed");

        private final String label;

        Status(String label) { this.label = label; }

        @Override
        public String toString() { return label; }
    }

    private final int id;
    private String title;
    private String description;
    private Status status;

    /**
     * Creates a new task with status Pending.
     * @param id unique id of the task
     * @param title title of the task
     * @param description description of the task
     */
    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = Status.PENDING;
    }

    /** @return the task id */
    public int getId() { return id; }

    /** @return the task title */
    public String getTitle() { return title; }

    /** @param title the new title */
    public void setTitle(String title) { this.title = title; }

    /** @return the task description */
    public String getDescription() { return description; }

    /** @param description the new description */
    public void setDescription(String description) { this.description = description; }

    /** @return the task status */
    public Status getStatus() { return status; }

    /** @param status the new status */
    public void setStatus(Status status) { this.status = status; }
}