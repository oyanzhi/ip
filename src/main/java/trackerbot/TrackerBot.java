package trackerbot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import trackerbot.exceptions.TrackerBotException;
import trackerbot.tasks.Task;
import trackerbot.ui.ConsoleDisplayStyle;
import trackerbot.ui.UI;
import trackerbot.utils.FileIO;
import trackerbot.utils.Parser;
import trackerbot.utils.TaskList;
import trackerbot.utils.Trio;


/**
 * Main class of the Console Bot
 */
public class TrackerBot {
    private FileIO f;
    private TaskList taskList;
    private UI ui;
    private String responseToGui;

    /**
     * An enum class for all commands available for use for the bot
     */
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
        INVALID,
        SORT
    }

    /**
     * Returns an instance of the console TrackerBot with the correct settings
     */
    public TrackerBot() {
        try {
            this.f = new FileIO();
            this.taskList = this.f.readFileContents();
            this.ui = new UI();
            this.responseToGui = null;
        } catch (TrackerBotException | FileNotFoundException e) {
            ConsoleDisplayStyle.printBasicStyling(0, 0, e.getMessage());
        }
    }

    /**
     * Starts running the TrackerBot in the console and gui
     */
    public void run() {
        this.ui.greet();

        //Bot in Process
        Scanner inputScanner = new Scanner(System.in);
        boolean toExitLoop = false;

        while (!toExitLoop && inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();
            toExitLoop = botCompute(true, userInput);
        }
    }

    /**
     * Handles the bot response to a user input
     * @param isConsole boolean on whether is console or GUI
     * @param userInput the user input as a string
     * @return true if console && exitloop or gui to react / false otherwise
     */
    public boolean botCompute(boolean isConsole, String userInput) {
        int inputLength = userInput.length();

        Trio<Commands, Integer, TaskList> commandsIndexTaskTrio;
        try {
            commandsIndexTaskTrio = Parser.parseUserInput(userInput, this.taskList);
        } catch (TrackerBotException e) {
            if (isConsole) {
                ConsoleDisplayStyle.printBasicStyling(0, 0, e.getMessage());
                return false;
            } else {
                this.responseToGui = e.getMessage();
                return true;
            }
        }
        Commands userCommand = commandsIndexTaskTrio.getHead();

        assert userCommand != null : "user command should not be null";

        Integer taskIndex = commandsIndexTaskTrio.getBody();
        TaskList taskTargetList = commandsIndexTaskTrio.getTail();
        Task taskTarget = taskIndex != null ? taskTargetList.getTask(0) : null;

        //Bot Replies
        switch (userCommand) {
        case BYE:
            this.executeBye(isConsole);

            //console also returns true to exit loop
            return true;

        case LIST:
            executeList(isConsole);

            //false for console to not exit loop
            //true for gui to return string
            return !isConsole;

        case MARK:
            try {
                this.executeMark(inputLength, taskIndex, taskTarget, isConsole);
            } catch (IOException e) {
                if (isConsole) {
                    String message = this.ui.printErrorMessage(Commands.MARK, inputLength, true);
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
                } else {
                    this.responseToGui = this.ui.printErrorMessage(Commands.MARK, inputLength, false);
                }
                //always true for errors
                return true;
            }
            return !isConsole;

        case UNMARK:
            try {
                this.executeUnmark(inputLength, taskIndex, taskTarget, isConsole);
            } catch (IOException e) {
                if (isConsole) {
                    String message = this.ui.printErrorMessage(Commands.UNMARK, inputLength, true);
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
                } else {
                    this.responseToGui = this.ui.printErrorMessage(Commands.UNMARK, inputLength, false);
                }
                //always true for errors
                return true;
            }
            return !isConsole;

        case DELETE:
            try {
                this.executeDelete(inputLength, taskIndex, taskTarget, isConsole);
            } catch (IOException e) {
                if (isConsole) {
                    String message = this.ui.printErrorMessage(Commands.DELETE, inputLength, true);
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
                } else {
                    this.responseToGui = this.ui.printErrorMessage(Commands.DELETE, inputLength, false);
                }
                //always true for errors
                return true;
            }
            return !isConsole;

        case ADDTASK:
            try {
                this.executeAddTask(inputLength, taskTarget, isConsole);
            } catch (IOException e) {
                if (isConsole) {
                    String message = this.ui.printErrorMessage(Commands.ADDTASK, inputLength, true);
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
                } else {
                    this.responseToGui = this.ui.printErrorMessage(Commands.ADDTASK, inputLength, false);
                }
                //always true for errors
                return true;
            }
            return !isConsole;

        case FIND:
            this.executeFind(isConsole, taskTargetList);
            return !isConsole;

        case INVALID:
            //do nothing and wait for next line
            return isConsole;

        case SORT:
            try {
                this.executeSort();
            } catch (IOException e) {
                if (isConsole) {
                    String message = this.ui.printErrorMessage(Commands.ADDTASK, inputLength, true);
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
                } else {
                    this.responseToGui = this.ui.printErrorMessage(Commands.ADDTASK, inputLength, false);
                }
                //always true for errors
                return true;
            }
            this.executeList(isConsole);
            return !isConsole;

        case DEFAULT:
            this.executeDefault(inputLength, isConsole);
            return !isConsole;

        default:
            //added for style purposes
            break;
        }

        return false; //does not exit loop
    }

    private void executeBye(boolean isConsole) {
        if (isConsole) {
            this.ui.sayBye();
            this.responseToGui = "";
        } else {
            this.responseToGui = "Bye. Hope to see you again soon!";
        }
    }

    private void executeList(boolean isConsole) {
        if (isConsole) {
            this.taskList.printTaskList(0);
            this.responseToGui = "";
        } else {
            this.responseToGui = this.taskList.returnTaskList();
        }
    }

    private void executeMark(int inputLength, int taskIndex, Task taskTarget, boolean isConsole) throws IOException {
        this.taskList.markTask(taskIndex);
        f.writeToFile(null, this.taskList, false);

        if (isConsole) {
            this.ui.printTask("mark", inputLength, 0, taskTarget, true);
            this.responseToGui = "";
        } else {
            this.responseToGui = this.ui.printTask("mark",
                    inputLength,
                    0,
                    taskTarget,
                    false);
        }
    }

    private void executeUnmark(int inputLength, int taskIndex, Task taskTarget, boolean isConsole) throws IOException {
        this.taskList.unmarkTask(taskIndex);
        f.writeToFile(null, this.taskList, false);

        if (isConsole) {
            this.ui.printTask("unmark", inputLength, 0, taskTarget, true);
            this.responseToGui = "";
        } else {
            this.responseToGui = this.ui.printTask("unmark",
                    inputLength,
                    0,
                    taskTarget,
                    false);
        }
    }

    private void executeDelete(int inputLength, int taskIndex, Task taskTarget, boolean isConsole) throws IOException {
        this.taskList.removeTask(taskIndex);
        f.writeToFile(null, this.taskList, false);

        if (isConsole) {
            this.ui.printTask("delete", inputLength, 0, taskTarget, true);
            ConsoleDisplayStyle.printIndentation(inputLength);
            System.out.printf("Now you have %d tasks in the list.%n", this.taskList.getSize());
            ConsoleDisplayStyle.printHorizontalLine(inputLength, 0);
        } else {
            this.responseToGui = this.ui.printTask("delete",
                    inputLength,
                    0,
                    taskTarget,
                    false);
        }
    }

    private void executeAddTask(int inputLength, Task taskTarget, boolean isConsole) throws IOException {
        this.taskList.addTask(taskTarget);
        f.writeToFile(taskTarget, this.taskList, true);

        if (isConsole) {
            this.ui.printTask("addTask", inputLength, 0, taskTarget, true);
            ConsoleDisplayStyle.printIndentation(inputLength);
            System.out.printf("Now you have %d tasks in the list.%n", this.taskList.getSize());
            ConsoleDisplayStyle.printHorizontalLine(inputLength, 0);
        } else {
            this.responseToGui = this.ui.printTask("addTask",
                    inputLength,
                    0,
                    taskTarget,
                    false);
        }
    }

    private void executeFind(boolean isConsole, TaskList taskTargetList) {
        if (isConsole) {
            taskTargetList.printTaskList(0);
        } else {
            this.responseToGui = taskTargetList.returnTaskList();
        }
    }

    private void executeSort() throws IOException {
        this.taskList.sortTaskList();
        f.writeToFile(null, this.taskList, false);
    }

    private void executeDefault(int inputLength, boolean isConsole) {
        String message = this.ui.printErrorMessage(Commands.DEFAULT, inputLength, true) + "\n";

        //show possible commands
        message += "Possible Commands: \n";
        for (Commands c : Commands.values()) {
            if (c == Commands.ADDTASK || c == Commands.DEFAULT || c == Commands.INVALID) {
                continue;
            }
            message += c.toString() + "\n";
        }

        if (isConsole) {
            ConsoleDisplayStyle.printBasicStyling(0, message.length(), message);
        } else {
            this.responseToGui = message;
        }
    }

    public String getResponse(String input) {
        return botCompute(false, input) ? this.responseToGui : "Cannot Get Results";
    }

    public static void main(String[] args) {
        new TrackerBot().run();
    }
}
