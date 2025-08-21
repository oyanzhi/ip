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


            //to parse input of mark and unmark
            if (userInput.startsWith("mark ")) {
                taskIndex = Integer.parseInt(userInput.substring("mark ".length())) - 1;
                userInput = "mark";
            }

            if (userInput.startsWith("unmark ")) {
                taskIndex = Integer.parseInt(userInput.substring("unmark ".length())) - 1;
                userInput = "unmark";
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
                Task taskToMarkDone = tasks.get(taskIndex);
                taskToMarkDone.markAsDone();

                //print text
                ConsoleDisplayStyle.printMarkingStyling(true,
                        inputLength,
                        maxInputLength + 7,
                        taskToMarkDone);

                break;

            case "unmark":
                //set task to undone
                Task taskToMarkUndone = tasks.get(taskIndex);
                taskToMarkUndone.markAsUndone();

                ConsoleDisplayStyle.printMarkingStyling(false,
                        inputLength,
                        maxInputLength + 7,
                        taskToMarkUndone);

                break;

            default:
                tasks.add(new Task(userInput));
                String defaultAddToStorageText = "added: " + userInput;

                //7 for styling
                ConsoleDisplayStyle.printBasicStyling(inputLength, inputLength + 7, defaultAddToStorageText);
                break;
            }

        }

    }
}