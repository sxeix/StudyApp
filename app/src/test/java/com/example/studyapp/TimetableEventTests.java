package com.example.studyapp;

import org.junit.Test;

import java.time.LocalDateTime;

import studynowbackend.RepeatFrequency;
import studynowbackend.TimetableEvent;

import static org.junit.Assert.*;

public class TimetableEventTests {
    @Test
    public void getterTest() {
        TimetableEvent event = new TimetableEvent(
                "test name",
                "test description",
                "test location",
                LocalDateTime.of(1999, 3, 23, 12, 33),
                LocalDateTime.of(1999, 3, 23, 19, 33),
                true, RepeatFrequency.NoRepeat
        );
        assertEquals("test name", event.getName());
        assertEquals("test description", event.getDescription());
        assertEquals("test location", event.getLocation());
        assertEquals(LocalDateTime.of(1999, 3, 23, 12, 33), event.getStart());
        assertEquals(LocalDateTime.of(1999, 3, 23, 19, 33), event.getEnd());
    }

    @Test
    public void setterTest() {
        TimetableEvent event = new TimetableEvent(
                "test name",
                "test description",
                "test location",
                LocalDateTime.of(1999, 3, 23, 12, 33),
                LocalDateTime.of(1999, 3, 23, 19, 33),
                false, RepeatFrequency.Daily
        );
        assertEquals("test name", event.getName());
        event.setName("new test name");
        assertEquals("new test name", event.getName());

        assertEquals("test description", event.getDescription());
        event.setDescription("a new test description");
        assertEquals("a new test description", event.getDescription());

        assertEquals("test location", event.getLocation());
        event.setLocation("new test location");
        assertEquals("new test location", event.getLocation());

        assertEquals(LocalDateTime.of(1999, 3, 23, 12, 33), event.getStart());
        event.setStart(LocalDateTime.of(1970, 3, 23, 12, 0));
        assertEquals(LocalDateTime.of(1970, 3, 23, 12, 0), event.getStart());

        assertEquals(LocalDateTime.of(1999, 3, 23, 19, 33), event.getEnd());
        event.setEnd(LocalDateTime.of(1975, 3, 23, 12, 0));
        assertEquals(LocalDateTime.of(1975, 3, 23, 12, 0), event.getEnd());
    }
}
