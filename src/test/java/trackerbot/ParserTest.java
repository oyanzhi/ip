package trackerbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import trackerbot.exceptions.TrackerBotException;
import trackerbot.utils.Parser;
import trackerbot.utils.TaskList;
import trackerbot.utils.Trio;

public class ParserTest {
    @Test
    public void parseList() {
        //tests for excessive input behind list
        Trio<TrackerBot.Commands, Integer, TaskList> expected = new Trio<>(TrackerBot.Commands.LIST,
                null,
                null);
        Trio<TrackerBot.Commands, Integer, TaskList> actual = null;
        try {
            actual = Parser.parseUserInput("list test12",
                    new TaskList());
        } catch (TrackerBotException e) {
            fail();
        }
        assertEquals(actual.getHead(), expected.getHead());
        assertEquals(actual.getBody(), expected.getBody());
        assertEquals(actual.getTail(), expected.getTail());
    }

    @Test
    public void parseInvalidDeadlineTask() {
        try {
            Parser.parseUserInput("deadline testfail", new TaskList());
        } catch (TrackerBotException e) {
            assert(e.getMessage().equals("Missing Arguments! Example usage 'deadline tasking /by date'"));
        }

    }

}
