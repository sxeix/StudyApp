package studynowbackend;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TimetableEvent implements Serializable {
    private String name;
    private String description;
    private String location;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean allDay;
    private RepeatFrequency repeatFrequency;

    public TimetableEvent(String name, String description, String location, LocalDateTime start, LocalDateTime end, boolean allDay, RepeatFrequency repeatFrequency) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.allDay = allDay;
        this.repeatFrequency = repeatFrequency;
    }

    public TimetableEvent(TimetableEvent event) {
        this.name = event.name;
        this.description = event.description;
        this.location = event.location;
        this.start = event.start;
        this.end = event.end;
        this.allDay = event.allDay;
        this.repeatFrequency = event.repeatFrequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public RepeatFrequency getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(RepeatFrequency repeatFrequency) {
        this.repeatFrequency = repeatFrequency;
    }

    public int compareTo(TimetableEvent other) {
        return start.compareTo(other.start);
    }
}

