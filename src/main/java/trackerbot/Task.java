package trackerbot;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (this.isDone ? "X" : " "); //mark with X when done
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public boolean checkDescription(String toSearchDescription) {
        return this.description.contains(toSearchDescription);
    }

    public String toString() {
        return String.format("[ ][%s] %s", this.getStatusIcon(), this.description);
    }
}
