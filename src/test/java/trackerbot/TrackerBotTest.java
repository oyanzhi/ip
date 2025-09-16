package trackerbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrackerBotTest {
    private TrackerBot bot;

    @BeforeEach
    public void setUp() {
        bot = new TrackerBot();
    }

    @Test
    public void testAddTask() {
        String response = bot.getResponse("todo read book");
        assertTrue(response.contains("read book"), "Should confirm added task");
    }

    @Test
    public void testList() {
        bot.getResponse("todo write code");
        String response = bot.getResponse("list");
        assertTrue(response.contains("write code"), "List should contain added task");
    }

    @Test
    public void testMarkAndUnmark() {
        while (!bot.getResponse("list").contains("Empty List!")) {
            bot.getResponse("delete 1");
        }
        bot.getResponse("todo run");
        bot.getResponse("mark 1");
        String response = bot.getResponse("list");
        assertTrue(response.contains("[X]"), "Task should be marked as done");

        bot.getResponse("unmark 1");
        response = bot.getResponse("list");
        assertTrue(response.contains("[ ]"), "Task should be unmarked");
    }

    @Test
    public void testDeleteTask() {
        while (!bot.getResponse("list").contains("Empty List!")) {
            bot.getResponse("delete 1");
        }
        String response = bot.getResponse("todo eat lunch");
        assertTrue(response.contains("eat lunch"), "Should confirm deleted task");

        bot.getResponse("delete 1");
        response = bot.getResponse("list");
        assertFalse(response.contains("eat lunch"), "Deleted task should not appear in list");
    }

    @Test
    public void testFindTask() {
        bot.getResponse("todo read Java book");
        bot.getResponse("todo write notes");

        String response = bot.getResponse("find Java");
        assertTrue(response.contains("read Java book"), "Find should return matching task");
        assertFalse(response.contains("write notes"), "Find should not return unrelated task");
    }

    @Test
    public void testSortTasks() {
        bot.getResponse("todo C task");
        bot.getResponse("todo A task");
        bot.getResponse("sort");

        String response = bot.getResponse("list");
        assertTrue(response.indexOf("A task") < response.indexOf("C task"),
                "Tasks should be sorted alphabetically");
    }

    @Test
    public void testByeCommand() {
        String response = bot.getResponse("bye");
        assertEquals("Bye. Hope to see you again soon!",
                response,
                "Bye should return farewell message");
    }

    @Test
    public void testDefaultCommand() {
        String response = bot.getResponse("blahblah");
        assertTrue(response.contains("Possible Commands:"), "Default command should list possible commands");
    }
}
