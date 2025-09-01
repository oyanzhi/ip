package trackerbot;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Deadlines extends Task {
    protected LocalDateTime deadline;

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

    public String toString() {
        String formattedDeadline = this.deadline.format(DateTimeFormatter.ofPattern("MMM dd yyyy HHmm"));
        return String.format("[D][%s] %s (by: %s)", this.getStatusIcon(), this.description, formattedDeadline);
    }
}
