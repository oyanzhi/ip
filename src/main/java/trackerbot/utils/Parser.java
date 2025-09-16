package trackerbot.utils;

import trackerbot.TrackerBot;
import trackerbot.exceptions.TrackerBotException;
import trackerbot.tasks.Deadlines;
import trackerbot.tasks.Events;
import trackerbot.tasks.Task;
import trackerbot.tasks.ToDos;

/**
 * A static class that contains all the utility methods for parsing user input
 */
public abstract class Parser {

    /**
     * A method used to parse user inputs
     * @param userInput String representing the user input
     * @param taskList Current instance memory of the task list in the running process
     * @return A Trio where the head is the command, body is the task index, and tail is a new list of Tasks
     */
    public static Trio<TrackerBot.Commands, Integer, TaskList> parseUserInput(
            String userInput, TaskList taskList) throws TrackerBotException {

        int taskIndex = -1;
        TaskList tList = new TaskList();

        //to parse utility inputs
        if (userInput.startsWith("list")) {
            return new Trio<>(TrackerBot.Commands.LIST, null, null);
        }

        if (userInput.startsWith("bye")) {
            return new Trio<>(TrackerBot.Commands.BYE, null, null);
        }

        if (userInput.startsWith("find")) {
            if ("find".length() + 1 > userInput.length()) {
                throw new TrackerBotException("Missing Arguments! Example usage 'find text'");
            }
            String toSearchDescription = userInput.substring(5);
            tList = taskList.getAllRelatedTask(toSearchDescription);
            return new Trio<>(TrackerBot.Commands.FIND, null, tList);
        }

        if (userInput.startsWith("sort")) {
            return new Trio<>(TrackerBot.Commands.SORT, null, null);
        }

        //to parse input of mark
        if (userInput.startsWith("mark")) {
            try {
                if ("mark".length() + 1 > userInput.length()) {
                    throw new TrackerBotException("Missing Arguments! Example usage 'mark 1'");
                }
                taskIndex = Integer.parseInt(userInput.substring("mark".length() + 1)) - 1;
                if (taskIndex < 0 || taskIndex >= taskList.getSize()) {
                    throw new TrackerBotException("Invalid Task Index");
                }
                Task taskTarget = taskList.getTask(taskIndex);
                tList.addTask(taskTarget);
                return new Trio<>(TrackerBot.Commands.MARK, taskIndex, tList);
            } catch (NumberFormatException e) {
                String message = "Missing Task Index. Example usage 'mark 1'";
                throw new TrackerBotException(message);
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
                    throw new TrackerBotException("Invalid Task Index");
                }
                Task taskTarget = taskList.getTask(taskIndex);
                tList.addTask(taskTarget);
                return new Trio<>(TrackerBot.Commands.UNMARK, taskIndex, tList);
            } catch (NumberFormatException e) {
                String message = "Missing Task Index. Example usage 'unmark 1'";
                throw new TrackerBotException(message);
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
                    throw new TrackerBotException("Invalid Task Index");
                }
                Task taskTarget = taskList.getTask(taskIndex);
                tList.addTask(taskTarget);
                return new Trio<>(TrackerBot.Commands.DELETE, taskIndex, tList);
            } catch (NumberFormatException e) {
                String message = "Missing Task Index. Example usage 'delete 1'";
                throw new TrackerBotException(message);
            }
        }

        //to parse input of task additions
        if (userInput.startsWith("todo")) {
            if ("todo".length() + 1 > userInput.length()) {
                throw new TrackerBotException("Missing Arguments! Example usage 'todo tasking'");
            }
            String taskDescription = userInput.substring("todo ".length());
            if (taskDescription.isEmpty()) {
                throw new TrackerBotException("Missing Description. Example usage 'todo tasking'");
            }
            Task taskTarget = new ToDos(taskDescription);
            TrackerBot.Commands userCommand = TrackerBot.Commands.ADDTASK;
            tList.addTask(taskTarget);
            return new Trio<>(userCommand, taskList.getSize() - 1, tList);
        }

        if (userInput.startsWith("deadline")) {
            int deadlineIndex = userInput.indexOf("/by ");
            if ("deadline".length() + 1 > userInput.length() || deadlineIndex <= 9
                    || userInput.charAt(deadlineIndex - 1) != ' ') {
                throw new TrackerBotException("Missing Arguments! Example usage 'deadline tasking /by date'");
            }

            //-1 to account for space between description and /by
            String taskDescription = userInput.substring("deadline ".length(), deadlineIndex - 1);
            String deadline = userInput.substring(deadlineIndex + "/by ".length());

            Task taskTarget = new Deadlines(taskDescription, deadline);
            TrackerBot.Commands userCommand = TrackerBot.Commands.ADDTASK;
            tList.addTask(taskTarget);
            return new Trio<>(userCommand, taskList.getSize() - 1, tList);
        }

        if (userInput.startsWith("event")) {
            int startDateIndex = userInput.indexOf("/from ");
            int endDateIndex = userInput.indexOf("/to ");

            if (("event".length() + 1) > userInput.length() || startDateIndex <= 7 || endDateIndex <= 15
                    || userInput.charAt(startDateIndex - 1) != ' '
                    || userInput.charAt(endDateIndex - 1) != ' ') {
                throw new TrackerBotException("Missing Arguments! "
                        + "Example usage 'event tasking /from startDate /to endDate");
            }

            //-1 to account for space between description and /from
            String taskDescription = userInput.substring("event ".length(), startDateIndex - 1);

            // -1 for proper spacing between /from [start] /to [end]
            String startDate = userInput.substring(startDateIndex + "/from ".length(), endDateIndex - 1);
            String endDate = userInput.substring(endDateIndex + "/to ".length());

            Task taskTarget = new Events(taskDescription, startDate, endDate);
            TrackerBot.Commands userCommand = TrackerBot.Commands.ADDTASK;
            tList.addTask(taskTarget);
            return new Trio<>(userCommand, taskList.getSize() - 1, tList);
        }

        return new Trio<>(TrackerBot.Commands.DEFAULT, null, null);
    }

}
