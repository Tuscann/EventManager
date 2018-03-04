package EventManager.app;

import EventManager.io.Reader;
import EventManager.io.Writer;

import java.io.IOException;
import java.text.ParseException;

public class Engine implements Runnable {

    private Reader reader;
    private Writer writer;
    private CommandManger commandManger;

    public Engine(Writer writer, Reader reader, CommandManger commandManger) {
        this.reader = reader;
        this.writer = writer;
        this.commandManger = commandManger;
    }
    boolean isFound = false;
    @Override
    public void run() {
        while (true) {
           if (isFound){
               break;
           }
            this.writer.writeLine("####################################################");
            this.writer.writeLine("For reading all events press: 1");
            this.writer.writeLine("For creating a new event press: 2");
            this.writer.writeLine("For updating a event press: 3");
            this.writer.writeLine("For deleting a event press: 4");
            this.writer.writeLine("For exit: 5");
            this.writer.writeLine("####################################################");

            try {
                String input = this.reader.readLine();
                if (input.equals("1") || input.equals("2") || input.equals("3") || input.equals("4") || input.equals("5")) {

                    int command = Integer.parseInt(input);
                    if (this.commandManger.manageCommand(command)) {
                        this.writer.writeLine("Thank you for using our Event Manage System!");
                        isFound = true;
                    }
                } else {
                    this.writer.writeLine("Invalid command!!");
                    this.writer.writeLine("Try another command.");
                    run();
                }

            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
