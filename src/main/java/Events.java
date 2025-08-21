public class Events extends Task {
    protected String startDate;
    protected String endDate;

    public Events(String description, String startDate, String endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String toString() {
        return String.format("[E][%s] %s (from: %s to: %s)",
                this.getStatusIcon(),
                this.description,
                this.startDate,
                this.endDate);
    }
}
