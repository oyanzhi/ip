package trackerbot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main class of the Console Bot
 */
public class TrackerBot {
    private FileIO f;
    private TaskList taskList;
    private UI ui;

    public enum Commands {
        MARK,
        UNMARK,
        DELETE,
        ADDTASK,
        TODO,
        DEADLINE,
        EVENT,
        BYE,
        LIST,
        FIND,
        DEFAULT,
        INVALID
    }

    /**
     * Returns an instance of the console TrackerBot with the correct settings
     */
    public TrackerBot() {
        try {
            this.f = new FileIO();
            this.taskList = this.f.readFileContents();
            this.ui = new UI();
        } catch (TrackerBotException | FileNotFoundException e) {
            ConsoleDisplayStyle.printBasicStyling(0,0, e.getMessage());
        }
    }

    /**
     * Starts running the TrackerBot in the console
     */
    public void run() {

        this.ui.greet();

        //Bot in Process
        Scanner inputScanner = new Scanner(System.in);
        int maxInputLength = 0;
        boolean exitLoop = false;

        while (!exitLoop && inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();
            int inputLength = userInput.length();
            Trio<Commands, Integer, TaskList> commandsIndexTaskTrio = Parser.parseUserInput(userInput, this.taskList);
            Commands userCommand = commandsIndexTaskTrio.getHead();
            Integer taskIndex = commandsIndexTaskTrio.getBody();
            TaskList taskTargetList = commandsIndexTaskTrio.getTail();
            Task taskTarget = taskIndex != null ? taskTargetList.getTask(taskIndex) : null;

            //Bot Replies Instead of Echo
            int stylingIndex = 7;
            switch (userCommand) {
                case BYE:
                    exitLoop = true;
                    this.ui.sayBye();
                    break;

                case LIST:
                    this.taskList.printTaskList(maxInputLength);
                    break;

                case MARK:
                    //set task to done
                    this.taskList.markTask(taskIndex);
                    try {
                        this.f.writeToFile(null, this.taskList, false);
                    } catch (IOException e) {
                        this.ui.printErrorMessage(Commands.MARK, inputLength);
                    }

                    //print text
                    this.ui.printTask("mark", inputLength, maxInputLength, taskTarget);

                    break;

                case UNMARK:
                    //set task to undone
                    this.taskList.unmarkTask(taskIndex);
                    try {
                        f.writeToFile(null, this.taskList, false);
                    } catch (IOException e) {
                        this.ui.printErrorMessage(Commands.UNMARK, inputLength);
                    }

                    //print text
                    this.ui.printTask("unmark", inputLength, maxInputLength, taskTarget);

                    break;

                case DELETE:
                    this.taskList.removeTask(taskIndex);
                    try {
                        f.writeToFile(null, this.taskList, false);
                    } catch (IOException e) {
                        this.ui.printErrorMessage(Commands.DELETE, inputLength);
                    }

                    //print text
                    this.ui.printTask("delete", inputLength, maxInputLength, taskTarget);

                    ConsoleDisplayStyle.printIndentation(inputLength);
                    System.out.printf("Now you have %d tasks in the list.%n", this.taskList.getSize());
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength);
                    break;

                case ADDTASK:
                    this.taskList.addTask(taskTarget);

                    //to write it into file
                    try {
                        f.writeToFile(taskTarget, this.taskList, true);
                    } catch (IOException e) {
                        this.ui.printErrorMessage(Commands.ADDTASK, inputLength);
                        return;
                    }

                    //prints added task
                    this.ui.printTask("addTask", inputLength, maxInputLength, taskTarget);

                    ConsoleDisplayStyle.printIndentation(inputLength);
                    System.out.printf("Now you have %d tasks in the list.%n", this.taskList.getSize());
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + stylingIndex);
                    break;

                case FIND:
                    taskTargetList.printTaskList(maxInputLength);
                    break;

                case INVALID:
                    //do nothing and wait for next line
                    break;

                case DEFAULT:
                    this.ui.printErrorMessage(Commands.DEFAULT, inputLength);

                    //show possible commands
                    ConsoleDisplayStyle.printIndentation(inputLength);
                    System.out.println("Possible Commands:");
                    for (Commands c : Commands.values()) {
                        if (c == Commands.ADDTASK || c == Commands.DEFAULT || c == Commands.INVALID) {
                            continue;
                        }
                        ConsoleDisplayStyle.printIndentation(inputLength);
                        System.out.println(c);
                    }
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, 0);
                    break;
            }

        }

    }

    public static void main(String[] args) {
        new TrackerBot().run();
    }
}