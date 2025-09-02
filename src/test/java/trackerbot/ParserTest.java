package trackerbot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {
    @Test
    public void parseList() {
        //tests for excessive input behind list
        Trio<TrackerBot.Commands, Integer, TaskList> expected = new Trio<>(TrackerBot.Commands.LIST
                , null
                , null);
        Trio<TrackerBot.Commands, Integer, TaskList> actual = Parser.parseUserInput("list test12"
                , new TaskList());
        assertEquals(actual.getHead(), expected.getHead());
        assertEquals(actual.getBody(), expected.getBody());
        assertEquals(actual.getTail(), expected.getTail());
    }

    @Test
    public void parseInvalidDeadlineTask() {
        Trio<TrackerBot.Commands, Integer, TaskList> expected = new Trio<>(TrackerBot.Commands.INVALID
                ,null
                , null);
        Trio<TrackerBot.Commands, Integer, TaskList> actual = Parser.parseUserInput(
                "deadline testfail"
                , new TaskList());
        assertEquals(actual.getHead(), expected.getHead());
        assertEquals(actual.getBody(), expected.getBody());
        assertEquals(actual.getTail(), expected.getTail());
    }

}
