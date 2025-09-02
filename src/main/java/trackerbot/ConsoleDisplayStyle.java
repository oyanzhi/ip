package trackerbot;

import java.lang.Math;

/**
 * Is a abstract class used for printing and styling texts in the console
 */
public abstract class ConsoleDisplayStyle {
    static final int MINIMUM_LENGTH = 50;
    static final String CHAR = "-";

    /**
     * Prints a horizontal line into console
     * @param spacing Indentation of the line from the left
     * @param length Length of horizontal line as represented by "-"
     */
    public static void printHorizontalLine(int spacing, int length) {
        int minimumLineLength = Math.max(length, MINIMUM_LENGTH);

        System.out.println();

        for (int i = 0; i < spacing; i++) {
            System.out.print(" ");
        }

        for (int j = 0; j < minimumLineLength; j++) {
            System.out.print(CHAR);
        }

        System.out.println();
    }

    /**
     * Prints spacing before a line in the console
     * @param spacing Number of " " before the line
     */
    public static void printIndentation(int spacing) {
        for (int i = 0; i < spacing; i++) {
            System.out.print(" ");
        }
    }

    /**
     * Prints a specific String in the correct style
     * @param spacing Spacing before the text unit
     * @param length Length of horizontal line separator
     * @param defaultText String to be printed
     */
    public static void printBasicStyling(int spacing, int length, String defaultText) {
        printHorizontalLine(spacing, length);
        printIndentation(spacing);
        System.out.println(defaultText);
        printHorizontalLine(spacing, length);
    }

    /**
     * Prints a Task in the correct style
     * @param indentation Spacing before the text unit
     * @param maxInputLength Length required of the horizontal line
     * @param index Index of the task in the list
     * @param type Type of the Task - TODO, Deadline, Event, etc
     * @param isCompleted Whether the task is Completed
     * @param taskDescription Description of the task
     */
    public static void printTaskStyling( //for printing from storage - unused at this stage
            int indentation, int maxInputLength, int index, String type, String isCompleted, String taskDescription) {
        ConsoleDisplayStyle.printIndentation(indentation);
        String oneRow = String.format("%d.[%s][%s] %s", index, type, isCompleted, taskDescription);
        System.out.println(oneRow);
        ConsoleDisplayStyle.printHorizontalLine(indentation, maxInputLength);
    }

    /**
     * Prints additional informational text as related to individual commands
     * @param criteria Type of command as represented by a string
     * @param spacing Spacing before the text unit
     * @param length
     * @param task
     */
    public static void printCommandStyling(String criteria, int spacing, int length, Task task) {
        //print default text
        ConsoleDisplayStyle.printHorizontalLine(spacing, length); //7 to style
        ConsoleDisplayStyle.printIndentation(spacing);

        switch (criteria) {
        case "mark":
            System.out.println("Nice! I've marked this task as done:");
            break;

        case "unmark":
            System.out.println("OK, I've marked this task as not done yet:");
            break;

        case "delete":
            System.out.println("OK, I've removed this task:");
            break;

        case "addTask":
            System.out.println("Got it. I've added this task:");
            break;
        }
        //print task specific text
        ConsoleDisplayStyle.printIndentation(spacing + 2);
        System.out.println(task);
        ConsoleDisplayStyle.printHorizontalLine(spacing, length);
    }


}