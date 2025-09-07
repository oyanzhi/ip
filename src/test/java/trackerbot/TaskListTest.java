package trackerbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import trackerbot.exceptions.TrackerBotException;
import trackerbot.tasks.Deadlines;
import trackerbot.tasks.Task;
import trackerbot.utils.TaskList;


public class TaskListTest {
    @Test
    public void getValidTaskFromList() {
        Task t = null;
        TaskList tList = new TaskList();
        try {
            t = new Deadlines("test", "1900-10-10 1000");
        } catch (TrackerBotException e) {
            fail();
            return;
        } finally {
            tList.addTask(t);
        }
        assertEquals(tList.getTask(0), t);
    }

    @Test
    public void addValidTaskToList() {
        Task t = null;
        TaskList tList = new TaskList();
        try {
            t = new Deadlines("test", "1900-10-10 1000");
        } catch (TrackerBotException e) {
            System.out.println("Invalid Input");
            return;
        } finally {
            tList.addTask(t);
        }
        assertEquals(tList.isEmpty(), false);
    }
}
