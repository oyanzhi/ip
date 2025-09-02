package trackerbot;

/**
 * A static class that contains all the utility methods for parsing user input
 */
public abstract class Parser {

    /**
     * A method used to parse user inputs
     * @param userInput String representing the user input
     * @param taskList Current instance memory of the task list in the running process
     * @return A Trio where the head is the command, body is the task index, and tail is the task
     */
    public static Trio<TrackerBot.Commands, Integer, Task> parseUserInput(
            String userInput, TaskList taskList) {

        int taskIndex = -1;

        //to parse utility inputs
        if (userInput.startsWith("list")) {
            return new Trio<>(TrackerBot.Commands.LIST, null, null);
        }

        if (userInput.startsWith("bye")) {
            return new Trio<>(TrackerBot.Commands.BYE, null, null);
        }

        //to parse input of mark
        if (userInput.startsWith("mark")) {
            try {
                if ("mark".length() + 1 > userInput.length()) {
                    throw new TrackerBotException("Missing Arguments! Example usage 'mark 1'");
                }
                taskIndex = Integer.parseInt(userInput.substring("mark".length() + 1)) - 1;
                if (taskIndex < 0 || taskIndex >= taskList.getSize()) {
                    throw new TrackerBotException("Invalid trackerbot.Task Index");
                }
                Task taskTarget = taskList.getTask(taskIndex);
                return new Trio<>(TrackerBot.Commands.MARK, taskIndex, taskTarget);
            } catch (NumberFormatException e) {
                String message = "Missing trackerbot.Task Index. Example usage 'mark 1'";
                ConsoleDisplayStyle.printBasicStyling(4, message.length(),  message);
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);
            } catch (TrackerBotException e) {
                ConsoleDisplayStyle.printBasicStyling(4, e.getMessage().length(), e.getMessage());
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);
            }
        }

        //to parse input of unmark
        if (userInput.startsWith("unmark")) {
            try {
                if ("unmark".length() + 1 > userInput.length()) {
                    throw new TrackerBotException("Missing Arguments! Example usage 'unmark 1'");
                }
                taskIndex = Integer.parseInt(userInput.substring("unmark".length() + 1)) - 1;
                if (taskIndex < 0 || taskIndex >= taskList.getSize()) {
                    throw new TrackerBotException("Invalid trackerbot.Task Index");
                }
                Task taskTarget = taskList.getTask(taskIndex);
                return new Trio<>(TrackerBot.Commands.UNMARK, taskIndex, taskTarget);
            } catch (NumberFormatException e) {
                String message = "Missing trackerbot.Task Index. Example usage 'unmark 1'";
                ConsoleDisplayStyle.printBasicStyling(6, message.length(),  message);
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);
            } catch (TrackerBotException e) {
                ConsoleDisplayStyle.printBasicStyling(6, e.getMessage().length(), e.getMessage());
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);
            }
        }

        //to parse input for deleting
        if (userInput.startsWith("delete")) {
            try {
                if ("delete".length() + 1 > userInput.length()) {
                    throw new TrackerBotException("Missing Arguments! Example usage 'delete 1'");
                }
                taskIndex = Integer.parseInt(userInput.substring("delete".length() + 1)) - 1;
                if (taskIndex < 0 || taskIndex >= taskList.getSize()) {
                    throw new TrackerBotException("Invalid trackerbot.Task Index");
                }
                Task taskTarget = taskList.getTask(taskIndex);
                return new Trio<>(TrackerBot.Commands.DELETE, taskIndex, taskTarget);
            } catch (NumberFormatException e) {
                String message = "Missing trackerbot.Task Index. Example usage 'delete 1'";
                ConsoleDisplayStyle.printBasicStyling(6, message.length(),  message);
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);
            } catch (TrackerBotException e) {
                ConsoleDisplayStyle.printBasicStyling(6, e.getMessage().length(), e.getMessage());
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);

            }
        }

        //to parse input of task additions
        if (userInput.startsWith("todo")) {
            try {
                if ("todo".length() + 1 > userInput.length()) {
                    throw new TrackerBotException("Missing Arguments! Example usage 'todo tasking'");
                }
                String taskDescription = userInput.substring("todo ".length());
                if (taskDescription.isEmpty()) {
                    throw new TrackerBotException("Missing Description. Example usage 'todo tasking'");
                }
                Task taskTarget = new ToDos(taskDescription);
                TrackerBot.Commands userCommand = TrackerBot.Commands.ADDTASK;
                return new Trio<>(userCommand, null, taskTarget);
            } catch (TrackerBotException e) {
                ConsoleDisplayStyle.printBasicStyling(4, e.getMessage().length(), e.getMessage());
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);

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

                Task taskTarget = new Deadlines(taskDescription, deadline);
                TrackerBot.Commands userCommand = TrackerBot.Commands.ADDTASK;
                return new Trio<>(userCommand, null, taskTarget);
            } catch (TrackerBotException e) {
                ConsoleDisplayStyle.printBasicStyling(8, e.getMessage().length(), e.getMessage());
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);
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

                Task taskTarget = new Events(taskDescription, startDate, endDate);
                TrackerBot.Commands userCommand = TrackerBot.Commands.ADDTASK;
                return new Trio<>(userCommand, null, taskTarget);
            } catch (TrackerBotException e) {
                ConsoleDisplayStyle.printBasicStyling(5, e.getMessage().length(), e.getMessage());
                return new Trio<>(TrackerBot.Commands.INVALID, null, null);
            }
        }

        return new Trio<>(TrackerBot.Commands.DEFAULT, null, null);
    }

}
