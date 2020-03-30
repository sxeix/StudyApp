package studynowbackend;

import java.time.LocalDateTime;

public class TimeSlot {
    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;

    TimeSlot(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
        this.duration = new Duration(start, end);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
        this.duration = new Duration(this.start, this.end);
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
        this.duration = new Duration(this.start, this.end);
    }
}
