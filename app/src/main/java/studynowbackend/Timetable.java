package studynowbackend;

import java.util.ArrayList;

public class Timetable {
    private static Timetable instance = new Timetable();

    private ArrayList<TimetableEvent> events;

    private Timetable() {
        this.events = new ArrayList<>();
    }

    public static Timetable getInstance() {
        return instance;
    }

    public ArrayList<TimetableEvent> getEvents() {
        return events;
    }

    public void AddEventUnchecked(TimetableEvent event) {
        this.events.add(event);
    }
}
