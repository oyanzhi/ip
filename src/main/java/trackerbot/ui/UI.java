package trackerbot.ui;

import trackerbot.TrackerBot;
import trackerbot.tasks.Task;

/**
 * A class to maintain most of the interactions of the bot in the console
 */
public class UI {
    private final String name = "TrackerBot";

    /**
     * Prints the initial greeting of the bot into the console
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
     * Prints the ending text of the bot into the console
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
    public String printTask(String command, int inputLength, int maxInputLength, Task taskTarget, boolean isConsole) {
        if (isConsole) {
            ConsoleDisplayStyle.printCommandStyling(command,
                    inputLength,
                    maxInputLength + 7,
                    taskTarget);
            return null;
        } else {
            String message = "";
            switch (command) {
            case "mark":
                message = String.format("OK! I've marked this task as done! \n %s", taskTarget);
                break;

            case "unmark":
                message = String.format("OK! I've marked this task as undone! \n %s", taskTarget);
                break;

            case "delete":
                message = String.format("OK! I've deleted this task! \n %s", taskTarget);
                break;

            case "addTask":
                message = String.format("OK! I've added this task! \n %s", taskTarget);
                break;

            default:
                //added for style purposes
                break;
            }
            return message;
        }

    }

    /**
     * Prints the error message in the correct format into the console
     * @param command String representation of the user command
     * @param inputLength Length of the user input used for indentation
     */
    public String printErrorMessage(TrackerBot.Commands command, int inputLength, boolean isConsole) {
        String message = "";
        switch (command) {
        case MARK:
            message = "Failed to Mark in File. Please try again.";
            break;

        case UNMARK:
            message = "Failed to Unmark in File. Please try again.";
            break;

        case DELETE:
            message = "Failed to Delete trackerbot.Tasks.Task. Please try again.";
            break;

        case ADDTASK:
            message = "Failed to save task. Please Try Again";
            break;

        case DEFAULT:
            message = "Missing Command!";
            break;

        default:
            //added for style purposes
            break;
        }

        return message;

    }

}
