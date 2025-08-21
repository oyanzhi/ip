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


            //to parse input of mark and unmark
            if (userInput.startsWith("mark ")) {
                taskIndex = Integer.parseInt(userInput.substring("mark ".length())) - 1;
                userInput = "mark";
            }

            if (userInput.startsWith("unmark ")) {
                taskIndex = Integer.parseInt(userInput.substring("unmark ".length())) - 1;
                userInput = "unmark";
            }

            //to parse input of task additions
            if (userInput.startsWith("todo ")) {
                String taskDescription = userInput.substring("todo ".length());

                taskTarget = new ToDos(taskDescription);
                userInput = "addTask";
            }

            if (userInput.startsWith("deadline ")) {
                int deadlineIndex = userInput.indexOf("/by ");

                //-1 to account for space between description and /by
                String taskDescription = userInput.substring("deadline ".length(), deadlineIndex - 1);
                String deadline = userInput.substring(deadlineIndex + "/by ".length());

                taskTarget = new Deadlines(taskDescription, deadline);
                userInput = "addTask";
            }

            if (userInput.startsWith("event ")) {
                int startDateIndex = userInput.indexOf("/from ");
                int endDateIndex = userInput.indexOf("/to ");

                //-1 to account for space between description and /from
                String taskDescription = userInput.substring("event ".length(), startDateIndex - 1);

                // -1 for proper spacing between /from [start] /to [end]
                String startDate = userInput.substring(startDateIndex + "/from ".length(), endDateIndex - 1);
                String endDate = userInput.substring(endDateIndex + "/to ".length());

                taskTarget = new Events(taskDescription, startDate, endDate);
                userInput = "addTask";
            }


            //Bot Replies Instead of Echo
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
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + 7); //7 to style
                    ConsoleDisplayStyle.printIndentation(inputLength);
                    System.out.printf("Here are the [%d] tasks in your list:\n", tasks.size());
                    for (int i = 0; i < tasks.size(); i++) { //Non-Empty Texts Stored
                        ConsoleDisplayStyle.printIndentation(inputLength);
                        String oneRow = String.format("%d. %s", i + 1, tasks.get(i));
                        System.out.println(oneRow);
                    }
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + 7); //7 to style
                }
                break;

            case "mark":
                //set task to done
                taskTarget = tasks.get(taskIndex);
                taskTarget.markAsDone();

                //print text
                ConsoleDisplayStyle.printCommandStyling(userInput,
                        inputLength,
                        maxInputLength + 7,
                        taskTarget);

                break;

            case "unmark":
                //set task to undone
                taskTarget = tasks.get(taskIndex);
                taskTarget.markAsUndone();

                ConsoleDisplayStyle.printCommandStyling(userInput,
                        inputLength,
                        maxInputLength + 7,
                        taskTarget);

                break;

            case "addTask":
                tasks.add(taskTarget);

                ConsoleDisplayStyle.printCommandStyling(userInput,
                        inputLength,
                        maxInputLength + 7,
                        taskTarget);

                ConsoleDisplayStyle.printIndentation(inputLength);
                System.out.printf("Now you have %d tasks in the list.%n", tasks.size());
                ConsoleDisplayStyle.printHorizontalLine(inputLength, 0);
                break;

            default:
                String defaultAddToStorageText = "Missing Command!";

                //7 for styling
                ConsoleDisplayStyle.printBasicStyling(inputLength, inputLength + 7, defaultAddToStorageText);
                break;
            }

        }

    }
}