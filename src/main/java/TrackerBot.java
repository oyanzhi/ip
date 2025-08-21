import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;

public class TrackerBot {
    static ArrayList<String> texts = new ArrayList<>();

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
        String toExit = "bye";
        String toDisplay = "list";
        String addStyling = "added: ";
        int addStylingLength = addStyling.length();

        Scanner inputScanner = new Scanner(System.in);

        int maxInputLength = 0;

        while (inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();
            int inputLength = userInput.length();

            maxInputLength = Math.max(inputLength, maxInputLength);


            //Bot Replies Instead of Echo
            
            //Exit Check
            if (userInput.equals(toExit)) { //Exiting
                String defaultExitText = "Bye. Hope to see you again soon!";
                ConsoleDisplayStyle.printBasicStyling(inputLength, 0, defaultExitText);
                break;
            } else if (userInput.equals(toDisplay)) { //Display Stored Texts
                if (texts.isEmpty()) { //No Texts Stored
                    String defaultEmptyListText = "Empty List!";
                    ConsoleDisplayStyle.printBasicStyling(inputLength, 0, defaultEmptyListText);
                } else { //Texts Stored
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + 3); //3 for styling
                    for (int i = 0; i < texts.size(); i++) { //Non-Empty Texts Stored
                        ConsoleDisplayStyle.printIndentation(inputLength);
                        String oneRow = String.format("%d. %s", i + 1, texts.get(i));
                        System.out.println(oneRow);
                    }
                    ConsoleDisplayStyle.printHorizontalLine(inputLength, maxInputLength + 3); //3 for styling
                }
            } else { //Not Exiting + Storing Text
                texts.add(userInput);
                String defaultAddedToStorageText = "added: " + userInput;

                //7 for styling
                ConsoleDisplayStyle.printBasicStyling(inputLength, inputLength + 7, defaultAddedToStorageText);
            }
        }

    }
}