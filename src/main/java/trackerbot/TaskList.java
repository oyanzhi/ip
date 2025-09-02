package trackerbot;

import java.util.ArrayList;

/**
 * A class that is used to store a list of Tasks
 */
public class TaskList {
    private ArrayList<Task> taskList;

    /**
     * Initialises an empty ArrayList of type Task
     */
    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    /**
     * Gets the number of tasks stored
     * @return An int that refers to the number of tasks stored
     */
    public int getSize() {
        return this.taskList.size();
    }

    /**
     * @return A boolean on whether the current TaskList is empty
     */
    public boolean isEmpty() {
        return this.taskList.isEmpty();
    }

    /**
     * Adds a task to the TaskList
     */
    public void addTask(Task t) {
        this.taskList.add(t);
    }

    /**
     * Removes a task from the TaskList
     * @param taskIndex An index pointing to the actual position of the task in the TaskList not the displayed list
     */
    public void removeTask(int taskIndex) {
        this.taskList.remove(taskIndex);
    }

    /**
     * Gets the Task from the TaskList
     * @param taskIndex An index pointing to the actual position of the task in the TaskList not the displayed list
     * @return The task with the specified task index in the Task List
     */
    public Task getTask(int taskIndex) {
        return this.taskList.get(taskIndex);
    }

    /**
     * Marks a specific task in the Task List as completed
     * @param taskIndex An index pointing to the actual position of the task in the TaskList not the displayed list
     */
    public void markTask(int taskIndex) {
        this.taskList.get(taskIndex).markAsDone();
    }

    /**
     * Marks a specific task in the Task List as uncompleted
     * @param taskIndex An index pointing to the actual position of the task in the TaskList not the displayed list
     */
    public void unmarkTask(int taskIndex) {
        this.taskList.get(taskIndex).markAsUndone();
    }

    /**
     * Prints the Task List in the correct styling into the console
     * @param maxInputLength Used to format the horizontal line such that it covers the longest task row
     */
    public void printTaskList(int maxInputLength) {
        if (taskList.isEmpty()) { //No Texts Stored
            String defaultEmptyListText = "Empty List!";
            ConsoleDisplayStyle.printBasicStyling(4, 0, defaultEmptyListText);
        } else { //Texts Stored
            //Print from Instance Memory
            ConsoleDisplayStyle.printHorizontalLine(4, maxInputLength + 7);
            ConsoleDisplayStyle.printIndentation(4);
            System.out.printf("Here are the [%d] tasks in your list:\n", this.taskList.size());
            for (int i = 0; i < this.taskList.size(); i++) { //Non-Empty Texts Stored
                ConsoleDisplayStyle.printIndentation(4);
                String oneRow = String.format("%d. %s", i + 1, this.taskList.get(i));
                System.out.println(oneRow);
            }
            ConsoleDisplayStyle.printHorizontalLine(4, maxInputLength + 7);
        }
    }




}
