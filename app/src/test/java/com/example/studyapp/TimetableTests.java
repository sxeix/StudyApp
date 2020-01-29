package com.example.studyapp;

import org.junit.Test;

import java.time.LocalDateTime;

import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;

import static org.junit.Assert.*;

public class TimetableTests {
    @Test
    public void uncheckedAdds() {
        Timetable timetable = new Timetable();

        assertEquals(0, timetable.getEvents().size());

        timetable.AddEventUnchecked(new TimetableEvent("", "", "",
                LocalDateTime.MIN, LocalDateTime.MIN));

        assertEquals(1, timetable.getEvents().size());

        timetable.AddEventUnchecked(new TimetableEvent("", "", "",
                LocalDateTime.MIN, LocalDateTime.MIN));

        assertEquals(2, timetable.getEvents().size());
    }
}
