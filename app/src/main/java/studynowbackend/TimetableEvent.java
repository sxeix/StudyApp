package studynowbackend;

import java.time.LocalDateTime;

public class TimetableEvent {
    private String name;
    private String description;
    private String location;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean allDay;
    private RepeatFrequency repeatFrequency;

    @Deprecated
    public TimetableEvent(String name, String description, String location, LocalDateTime start, LocalDateTime end, int type) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;

        switch (type) {
            case 1:
                this.allDay = true;
                this.repeatFrequency = RepeatFrequency.NoRepeat;
                break;
            case 2:
                this.allDay = false;
                this.repeatFrequency = RepeatFrequency.Weekly;
                break;
            default:
                this.allDay = false;
                this.repeatFrequency = RepeatFrequency.NoRepeat;
                break;
        }
    }

    public TimetableEvent(String name, String description, String location, LocalDateTime start, LocalDateTime end, boolean allDay, RepeatFrequency repeatFrequency) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.start = start;
        this.end = end;
        this.allDay = allDay;
        this.repeatFrequency = repeatFrequency;
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

    @Deprecated
    public int getType() {
        if (!allDay && repeatFrequency == RepeatFrequency.NoRepeat) {
            return 0;
        } else if (allDay && repeatFrequency == RepeatFrequency.NoRepeat) {
            return 1;
        } else if (!allDay) {
            return 2;
        } else {
            return -1;
        }
    }
}
