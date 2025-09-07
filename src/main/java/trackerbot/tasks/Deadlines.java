package trackerbot.tasks;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import trackerbot.exceptions.TrackerBotException;

/**
 * Is a type of task that extends from Task with an additional deadline
 */
public class Deadlines extends Task {
    protected LocalDateTime deadline;

    /**
     * Creates a deadline task with the following details
     * @param description Description of the task
     * @param deadline Deadline of the task
     * @throws TrackerBotException Error thrown when the deadline cannot be parsed due to incorrect format
     */
    public Deadlines(String description, String deadline) throws TrackerBotException {
        super(description);

        //to add date recognition - accepts YYYY-mm-dd HHMM / MMM dd yyyy HHmm
        try {
            DateTimeFormatter format = new DateTimeFormatterBuilder()
                    .optionalStart()
                    .appendPattern("[yyyy-MM-dd HHmm]")
                    .appendPattern("[MMM dd yyyy HHmm]")
                    .optionalEnd()
                    .toFormatter();
            this.deadline = LocalDateTime.parse(deadline, format);
        } catch (DateTimeException e) {
            throw new TrackerBotException("Invalid Date-Time. Please Use YYYY-MM-DD HHMM or MMM DD YYYY HHmm");
        }
    }

    /**
     * {@inheritDoc}
     * String representation of deadline given by [D][isCompleted] TaskDescription (by: Deadline)
     */
    @Override
    public String toString() {
        String formattedDeadline = this.deadline.format(DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"));
        return String.format("[D][%s] %s (by: %s)", this.getStatusIcon(), this.description, formattedDeadline);
    }
}
