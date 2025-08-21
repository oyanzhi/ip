import java.lang.Math;

public final class ConsoleDisplayStyle {
    static final int MINIMUM_LENGTH = 50;
    static final String CHAR = "-";

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

    public static void printIndentation(int spacing) {
        for (int i = 0; i < spacing; i++) {
            System.out.print(" ");
        }
    }

    public static void printBasicStyling(int spacing, int length, String defaultText) {
        printHorizontalLine(spacing, length);
        printIndentation(spacing);
        System.out.println(defaultText);
        printHorizontalLine(spacing, length);
    }

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