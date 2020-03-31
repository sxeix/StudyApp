package com.example.studyapp;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import studynowbackend.Course;
import studynowbackend.CourseTimeSlot;
import studynowbackend.Duration;
import studynowbackend.RepeatFrequency;
import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;

import static org.junit.Assert.*;

public class BackendTests {
    @Test
    public void eventGetterTest() {
        TimetableEvent event = new TimetableEvent(
                "Event Name",
                "Description",
                "Location",
                LocalDateTime.of(2020, 3, 28, 12, 0, 0),
                LocalDateTime.of(2020, 3, 28, 12, 30, 0),
                false,
                RepeatFrequency.NoRepeat
        );

        assertEquals("Event Name", event.getName());
        assertEquals("Description", event.getDescription());
        assertEquals("Location", event.getLocation());
        assertEquals(LocalDateTime.of(2020, 3, 28, 12, 0, 0), event.getStart());
        assertEquals(LocalDateTime.of(2020, 3, 28, 12, 30, 0), event.getEnd());
        assertFalse(event.getAllDay());
        assertEquals(RepeatFrequency.NoRepeat, event.getRepeatFrequency());
    }

    @Test
    public void eventSetterTest() {
        TimetableEvent event = new TimetableEvent(
                "Event Name",
                "Description",
                "Location",
                LocalDateTime.of(2020, 3, 28, 12, 0, 0),
                LocalDateTime.of(2020, 3, 28, 12, 30, 0),
                false,
                RepeatFrequency.NoRepeat
        );

        event.setName("Test Name");
        assertEquals("Test Name", event.getName());

        event.setDescription("Test Description");
        assertEquals("Test Description", event.getDescription());

        event.setLocation("Test Location");
        assertEquals("Test Location", event.getLocation());

        event.setStart(LocalDateTime.of(1970, 5, 16, 5, 0, 0));
        assertEquals(LocalDateTime.of(1970, 5, 16, 5, 0, 0), event.getStart());

        event.setEnd(LocalDateTime.of(1970, 5, 16, 5, 30, 0));
        assertEquals(LocalDateTime.of(1970, 5, 16, 5, 30, 0), event.getEnd());

        event.setAllDay(true);
        assertTrue(event.getAllDay());

        event.setRepeatFrequency(RepeatFrequency.Weekly);
        assertEquals(RepeatFrequency.Weekly, event.getRepeatFrequency());
    }

    @Test
    public void eventComparisonTest() {
        TimetableEvent event = new TimetableEvent(
                "Event Name",
                "Description",
                "Location",
                LocalDateTime.of(2020, 3, 28, 12, 0, 0),
                LocalDateTime.of(2020, 3, 28, 12, 30, 0),
                false,
                RepeatFrequency.NoRepeat
        );
        TimetableEvent event2 = new TimetableEvent(
                "Event Name 2",
                "Description 46",
                "Location no",
                LocalDateTime.of(2020, 3, 28, 12, 0, 0),
                LocalDateTime.of(2020, 3, 28, 12, 30, 0),
                false,
                RepeatFrequency.NoRepeat
        );
        TimetableEvent eventBefore = new TimetableEvent(
                "Event Name",
                "Description",
                "Location",
                LocalDateTime.of(2020, 3, 28, 2, 0, 0),
                LocalDateTime.of(2020, 3, 28, 2, 30, 0),
                false,
                RepeatFrequency.NoRepeat
        );
        TimetableEvent eventAfter = new TimetableEvent(
                "Event Name",
                "Description",
                "Location",
                LocalDateTime.of(2020, 3, 29, 2, 0, 0),
                LocalDateTime.of(2020, 3, 30, 2, 30, 0),
                false,
                RepeatFrequency.NoRepeat
        );

        assertEquals(0, event.compareTo(event2));
        assertTrue(event.compareTo(eventBefore) > 0);
        assertTrue(event.compareTo(eventAfter) < 0);
    }

    @Test
    public void courseTest() {
        Course course = new Course("Innovative Product Development", 100, LocalDate.of(2020, Month.APRIL, 24));

        assertEquals("Innovative Product Development", course.getName());
        assertEquals(100, course.getRequiredStudyHours());
        assertEquals(6000, course.getRequiredStudyMinutes());
        assertEquals(100, course.getRemainingStudyHours());
        assertEquals(6000, course.getRemainingStudyMinutes());

        course.logMinutes(120);
        assertEquals(100, course.getRequiredStudyHours());
        assertEquals(6000, course.getRequiredStudyMinutes());
        assertEquals(98, course.getRemainingStudyHours());
        assertEquals(5880, course.getRemainingStudyMinutes());
    }

    // Saving and loading cannot be unit tested because it requires a context
    @Test
    public void timetableTest() {
        Timetable timetable = Timetable.getInstance();

        assertEquals(0, timetable.getEvents().size());
        assertEquals(0, timetable.getDailyEvents().size());
        assertEquals(0, timetable.getWeeklyEvents().size());
        assertEquals(0, timetable.getMonthlyEvents().size());
        assertEquals(0, timetable.getYearlyEvents().size());
        assertEquals(0, timetable.getCourses().size());

        // Add and remove courses
        Course course = new Course("Innovative Product Development",
                60, LocalDate.of(2020, 4, 24));

        timetable.addCourse(course);
        assertEquals(1, timetable.getCourses().size());
        assertEquals("Innovative Product Development", timetable.getCourses().get(0).getName());

        course = new Course("Web Application Development",
                70, LocalDate.of(2020, 4, 24));

        timetable.addCourse(course);
        assertEquals(2, timetable.getCourses().size());

        course = timetable.removeCourse("Innovative Product Development");
        assertEquals("Innovative Product Development", course.getName());
        assertEquals(60, course.getRequiredStudyHours());
        assertEquals(1, timetable.getCourses().size());
        assertEquals("Web Application Development", timetable.getCourses().get(0).getName());

        TimetableEvent event = new TimetableEvent("Event", "B", "Earth",
                LocalDateTime.of(2020, 3, 20, 12, 0, 0),
                LocalDateTime.of(2020, 3, 20, 14, 0, 0),
                false, RepeatFrequency.NoRepeat);
        timetable.addEvent(event);
        assertEquals(1, timetable.getEvents().size());

        ArrayList<CourseTimeSlot> timeSlots =  timetable.getCourseTimeSlots(
               LocalDate.of(2020, 4, 17));

        assertTrue(timeSlots.size() > 0);
        int totalMinutes = 0;
        for (CourseTimeSlot timeSlot : timeSlots) {
            int minutes = timeSlot.getDuration().getMinutes();
            totalMinutes += minutes;
            assertTrue(minutes <= 120);
            assertTrue(minutes >= 30);
        }
        assertEquals(70 * 60, totalMinutes);
    }
}
