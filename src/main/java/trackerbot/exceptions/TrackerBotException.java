package trackerbot.exceptions;

/**
 * A specific type of exception created for TrackerBot
 */
public class TrackerBotException extends Exception {

    /**
     * Creates a TrackerBotException with a custom message
     * @param message The custom message to be stored by the TrackerBotException instance
     */
    public TrackerBotException(String message) {
        super(message);
    }
}
