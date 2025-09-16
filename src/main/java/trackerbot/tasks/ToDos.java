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

    @Override
    public int compareTo(Task t) {
        if (t instanceof Deadlines || t instanceof Events) {
            return 1;
        }
        return this.description.compareTo(t.description);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return String.format("[T][%s] %s", this.getStatusIcon(), this.description);
    }

}
