package EventManager;

import EventManager.app.*;
import EventManager.app.Engine;
import EventManager.io.ConsoleReader;
import EventManager.io.ConsoleWriter;
import EventManager.io.Reader;
import EventManager.io.Writer;

import java.io.IOException;
import java.text.ParseException;


public class Main {

    public static void main(String[] args) throws ParseException, IOException {

        Reader reader = new ConsoleReader();
        Writer writer = new ConsoleWriter();

        CommandManger commandManger = new CommandManger(writer, reader);

        Runnable engine = new Engine(writer, reader, commandManger);
        engine.run();
    }
}
