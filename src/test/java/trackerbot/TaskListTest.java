package trackerbot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
