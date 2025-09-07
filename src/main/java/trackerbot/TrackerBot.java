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
        boolean exitLoop = false;

        while (!exitLoop && inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();
            exitLoop = botCompute(true, userInput);
        }
    }

    public boolean botCompute(boolean isConsole, String userInput) {
            int inputLength = userInput.length();

        Trio<Commands, Integer, TaskList> commandsIndexTaskTrio = null;
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
            Integer taskIndex = commandsIndexTaskTrio.getBody();
            TaskList taskTargetList = commandsIndexTaskTrio.getTail();
            Task taskTarget = taskIndex != null ? taskTargetList.getTask(0) : null;

            //Bot Replies
            switch (userCommand) {
            case BYE:
                if (isConsole) {
                    this.ui.sayBye();
                } else {
                    this.responseToGui = "Bye. Hope to see you again soon!";
                }

                //console also returns true to exit loop
                return true;

            case LIST:
                //false for console to not exit loop
                //true for gui to return string
                if (isConsole) {
                    this.taskList.printTaskList(0);
                    return false;
                } else {
                    this.responseToGui = this.taskList.returnTaskList();
                    return true;
                }

            case MARK:
                //set task to done
                this.taskList.markTask(taskIndex);
                try {
                    this.f.writeToFile(null, this.taskList, false);
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

                //print text
                if (isConsole) {
                    this.ui.printTask("mark", inputLength, 0, taskTarget, true);
                    return false;
                } else {
                    this.responseToGui = this.ui.printTask("mark",
                            inputLength,
                            0,
                            taskTarget,
                            false);
                    return true;
                }

            case UNMARK:
                //set task to undone
                this.taskList.unmarkTask(taskIndex);
                try {
                    f.writeToFile(null, this.taskList, false);
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

                //print text
                if (isConsole) {
                    this.ui.printTask("unmark", inputLength, 0, taskTarget, true);
                    return false;
                } else {
                    this.responseToGui = this.ui.printTask("unmark",
                            inputLength,
                            0,
                            taskTarget,
                            false);
                    return true;
                }

            case DELETE:
                this.taskList.removeTask(taskIndex);
                try {
                    f.writeToFile(null, this.taskList, false);
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

                //print text
                if (isConsole) {
                    this.ui.printTask("delete", inputLength, 0, taskTarget, true);
                    ConsoleDisplayStyle.printIndentation(inputLength);
                    System.out.printf("Now you have %d tasks in the list.%n", this.taskList.getSize());
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, 0);
                    return false;
                } else {
                    this.responseToGui = this.ui.printTask("delete",
                            inputLength,
                            0,
                            taskTarget,
                            false);
                    return true;
                }

            case ADDTASK:
                this.taskList.addTask(taskTarget);

                //to write it into file
                try {
                    f.writeToFile(taskTarget, this.taskList, true);
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

                //print text
                if (isConsole) {
                    this.ui.printTask("addTask", inputLength, 0, taskTarget, true);
                    ConsoleDisplayStyle.printIndentation(inputLength);
                    System.out.printf("Now you have %d tasks in the list.%n", this.taskList.getSize());
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, 0);
                    return false;
                } else {
                    this.responseToGui = this.ui.printTask("addTask",
                            inputLength,
                            0,
                            taskTarget,
                            false);
                    return true;
                }

            case FIND:
                taskTargetList.printTaskList(0);
                break;

            case INVALID:
                //do nothing and wait for next line
                break;

            case DEFAULT:
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
                    return false;

                } else {
                    this.responseToGui = message;
                    return true;
                }

            default:
                //added for style purposes
                break;
            }

            return false; //does not exit loop
    }

    public String getResponse(String input) {
        return botCompute(false, input) ? this.responseToGui : "Cannot Get Results";
    }

    public void clearResponse() {
        this.responseToGui = null;
    }

    public static void main(String[] args) {
        new TrackerBot().run();
    }
}
