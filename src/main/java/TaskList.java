import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> taskList;

    public TaskList() {
        this.taskList = new ArrayList<>();
    }

    public int getSize() {
        return this.taskList.size();
    }

    public boolean isEmpty() {
        return this.taskList.isEmpty();
    }

    public void addTask(Task t) {
        this.taskList.add(t);
    }

    public void removeTask(int taskIndex) {
        this.taskList.remove(taskIndex);
    }

    public Task getTask(int taskIndex) {
        return this.taskList.get(taskIndex);
    }

    public void markTask(int taskIndex) {
        this.taskList.get(taskIndex).markAsDone();
    }

    public void unmarkTask(int taskIndex) {
        this.taskList.get(taskIndex).markAsUndone();
    }

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
