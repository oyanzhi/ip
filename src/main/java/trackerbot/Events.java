package trackerbot;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * Is a type of task that extends from Task with an additional start date and end date
 */
public class Events extends Task {
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;

    /**
     * Creates a event task with the following details
     * @param description Description of the task
     * @param startDate Start date of the event
     * @param endDate End date of the event
     * @throws TrackerBotException Error thrown when either date cannot be parsed due to incorrect format
     */
    public Events(String description, String startDate, String endDate) throws TrackerBotException {
        super(description);
        try {
            DateTimeFormatter format = new DateTimeFormatterBuilder()
                    .optionalStart()
                    .appendPattern("[yyyy-MM-dd HHmm]")
                    .appendPattern("[MMM dd yyyy HHmm]")
                    .optionalEnd()
                    .toFormatter();
            this.startDate = LocalDateTime.parse(startDate, format);
            this.endDate = LocalDateTime.parse(endDate, format);
        } catch (DateTimeException e) {
            throw new TrackerBotException("Invalid Date-Time. Please Use YYYY-MM-DD HHMM or MMM DD YYYY HHmm");
        }
    }

    /**
     * {@inheritDoc}
     * A string representation of the event given by [E][isCompleted] Description (from: start date to: end date)
     */
    @Override
    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        return String.format("[E][%s] %s (from: %s to: %s)",
                this.getStatusIcon(),
                this.description,
                this.startDate.format(format),
                this.endDate.format(format));
    }
}
