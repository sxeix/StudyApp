package studynowbackend;

import java.time.LocalDateTime;

public class CourseTimeSlot extends TimeSlot {
    private Course course;

    CourseTimeSlot(LocalDateTime start, LocalDateTime end, Course course) {
        super(start, end);
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }
}
