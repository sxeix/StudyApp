package studynowbackend;

import java.time.LocalDate;

public class Course {
    private String name;
    private int requiredStudyMinutes;
    private int remainingStudyMinutes;
    private LocalDate endDate;

    public Course(String name, int requiredStudyHours, LocalDate endDate) {
        this.name = name;
        this.requiredStudyMinutes = requiredStudyHours * 60;
        this.remainingStudyMinutes = this.requiredStudyMinutes;
        this.endDate = endDate;
    }

    public int logMinutes(int minutes) {
        this.remainingStudyMinutes -= minutes;
        if (this.remainingStudyMinutes < 0) {
            this.remainingStudyMinutes = 0;
        }
        return this.remainingStudyMinutes;
    }

    public String getName() {
        return name;
    }

    public int getRequiredStudyMinutes() {
        return requiredStudyMinutes;
    }

    public int getRequiredStudyHours() {
        return requiredStudyMinutes / 60;
    }

    public int getRemainingStudyMinutes() {
        return remainingStudyMinutes;
    }

    public int getRemainingStudyHours() {
        return remainingStudyMinutes / 60;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
