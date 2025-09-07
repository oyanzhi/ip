package trackerbot.tasks;

/**
 * Is a type of task that extends from Task without any additional information
 */
public class ToDos extends Task {
    /**
     * Creates a ToDos Task with the description
     * @param description The description of the ToDos Task
     */
    public ToDos(String description) {
        super(description);
    }

    /**
     * {@inheritDoc}
     * @return A string representation of the ToDos Task in the form of [T][isCompleted] TaskDescription
     */
    public String toString() {
        return String.format("[T][%s] %s", this.getStatusIcon(), this.description);
    }
}
