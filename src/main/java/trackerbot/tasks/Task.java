package trackerbot.tasks;

/**
 * An abstract superclass for all the types of task to inherit from
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with description and defaults to uncompleted status
     * @param description Description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * A method that returns either "X" or " " based on completion status of the task
     * @return Either "X" for completed or " " for uncompleted task
     */
    public String getStatusIcon() {
        return (this.isDone ? "X" : " "); //mark with X when done
    }

    /**
     * A method that marks the task as completed
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * A method that marks the task as uncompleted
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * A method that matches current task's description with a specific description
     * @param toSearchDescription
     * @return
     */
    public boolean checkDescription(String toSearchDescription) {
        return this.description.contains(toSearchDescription);
    }

    public abstract int compareTo(Task t);
;
    /**
     * {@inheritDoc}
     * @return A string representation of the task in the form [ ][isCompleted] Task Description
     */
    public String toString() {
        return String.format("[ ][%s] %s", this.getStatusIcon(), this.description);
    }
}
