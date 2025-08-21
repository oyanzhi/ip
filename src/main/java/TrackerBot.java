import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;

public class TrackerBot {
    static ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        String name = "TrackerBot";

        //Initial Greeting
        ConsoleDisplayStyle.printHorizontalLine(0, 0);
        System.out.println("Hello! I'm "
                + name
                + "\n"
                + "What can I do for you?");
        ConsoleDisplayStyle.printHorizontalLine(0, 0);

        //Echo
        Scanner inputScanner = new Scanner(System.in);
        int maxInputLength = 0;
        boolean exitLoop = false;

        while (!exitLoop && inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();
            int taskIndex = -1;
            int inputLength = userInput.length();
            maxInputLength = Math.max(inputLength, maxInputLength);
            Task taskTarget = null;


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
                    userInput = "mark";
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
                    userInput = "unmark";
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
                    userInput = "delete";
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
                    userInput = "addTask";
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
                    userInput = "addTask";
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
                    userInput = "addTask";
                } catch (TrackerBotException e) {
                    ConsoleDisplayStyle.printBasicStyling(inputLength, e.getMessage().length(), e.getMessage());
                    continue;
                }
            }


            //Bot Replies Instead of Echo
            int stylingIndex = 7;
            switch (userInput) {
            case "bye":
                exitLoop = true;
                String defaultExitText = "Bye. Hope to see you again soon!";
                ConsoleDisplayStyle.printBasicStyling(inputLength, 0, defaultExitText);
                break;

            case "list":
                if (tasks.isEmpty()) { //No Texts Stored
                    String defaultEmptyListText = "Empty List!";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, 0, defaultEmptyListText);
                } else { //Texts Stored
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

            case "mark":
                //set task to done
                taskTarget = tasks.get(taskIndex);
                taskTarget.markAsDone();

                //print text
                ConsoleDisplayStyle.printCommandStyling(userInput,
                        inputLength,
                        maxInputLength + stylingIndex,
                        taskTarget);

                break;

            case "unmark":
                //set task to undone
                taskTarget = tasks.get(taskIndex);
                taskTarget.markAsUndone();

                ConsoleDisplayStyle.printCommandStyling(userInput,
                        inputLength,
                        maxInputLength + stylingIndex,
                        taskTarget);

                break;

            case "delete":
                taskTarget = tasks.get(taskIndex);
                tasks.remove(taskTarget);

                ConsoleDisplayStyle.printCommandStyling(userInput,
                        inputLength,
                        maxInputLength + stylingIndex,
                        taskTarget);

                ConsoleDisplayStyle.printIndentation(inputLength);
                System.out.printf("Now you have %d tasks in the list.%n", tasks.size());
                ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength);
                break;

            case "addTask":
                tasks.add(taskTarget);

                ConsoleDisplayStyle.printCommandStyling(userInput,
                        inputLength,
                        maxInputLength + stylingIndex,
                        taskTarget);

                ConsoleDisplayStyle.printIndentation(inputLength);
                System.out.printf("Now you have %d tasks in the list.%n", tasks.size());
                ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + stylingIndex);
                break;

            default:
                String missingText = "Missing Command!";

                //stylingIndex for styling
                ConsoleDisplayStyle.printBasicStyling(inputLength, inputLength + stylingIndex, missingText);
                break;
            }

        }

    }
}