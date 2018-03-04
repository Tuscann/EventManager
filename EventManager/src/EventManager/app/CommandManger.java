package EventManager.app;

import EventManager.Event;
import EventManager.io.Reader;
import EventManager.io.Writer;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CommandManger {

    private static final String DATABASE = "./database.txt";

    private Writer writer;
    private Reader reader;

    public CommandManger(Writer writer, Reader reader) throws ParseException, IOException {
        loadDatabase();

        this.writer = writer;
        this.reader = reader;
    }

    public boolean manageCommand(int command) throws ParseException, IOException {
        switch (command) {
            case 1:
                List<Event> events = readAllEvents();
                printEvents(events);
                break;
            case 2:
                Event event = creteNewEvent();
                writeToDatabase(event);

                this.writer.writeLine("Event created successfully!");
                break;
            case 3:
                updateEvent();
                break;
            case 4:
                deleteEvent();
                break;
            case 5:
                return true;
        }

        return false;
    }

    private List<Event> readAllEvents() throws ParseException, IOException {
        List<Event> events = new LinkedList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(DATABASE))) {

            String line = null;
            while ((line = br.readLine()) != null) {
                Event event = getEvent(line);
                events.add(event);
            }

        } catch (IOException e) {
            File file = new File(DATABASE);
            file.createNewFile();
        }

        return events;
    }

    private Event getEvent(String line) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        String[] tokens = line.split(" ");

        long id = Long.parseLong(tokens[0]);
        String name = tokens[1];
        String location = tokens[2];
        Date startDate = df.parse(tokens[3]);
        Date endDate = df.parse(tokens[4]);
        double duration = Double.parseDouble(tokens[5]);

        return new Event(id, name, location, startDate, endDate, duration);
    }

    private void printEvents(List<Event> events) {
        this.writer.writeLine("All events: ");

        for (Event event : events) {
            this.writer.writeLine("####################################################");
            this.writer.writeLine(event.format());
            this.writer.writeLine("####################################################");
        }
    }

    private void deleteEvent() throws ParseException, IOException {
        this.writer.write("Input Event's id to delete: ");
        long id = Long.parseLong(this.reader.readLine());

        List<Event> events = readAllEvents();
        List<Event> afterDeleting = new LinkedList<>();

        for (Event event : events) {
            if (event.getId() != id) {
                afterDeleting.add(event);
            }
        }

        if (events.size() != afterDeleting.size()) {
            this.writer.writeLine("Event with id: " + id + " has been deleted!");
        } else {
            this.writer.writeLine("Event with id: " + id + " does not exist!");
        }

        updateAllEvents(afterDeleting);
    }

    private void updateAllEvents(List<Event> events) {
        dropDatabase();

        for (Event event : events) {
            writeToDatabase(event);
        }
    }

    private void dropDatabase() {
        try {
            File file = new File(DATABASE);

            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateEvent() throws ParseException, IOException {
        this.writer.write("Input Event's id to update: ");
        long id = Long.parseLong(this.reader.readLine());

        boolean hasFound = false;
        List<Event> events = readAllEvents();
        for (Event event : events) {
            if (event.getId() == id) {
                Event editedEvent = creteNewEvent();

                mapEvents(event, editedEvent);
                hasFound = true;
                break;
            }
        }

        if (!hasFound)
            this.writer.writeLine("Event with id: " + id + " does not exist!");
        else {
            this.writer.writeLine("Event with id: " + id + " has been updated successfully!");

            dropDatabase();

            for (Event event : events) {
                writeToDatabase(event);
            }
        }
    }

    private void mapEvents(Event event, Event editedEvent) {
        event.setName(editedEvent.getName());
        event.setLocation(editedEvent.getLocation());
        event.setStartDate(editedEvent.getStartDate());
        event.setEndDate(editedEvent.getEndDate());
        event.setDuration(editedEvent.getDuration());
    }

    private Event creteNewEvent() throws ParseException, IOException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        this.writer.write("Name of the event: ");
        String name = this.reader.readLine();

        this.writer.write("Location of the event: ");
        String location = this.reader.readLine();

        this.writer.write("Start Date of the event(yyyy-MM-dd): ");
        Date startDate = df.parse(this.reader.readLine());

        this.writer.write("End Date of the event(yyyy-MM-dd): ");
        Date endDate = df.parse(this.reader.readLine());

        this.writer.write("Duration of the event: ");
        double duration = Double.parseDouble(this.reader.readLine());

        Event event = new Event(name, location, startDate, endDate, duration);
        return event;
    }

    private void writeToDatabase(Event event) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATABASE, true))) {
            bw.write(event.toString());
            bw.write(System.lineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadDatabase() throws ParseException, IOException {
        List<Event> events = readAllEvents();
        if (events.isEmpty()) Event.ID_COUNTER = 0;
        else Event.ID_COUNTER = events.get(events.size() - 1).getId() + 1;
    }
}
