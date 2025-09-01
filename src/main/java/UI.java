public class UI {
    private final String name = "TrackerBot";

    public void greet() {
        //Initial Greeting
        ConsoleDisplayStyle.printHorizontalLine(0, 0);
        System.out.println("Hello! I'm "
                + this.name
                + "\n"
                + "What can I do for you?");
        ConsoleDisplayStyle.printHorizontalLine(0, 0);
    }

    public void sayBye() {
        String defaultExitText = "Bye. Hope to see you again soon!";
        ConsoleDisplayStyle.printBasicStyling(3, 0, defaultExitText);
    }

    public void printTask(String command, int inputLength, int maxInputLength, Task taskTarget) {
        ConsoleDisplayStyle.printCommandStyling(command,
                inputLength,
                maxInputLength + 7,
                taskTarget);
    }

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
            message = "Failed to Delete Task. Please try again.";
            ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
            break;

        case ADDTASK:
            message = "Failed to save task. Please Try Again";
            ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(),  message);
            break;

        case DEFAULT:
            message = "Missing Command!";
            ConsoleDisplayStyle.printBasicStyling(inputLength, inputLength + 7, message);
            break;
        }
    }

}
