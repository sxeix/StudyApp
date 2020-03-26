package studynowbackend;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Duration {
    private int minutes;

    public int getMinutes() {
        return minutes;
    }

    public Duration(LocalDateTime start, LocalDateTime end) {
        minutes = (int) start.until(end, ChronoUnit.MINUTES);
    }

    public Duration(int minutes) {
        this.minutes = minutes;
    }

    public boolean isBetween(Duration smaller, Duration bigger) {
        return minutes >= smaller.minutes &&
                minutes <= bigger.minutes;
    }
}
