package trackerbot;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class Events extends Task {
    protected LocalDateTime startDate;
    protected LocalDateTime endDate;

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

    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd yyyy HHmm");
        return String.format("[E][%s] %s (from: %s to: %s)",
                this.getStatusIcon(),
                this.description,
                this.startDate.format(format),
                this.endDate.format(format));
    }
}
