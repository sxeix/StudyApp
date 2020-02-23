package studynowbackend;

import java.time.LocalDateTime;

public class TimetableEvent {
    private String name;
    private String description;
    private String location;
    private LocalDateTime start;
    private LocalDateTime end;
    private int type;

    public TimetableEvent(String name, String description, String location, LocalDateTime start, LocalDateTime end, int type) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.type = type;
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

    public int getType() { return type; }
}
