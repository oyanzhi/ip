import java.util.Scanner;

public class TrackerBot {

    public static void main(String[] args) {
        String name = "TrackerBot";

        //Initial Greeting
        ConsoleDisplayLine.printHorizontalLine(0);
        System.out.println("Hello! I'm "
                + name
                + "\n"
                + "What can I do for you?");
        ConsoleDisplayLine.printHorizontalLine(0);

        //Echo
        String toExit = "bye";

        Scanner inputScanner = new Scanner(System.in);

        while (inputScanner.hasNextLine()) {
            String userInput = inputScanner.nextLine();
            int inputLength = userInput.length();

            ConsoleDisplayLine.printHorizontalLine(inputLength);

            //Bot Echos
            for (int i = 0; i < inputLength; i++) {
                System.out.print(" ");
            }

            //Exit Check
            if (userInput.equals(toExit)) { //Exiting
                System.out.println("Bye. Hope to see you again soon!");
                ConsoleDisplayLine.printHorizontalLine(inputLength);
                break;
            } else { //Not Exiting
                System.out.println(userInput);
                ConsoleDisplayLine.printHorizontalLine(inputLength);
            }

        }

    }
}