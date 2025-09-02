package trackerbot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A class used for File Management of the Task Storage
 */
public final class FileIO {
    private File f;

    /**
     * Creates a Storage FileIO
     * @throws TrackerBotException Error thrown when the directory or file is unable to be created
     */
    public FileIO() throws TrackerBotException {
        //Set-Up Stored File Path
        String currDir = System.getProperty("user.dir");

        //to create parent directories
        Path dataFolderDir = Paths.get(currDir, "data");
        try {
            Files.createDirectories(dataFolderDir);
        } catch (IOException e) {
            throw new TrackerBotException("Could Not Create Data Folder. Please Try Again.");
        }

        //actual storage file
        Path p = Paths.get(dataFolderDir.toString(), "task_data");
        try {
            boolean fileExists = Files.exists(p);
            if (!fileExists) {
                throw new FileNotFoundException();
            }
            this.f = p.toFile();
        } catch (FileNotFoundException e) {
            //lets user know missing file - to create one
            System.out.println(e.getMessage() + "\n" + "Missing Storage File. Creating a Storage File Now...");
            try {
                Files.createFile(p);
            } catch (IOException ex) {
                throw new TrackerBotException("Cannot Create File. Please try again.");
            } finally {
                this.f = p.toFile();
            }
        }
    }

    /**
     * A method for reading the contents of the file
     * @return A TaskList that contains all the tasks that are stored in the file
     * @throws FileNotFoundException Thrown when the scanner cannot be created off the stored file
     * @throws TrackerBotException Thrown when the storage file is invalid format or task cannot be created
     */
    public TaskList readFileContents() throws FileNotFoundException, TrackerBotException {
        Scanner s = new Scanner(this.f); // might throw FileNotFoundException
        TaskList taskList = new TaskList();

        /*
         * Expected Format:
         * [TDE] | [01] | "task" | "deadline/ start date" | "end date"
         */
        while (s.hasNextLine()) {
            String line = s.nextLine();
            if (Pattern.matches("[TDE] \\| [01] \\| .+( \\| .+ )?(\\| .+)?", line)) {
                try {
                    Task t = createTask(line);
                    taskList.addTask(t);
                } catch (TrackerBotException e) {
                    throw new TrackerBotException("Could Not Create Task from Storage.");
                }
            } else {
                throw new TrackerBotException("Invalid Storage File Format!");
            }
        }
        return taskList;
    }

    /**
     * Creates a task from individual lines from the storage file
     * @param input The line being read from the storage file
     * @return Task that is created based on each given line
     * @throws TrackerBotException Thrown when any of the task cannot be created
     */
    public static Task createTask(String input) throws TrackerBotException {
        String[] splitLine = input.split(" \\| ");
        String type = splitLine[0];
        String isCompleted = splitLine[1];
        String taskDescription = splitLine[2];
        Task t = null;
        switch (type) {
        case "T":
            t = new ToDos(taskDescription);
            if (isCompleted.equals("1")) {
                t.markAsDone();
            } else {
                t.markAsUndone();
            }
            break;

        case "D":
            String deadline = splitLine[3];
            t = new Deadlines(taskDescription, deadline);
            if (isCompleted.equals("1")) {
                t.markAsDone();
            } else {
                t.markAsUndone();
            }
            break;

        case "E":
            String startDate = splitLine[3];
            String endDate = splitLine[4];
            t = new Events(taskDescription, startDate, endDate);
            if (isCompleted.equals("1")) {
                t.markAsDone();
            } else {
                t.markAsUndone();
            }
            break;
        }
        return t;
    }

    /**
     * Edits the storage file based on interactions with the bot
     * @param t Task that is being targeted in the file
     * @param taskList Current instance of all tasks in the running process of TrackerBot
     * @param isAppend Boolean on whether the to rewrite the file or add a next line to the file
     * @throws IOException Thrown when the file cannot be edited
     */
    public void writeToFile(Task t, TaskList taskList, boolean isAppend) throws IOException {

        //inner class for parsing and writing to file
        class editFile {
            public static void addTask(Task t, FileWriter fw) throws IOException {
                //appends to the end of task list
                //requires parse
                String taskParse = t.toString();
                char type = taskParse.charAt(1);
                char isDone = (taskParse.charAt(4) == 'X') ? '1' : '0';
                String toAdd = String.format("%s | %s | ", type, isDone);
                switch (type) {
                case 'T':
                    String todoDescription = taskParse.substring(7);
                    fw.write(toAdd + todoDescription + System.lineSeparator());
                    break;

                case 'D':
                    int deadlineIndex = taskParse.indexOf(" (by: ");
                    String deadlineDescription = taskParse.substring(7, deadlineIndex);
                    String deadlineDate = taskParse.substring(deadlineIndex + 6, taskParse.length() - 1);
                    fw.write(toAdd + deadlineDescription + " | " + deadlineDate + System.lineSeparator());
                    break;

                case 'E':
                    int startDateIndex = taskParse.indexOf(" (from: ");
                    int endDateIndex = taskParse.indexOf(" to: ");
                    String eventDescription = taskParse.substring(7, startDateIndex);
                    String startDate = taskParse.substring(startDateIndex + 8, endDateIndex);
                    String endDate = taskParse.substring(endDateIndex + 5, taskParse.length() - 1);
                    fw.write(toAdd + eventDescription + " | " + startDate + " | " + endDate + System.lineSeparator());
                    break;
                }
            }
        }

        if (isAppend) { //singular task
            FileWriter fw = new FileWriter(this.f, true);
            editFile.addTask(t, fw);
            fw.close();
        } else { //requires rewrite to file

            //clears the file
            FileWriter fwClear = new FileWriter(this.f, false);
            fwClear.close();

            for (int i = 0; i < taskList.getSize(); i++) {
                FileWriter fwAdd = new FileWriter(this.f, true);
                editFile.addTask(taskList.getTask(i), fwAdd);
                fwAdd.close();
            }
        }

    }

}
