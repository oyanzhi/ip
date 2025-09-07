package trackerbot.ui;

import trackerbot.TrackerBot;
import trackerbot.tasks.Task;

/**
 * A class to maintain most of the interactions of the bot in the console
 */
public class UI {
    private final String name = "TrackerBot";

    /**
     * A method that prints the initial greeting of the bot into the console
     */
    public void greet() {
        //Initial Greeting
        ConsoleDisplayStyle.printHorizontalLine(0, 0);
        System.out.println("Hello! I'm "
                + this.name
                + "\n"
                + "What can I do for you?");
        ConsoleDisplayStyle.printHorizontalLine(0, 0);
    }

    /**
     * A method that prints the ending text of the bot into the console
     */
    public void sayBye() {
        String defaultExitText = "Bye. Hope to see you again soon!";
        ConsoleDisplayStyle.printBasicStyling(3, 0, defaultExitText);
    }

    /**
     * Prints the task in the correct format into the console
     * @param command String representation of the user command
     * @param inputLength Length of the user input used for indentation
     * @param maxInputLength Length of the horizontal line separator
     * @param taskTarget Task that is to be printed
     */
    public void printTask(String command, int inputLength, int maxInputLength, Task taskTarget) {
        ConsoleDisplayStyle.printCommandStyling(command,
                inputLength,
                maxInputLength + 7,
                taskTarget);
    }

    /**
     * Prints the error message in the correct format into the console
     * @param command String representation of the user command
     * @param inputLength Length of the user input used for indentation
     */
    public void printErrorMessage(TrackerBot.Commands command, int inputLength) {
        String message;
        switch (command) {
        case MARK:
            message = "Failed to Mark in File. Please try again.";
            ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
            break;

        case UNMARK:
            message = "Failed to Unmark in File. Please try again.";
            ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
            break;

        case DELETE:
            message = "Failed to Delete trackerbot.Tasks.Task. Please try again.";
            ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
            break;

        case ADDTASK:
            message = "Failed to save task. Please Try Again";
            ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
            break;

        case DEFAULT:
            message = "Missing Command!";
            ConsoleDisplayStyle.printBasicStyling(inputLength, inputLength + 7, message);
            break;

        default:
            //added for style purposes
            break;
        }
    }

}
