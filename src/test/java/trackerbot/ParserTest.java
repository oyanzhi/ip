package trackerbot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import trackerbot.exceptions.TrackerBotException;
import trackerbot.tasks.Task;
import trackerbot.tasks.ToDos;
import trackerbot.utils.Parser;
import trackerbot.utils.TaskList;
import trackerbot.utils.Trio;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    private TaskList taskList;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        taskList.addTask(new ToDos("sample task"));
    }

    @Test
    void testListCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("list", taskList);
        assertEquals(TrackerBot.Commands.LIST, result.getHead());
        assertNull(result.getBody());
        assertNull(result.getTail());
    }

    @Test
    void testByeCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("bye", taskList);
        assertEquals(TrackerBot.Commands.BYE, result.getHead());
    }

    @Test
    void testSortCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("sort", taskList);
        assertEquals(TrackerBot.Commands.SORT, result.getHead());
    }

    @Test
    void testFindCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("find sample", taskList);
        assertEquals(TrackerBot.Commands.FIND, result.getHead());
        assertNotNull(result.getTail());
        assertTrue(result.getTail().getSize() > 0);
    }

    @Test
    void testFindMissingArgs() {
        assertThrows(TrackerBotException.class, () -> Parser.parseUserInput("find", taskList));
    }

    @Test
    void testMarkCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("mark 1", taskList);
        assertEquals(TrackerBot.Commands.MARK, result.getHead());
        assertEquals(0, result.getBody());
        assertEquals(1, result.getTail().getSize());
    }

    @Test
    void testMarkInvalidIndex() {
        assertThrows(TrackerBotException.class, () -> Parser.parseUserInput("mark 5", taskList));
    }

    @Test
    void testUnmarkCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("unmark 1", taskList);
        assertEquals(TrackerBot.Commands.UNMARK, result.getHead());
        assertEquals(0, result.getBody());
    }

    @Test
    void testDeleteCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("delete 1", taskList);
        assertEquals(TrackerBot.Commands.DELETE, result.getHead());
        assertEquals(0, result.getBody());
    }

    @Test
    void testTodoCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("todo new task", taskList);
        assertEquals(TrackerBot.Commands.ADDTASK, result.getHead());
        Task added = result.getTail().getTask(0);
        assertTrue(added instanceof ToDos);
        assertTrue(added.checkDescription("new task"));
    }

    @Test
    void testTodoMissingArgs() {
        assertThrows(TrackerBotException.class, () -> Parser.parseUserInput("todo", taskList));
    }

    @Test
    void testDeadlineCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput(
                "deadline project /by 1000-10-10 1000", taskList);
        assertEquals(TrackerBot.Commands.ADDTASK, result.getHead());
        assertEquals(0, result.getBody());
        assertNotNull(result.getTail());
    }

    @Test
    void testEventCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput(
                "event party /from 1000-10-10 1000 /to 1000-10-10 1000", taskList);
        assertEquals(TrackerBot.Commands.ADDTASK, result.getHead());
        assertEquals(0, result.getBody());
        assertNotNull(result.getTail());
    }

    @Test
    void testDefaultCommand() throws TrackerBotException {
        Trio<TrackerBot.Commands, Integer, TaskList> result = Parser.parseUserInput("blahblah", taskList);
        assertEquals(TrackerBot.Commands.DEFAULT, result.getHead());
    }
}
