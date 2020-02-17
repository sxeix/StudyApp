package studynowbackend;

import java.util.ArrayList;

public class Timetable {
    private ArrayList<TimetableEvent> events;

    public Timetable() {
        this.events = new ArrayList<>();
    }

    public ArrayList<TimetableEvent> getEvents() {
        return events;
    }

    public void AddEventUnchecked(TimetableEvent event) {
        this.events.add(event);
    }
}
