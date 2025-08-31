import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Math;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class TrackerBot {
    static ArrayList<Task> tasks = new ArrayList<>();

    enum Commands {
        MARK,
        UNMARK,
        DELETE,
        ADDTASK,
        BYE,
        LIST,
        DEFAULT
    }

    public static void main(String[] args) {
        String name = "TrackerBot";

        //Set-Up Stored File Path
        String currDir = System.getProperty("user.dir");

        //to create parent directories
        Path dataFolderDir = Paths.get(currDir, "data");
        try {
            Files.createDirectories(dataFolderDir);
        } catch (IOException e) {
            System.out.println("Could Not Create Data Folder. Please Try Again.");
            return;
        }

        //actual storage file
        Path p = Paths.get(dataFolderDir.toString(), "task_data");
        FileIO f = null;
        try {
            boolean fileExists = Files.exists(p);
            if (!fileExists) {
                throw new FileNotFoundException();
            }
            f = new FileIO(p.toFile());
            tasks = f.readFileContents();
        } catch (FileNotFoundException e) {
            //lets user know missing file - to create one
            System.out.println(e.getMessage() + "\n" + "Missing Storage File. Creating a Storage File Now...");
            try {
                Files.createFile(p);
            } catch (IOException ex) {
                System.out.println("Cannot Create File. Please try again.");
                return;
            } finally {
                f = new FileIO(p.toFile());
            }
        } catch (TrackerBotException e) {
            System.out.println(e.getMessage());
            return;
        }


        //Initial Greeting
        ConsoleDisplayStyle.printHorizontalLine(0, 0);
        System.out.println("Hello! I'm "
                + name
                + "\n"
                + "What can I do for you?");
        ConsoleDisplayStyle.printHorizontalLine(0, 0);


        //Bot Response
        Scanner inputScanner = new Scanner(System.in);
        int maxInputLength = 0;
        boolean exitLoop = false;

        while (!exitLoop && inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();
            int taskIndex = -1;
            int inputLength = userInput.length();
            maxInputLength = Math.max(inputLength, maxInputLength);
            Task taskTarget = null;

            Commands userCommand = Commands.DEFAULT;

            //to parse utility inputs
            if (userInput.startsWith("list")) {
                userCommand = Commands.LIST;
            }

            if (userInput.startsWith("bye")) {
                userCommand = Commands.BYE;
            }

            //to parse input of mark
            if (userInput.startsWith("mark")) {
                try {
                    if ("mark".length() + 1 > userInput.length()) {
                        throw new TrackerBotException("Missing Arguments! Example usage 'mark 1'");
                    }
                    taskIndex = Integer.parseInt(userInput.substring("mark".length() + 1)) - 1;
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        throw new TrackerBotException("Invalid Task Index");
                    }
                    userCommand = Commands.MARK;
                } catch (NumberFormatException e) {
                    String message = "Missing Task Index. Example usage 'mark 1'";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(),  message);
                    continue;
                } catch (TrackerBotException e) {
                    ConsoleDisplayStyle.printBasicStyling(inputLength, e.getMessage().length(), e.getMessage());
                    continue;
                }
            }

            //to parse input of unmark
            if (userInput.startsWith("unmark")) {
                try {
                    if ("unmark".length() + 1 > userInput.length()) {
                        throw new TrackerBotException("Missing Arguments! Example usage 'unmark 1'");
                    }
                    taskIndex = Integer.parseInt(userInput.substring("unmark".length() + 1)) - 1;
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        throw new TrackerBotException("Invalid Task Index");
                    }
                    userCommand = Commands.UNMARK;
                } catch (NumberFormatException e) {
                    String message = "Missing Task Index. Example usage 'unmark 1'";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(),  message);
                    continue;
                } catch (TrackerBotException e) {
                    ConsoleDisplayStyle.printBasicStyling(inputLength, e.getMessage().length(), e.getMessage());
                    continue;
                }
            }

            //to parse input for deleting
            if (userInput.startsWith("delete")) {
                try {
                    if ("delete".length() + 1 > userInput.length()) {
                        throw new TrackerBotException("Missing Arguments! Example usage 'delete 1'");
                    }
                    taskIndex = Integer.parseInt(userInput.substring("delete".length() + 1)) - 1;
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        throw new TrackerBotException("Invalid Task Index");
                    }
                    userCommand = Commands.DELETE;
                } catch (NumberFormatException e) {
                    String message = "Missing Task Index. Example usage 'delete 1'";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(),  message);
                    continue;
                } catch (TrackerBotException e) {
                    ConsoleDisplayStyle.printBasicStyling(inputLength, e.getMessage().length(), e.getMessage());
                    continue;
                }
            }

            //to parse input of task additions
            if (userInput.startsWith("todo")) {
                try {
                    if ("todo".length() + 1 > userInput.length()) {
                        throw new TrackerBotException("Missing Arguments! Example usage 'todo tasking'");
                    }
                    String taskDescription = userInput.substring("todo ".length());
                    if (taskDescription.equals("")) {
                        throw new TrackerBotException("Missing Description. Example usage 'todo tasking'");
                    }
                    taskTarget = new ToDos(taskDescription);
                    userCommand = Commands.ADDTASK;
                } catch (TrackerBotException e) {
                    ConsoleDisplayStyle.printBasicStyling(inputLength, e.getMessage().length(), e.getMessage());
                    continue;
                }


            }

            if (userInput.startsWith("deadline")) {
                try {
                    int deadlineIndex = userInput.indexOf("/by ");
                    if ("deadline".length() + 1 > userInput.length() || deadlineIndex <= 9
                            || userInput.charAt(deadlineIndex - 1) != ' ') {
                        throw new TrackerBotException("Missing Arguments! Example usage 'deadline tasking /by date'");
                    }

                    //-1 to account for space between description and /by
                    String taskDescription = userInput.substring("deadline".length() + 1, deadlineIndex - 1);
                    String deadline = userInput.substring(deadlineIndex + "/by ".length());

                    taskTarget = new Deadlines(taskDescription, deadline);
                    userCommand = Commands.ADDTASK;
                } catch (TrackerBotException e) {
                    ConsoleDisplayStyle.printBasicStyling(inputLength, e.getMessage().length(), e.getMessage());
                    continue;
                }

            }

            if (userInput.startsWith("event")) {
                try {
                    int startDateIndex = userInput.indexOf("/from ");
                    int endDateIndex = userInput.indexOf("/to ");

                    if (("event".length() + 1) > userInput.length() || startDateIndex <= 7 || endDateIndex <= 15
                            || userInput.charAt(startDateIndex - 1) != ' '
                            || userInput.charAt(endDateIndex - 1) != ' ') {
                        throw new TrackerBotException("Missing Arguments! " +
                                "Example usage 'event tasking /from startDate /to endDate");
                    }

                    //-1 to account for space between description and /from
                    String taskDescription = userInput.substring("event".length() + 1, startDateIndex - 1);

                    // -1 for proper spacing between /from [start] /to [end]
                    String startDate = userInput.substring(startDateIndex + "/from ".length(), endDateIndex - 1);
                    String endDate = userInput.substring(endDateIndex + "/to ".length());

                    taskTarget = new Events(taskDescription, startDate, endDate);
                    userCommand = Commands.ADDTASK;
                } catch (TrackerBotException e) {
                    ConsoleDisplayStyle.printBasicStyling(inputLength, e.getMessage().length(), e.getMessage());
                    continue;
                }
            }


            //Bot Replies Instead of Echo
            int stylingIndex = 7;
            switch (userCommand) {
            case BYE:
                exitLoop = true;
                String defaultExitText = "Bye. Hope to see you again soon!";
                ConsoleDisplayStyle.printBasicStyling(inputLength, 0, defaultExitText);
                break;

            case LIST:
                if (tasks.isEmpty()) { //No Texts Stored
                    String defaultEmptyListText = "Empty List!";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, 0, defaultEmptyListText);
                } else { //Texts Stored
                    //Print from Instance Memory
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + stylingIndex);
                    ConsoleDisplayStyle.printIndentation(inputLength);
                    System.out.printf("Here are the [%d] tasks in your list:\n", tasks.size());
                    for (int i = 0; i < tasks.size(); i++) { //Non-Empty Texts Stored
                        ConsoleDisplayStyle.printIndentation(inputLength);
                        String oneRow = String.format("%d. %s", i + 1, tasks.get(i));
                        System.out.println(oneRow);
                    }
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + stylingIndex);
                }
                break;

            case MARK:
                //set task to done
                taskTarget = tasks.get(taskIndex);
                taskTarget.markAsDone();
                try {
                    f.writeToFile(null, tasks, false);
                } catch (IOException e) {
                    String message = "Failed to Mark in File. Please try again.";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
                }

                //print text
                ConsoleDisplayStyle.printCommandStyling("mark",
                        inputLength,
                        maxInputLength + stylingIndex,
                        taskTarget);

                break;

            case UNMARK:
                //set task to undone
                taskTarget = tasks.get(taskIndex);
                taskTarget.markAsUndone();
                try {
                    f.writeToFile(null, tasks, false);
                } catch (IOException e) {
                    String message = "Failed to Mark in File. Please try again.";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
                }

                ConsoleDisplayStyle.printCommandStyling("unmark",
                        inputLength,
                        maxInputLength + stylingIndex,
                        taskTarget);

                break;

            case DELETE:
                taskTarget = tasks.get(taskIndex);
                tasks.remove(taskTarget);
                try {
                    f.writeToFile(null, tasks, false);
                } catch (IOException e) {
                    String message = "Failed to Mark in File. Please try again.";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(), message);
                }

                ConsoleDisplayStyle.printCommandStyling("delete",
                        inputLength,
                        maxInputLength + stylingIndex,
                        taskTarget);

                ConsoleDisplayStyle.printIndentation(inputLength);
                System.out.printf("Now you have %d tasks in the list.%n", tasks.size());
                ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength);
                break;

            case ADDTASK:
                tasks.add(taskTarget);

                //to write it into file
                try {
                    f.writeToFile(taskTarget, tasks, true);
                } catch (IOException e) {
                    String message = "Failed to save task. Please Try Again";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, message.length(),  message);
                    return;
                }

                ConsoleDisplayStyle.printCommandStyling("addTask",
                        inputLength,
                        maxInputLength + stylingIndex,
                        taskTarget);

                ConsoleDisplayStyle.printIndentation(inputLength);
                System.out.printf("Now you have %d tasks in the list.%n", tasks.size());
                ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + stylingIndex);
                break;

            case DEFAULT:
                String missingText = "Missing Command!";

                //stylingIndex for styling
                ConsoleDisplayStyle.printBasicStyling(inputLength, inputLength + stylingIndex, missingText);

                //show possible commands
                ConsoleDisplayStyle.printIndentation(inputLength);
                System.out.println("Possible Commands:");
                for (Commands c : Commands.values()) {
                    ConsoleDisplayStyle.printIndentation(inputLength);
                    System.out.println(c);
                }
                ConsoleDisplayStyle.printHorizontalLine(inputLength, 0);
                break;
            }

        }

    }
}