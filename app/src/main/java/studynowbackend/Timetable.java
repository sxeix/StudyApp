
package studynowbackend;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.example.studyapp.BuildConfig;
import com.example.studyapp.InputPage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class Timetable {
    private static final String SAVE_FILE_NAME = "TimetableSave";

    private static Timetable instance = new Timetable();

    private ArrayList<TimetableEvent> events;
    private ArrayList<TimetableEvent> dailyEvents;
    private ArrayList<TimetableEvent> weeklyEvents;
    private ArrayList<TimetableEvent> monthlyEvents;
    private ArrayList<TimetableEvent> yearlyEvents;
    private ArrayList<Course> courses;

    private Context context;

    public void load(Context context) {
        this.context = context;

        ObjectInputStream objIn = null;
        try {
            FileInputStream in = context.openFileInput(SAVE_FILE_NAME);
            objIn = new ObjectInputStream(in);

            instance.events = (ArrayList<TimetableEvent>) objIn.readObject();
            instance.dailyEvents = (ArrayList<TimetableEvent>) objIn.readObject();
            instance.weeklyEvents = (ArrayList<TimetableEvent>) objIn.readObject();
            instance.monthlyEvents = (ArrayList<TimetableEvent>) objIn.readObject();
            instance.yearlyEvents = (ArrayList<TimetableEvent>) objIn.readObject();
            instance.courses = (ArrayList<Course>) objIn.readObject();

            instance.events.sort(TimetableEvent::compareTo);
            instance.dailyEvents.sort(TimetableEvent::compareTo);
            instance.weeklyEvents.sort(TimetableEvent::compareTo);
            instance.monthlyEvents.sort(TimetableEvent::compareTo);
            instance.yearlyEvents.sort(TimetableEvent::compareTo);


        } catch (FileNotFoundException ignored) {

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (objIn != null) {
                try {
                    objIn.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public boolean save() {
        ObjectOutputStream objOut = null;

        boolean saved = false;
        try {

            FileOutputStream fileOut = context.openFileOutput(SAVE_FILE_NAME, Activity.MODE_PRIVATE);
            objOut = new ObjectOutputStream(fileOut);

            objOut.writeObject(instance.courses);
            objOut.writeObject(instance.events);
            objOut.writeObject(instance.dailyEvents);
            objOut.writeObject(instance.weeklyEvents);
            objOut.writeObject(instance.monthlyEvents);
            objOut.writeObject(instance.yearlyEvents);

            fileOut.getFD().sync();
            saved = true;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objOut != null) {
                try {
                    objOut.close();
                } catch (IOException e) {
                    // do nothing
                }
            }
        }
        return saved;
    }

    private Timetable() {
        this.events = new ArrayList<>();
        this.dailyEvents = new ArrayList<>();
        this.weeklyEvents = new ArrayList<>();
        this.monthlyEvents = new ArrayList<>();
        this.yearlyEvents = new ArrayList<>();
        this.courses = new ArrayList<>();
    }

    public static Timetable getInstance() {
        return instance;
    }

    public ArrayList<TimetableEvent> getEvents() {
        return events;
    }
    public ArrayList<TimetableEvent> getDailyEvents() {
        return dailyEvents;
    }
    public ArrayList<TimetableEvent> getWeeklyEvents() {
        return weeklyEvents;
    }
    public ArrayList<TimetableEvent> getMonthlyEvents() {
        return monthlyEvents;
    }
    public ArrayList<TimetableEvent> getYearlyEvents() {
        return yearlyEvents;
    }

    public void AddEvent(TimetableEvent event) {
        switch (event.getRepeatFrequency()) {
            case NoRepeat:
                this.events.add(event);
                this.events.sort(TimetableEvent::compareTo); break;
            case Daily:
                this.dailyEvents.add(event);
                this.dailyEvents.sort(TimetableEvent::compareTo); break;
            case Weekly:
                this.weeklyEvents.add(event);
                this.weeklyEvents.sort(TimetableEvent::compareTo); break;
            case Monthly:
                this.monthlyEvents.add(event);
                this.monthlyEvents.sort(TimetableEvent::compareTo); break;
            case Yearly:
                this.yearlyEvents.add(event);
                this.yearlyEvents.sort(TimetableEvent::compareTo); break;
        }
        save();
    }

    public void AddCourse(Course course) {
        courses.add(course);
    }

    public Course removeCourse(String name) {
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getName().equals(name)) {
                return courses.remove(i);
            }
        }
        return null;
    }

    private void addEventsOnDayOfWeek(ArrayList<TimetableEvent> events, DayOfWeek dayOfWeek) {
        for (TimetableEvent event : this.weeklyEvents) {
            DayOfWeek day = event.getStart().getDayOfWeek();
            DayOfWeek endDay = event.getStart().getDayOfWeek();

            if (day == dayOfWeek) {
                events.add(event);
                InputPage.sortList(event, events, 2);
                continue;
            }

            // Adds event if day is part of event
            while (day != endDay) {
                day = day.plus(1);

                if (day == dayOfWeek) {
                    events.add(event);
                    InputPage.sortList(event, events, 2);
                    break;
                }
            }
        }
    }

    private void addEventsOnDayOfMonth(ArrayList<TimetableEvent> events, int dayOfMonth) {
        for (TimetableEvent event : this.monthlyEvents) {
            int day = event.getStart().getDayOfMonth();
            int endDay = event.getStart().getDayOfMonth();

            if (day == dayOfMonth) {
                events.add(event);
                InputPage.sortList(event, events, 2);
                continue;
            }

            // Adds event if day is part of event
            while (day != endDay) {
                if (day < 31) {
                    day++;
                } else {
                    day = 0;
                }

                if (day == dayOfMonth) {
                    events.add(event);
                    InputPage.sortList(event, events, 2);
                    break;
                }
            }
        }
    }

    private void addEventsOnDayOfYear(ArrayList<TimetableEvent> events, int dayOfYear) {
        for (TimetableEvent event : this.yearlyEvents) {
            int day = event.getStart().getDayOfYear();
            int endDay = event.getStart().getDayOfYear();

            if (day == dayOfYear) {
                events.add(event);
                InputPage.sortList(event, events, 2);
                continue;
            }

            // Adds event if day is part of event
            while (day != endDay) {
                if (day < 366) {
                    day++;
                } else {
                    day = 0;
                }

                if (day == dayOfYear) {
                    events.add(event);
                    InputPage.sortList(event, events, 2);
                    break;
                }
            }
        }
    }

    private void addNormalEvents(ArrayList<TimetableEvent> events, LocalDate date) {
        if (events.size() > 512) {
            this.events.parallelStream().forEach((event) -> {
                LocalDate start = event.getStart().toLocalDate();
                LocalDate end = event.getEnd().toLocalDate();

                if (!(date.isBefore(start) || date.isAfter(end))) {
                    events.add(event);
                    InputPage.sortList(event, events, 2);
                }
            });
        } else {
            for (TimetableEvent event : this.events) {
                LocalDate start = event.getStart().toLocalDate();
                LocalDate end = event.getEnd().toLocalDate();

                if (!(date.isBefore(start) || date.isAfter(end))) {
                    events.add(event);
                    InputPage.sortList(event, events, 2);
                }
            }
        }
    }

    public ArrayList<TimetableEvent> getEventsOnDay(LocalDate date) {
        // Add add all daily events as they happen on all days
        ArrayList<TimetableEvent> events = new ArrayList<>(this.dailyEvents);

        // Add all weekly events that share the same day of the week
        addEventsOnDayOfWeek(events, date.getDayOfWeek());

        // Add all weekly events that share the same day of the week
        addEventsOnDayOfMonth(events, date.getDayOfMonth());

        // Add all weekly events that share the same day of the week
        addEventsOnDayOfYear(events, date.getDayOfYear());

        // Add all normal events sharing a day
        addNormalEvents(events, date);

        return events;
    }

    public void removeEvent(TimetableEvent event) {
        switch (event.getRepeatFrequency()) {
            case NoRepeat:
                this.events.remove(event); break;
            case Daily:
                this.dailyEvents.remove(event); break;
            case Weekly:
                this.weeklyEvents.remove(event); break;
            case Monthly:
                this.monthlyEvents.remove(event); break;
            case Yearly:
                this.yearlyEvents.remove(event); break;
        }
        save();
    }

    public ArrayList<TimeSlot> getFreeSlotsForWeekStarting(LocalDate date) {
        ArrayList<TimetableEvent> eventsInWeek = new ArrayList<>();

        LocalDateTime dateTime = LocalDateTime.from(date);

        int i = 0;
        // Get Events that start before and end after the start date
        for (; i < events.size(); i++) {
            TimetableEvent event = events.get(i);
            if (event.getStart().compareTo(dateTime) >= 0) {
                break;
            }
            if (event.getStart().compareTo(dateTime) < 0 && event.getEnd().compareTo(dateTime) > 0) {
                eventsInWeek.add(event);
            }
        }

        // Get all Fixed Events
        for (; i < events.size() - 1; i++) {
            TimetableEvent event = events.get(i);
            eventsInWeek.add(event);
            if (event.getEnd().compareTo(dateTime) > 0) {
                break;
            }
        }

        // Add all Daily Events
        for (TimetableEvent event : dailyEvents) {
            LocalTime startTime = event.getStart().toLocalTime();
            LocalTime endTime = event.getEnd().toLocalTime();

            for (int j = 0; j < 7; j++) {
                LocalDate eventDate = date.plusDays(j);
                LocalDateTime start = LocalDateTime.of(eventDate, startTime);
                LocalDateTime end = LocalDateTime.of(eventDate, endTime);

                event = new TimetableEvent(event);

                event.setStart(start);
                event.setEnd(end);

                eventsInWeek.add(event);
            }
        }

        // Add all Weekly Events
        for (TimetableEvent event : weeklyEvents) {
            LocalTime startTime = event.getStart().toLocalTime();
            DayOfWeek startDay = event.getStart().getDayOfWeek();
            LocalTime endTime = event.getEnd().toLocalTime();
            DayOfWeek endDay = event.getEnd().getDayOfWeek();

            // Calculate start date
            LocalDate startDate = date;
            while (startDate.getDayOfWeek() != startDay) {
                startDate = startDate.plusDays(1);
            }

            // Calculate end date
            LocalDate endDate = date;
            while (endDate.getDayOfWeek() != endDay) {
                endDate = endDate.plusDays(1);
            }

            LocalDateTime start = LocalDateTime.of(startDate, startTime);
            LocalDateTime end = LocalDateTime.of(endDate, endTime);

            event = new TimetableEvent(event);

            event.setStart(start);
            event.setEnd(end);

            eventsInWeek.add(event);
        }

        // Add Monthly Events in range
        for (TimetableEvent event : monthlyEvents) {
            LocalDateTime start = event.getStart();
            LocalDateTime end = event.getEnd();
            start = LocalDateTime.of(date.getYear(), date.getMonth(), start.getDayOfMonth(), start.getHour(), start.getMinute(), start.getSecond());
            end = LocalDateTime.of(date.getYear(), date.getMonth(), end.getDayOfMonth(), end.getHour(), end.getMinute(), end.getSecond());

            boolean startInRange = false;
            for (i = 0; i < 7; i++) {
                if (start.toLocalDate().equals(date.plusDays(i))) {
                    startInRange = true;
                    break;
                }
            }

            boolean endInRange = false;
            for (i = 0; i < 7; i++) {
                if (end.toLocalDate().equals(date.plusDays(i))) {
                    endInRange = true;
                    break;
                }
            }

            if (startInRange && endInRange) {
                event = new TimetableEvent(event);

                event.setStart(start);
                event.setEnd(end);

                eventsInWeek.add(event);
            }
        }

        // Add Yearly Events in range
        for (TimetableEvent event : yearlyEvents) {
            LocalDateTime start = event.getStart();
            LocalDateTime end = event.getEnd();
            start = LocalDateTime.of(date.getYear(), start.getMonth(), start.getDayOfMonth(), start.getHour(), start.getMinute(), start.getSecond());
            end = LocalDateTime.of(date.getYear(), end.getMonth(), end.getDayOfMonth(), end.getHour(), end.getMinute(), end.getSecond());

            boolean startInRange = false;
            for (i = 0; i < 7; i++) {
                if (start.toLocalDate().equals(date.plusDays(i))) {
                    startInRange = true;
                    break;
                }
            }

            boolean endInRange = false;
            for (i = 0; i < 7; i++) {
                if (end.toLocalDate().equals(date.plusDays(i))) {
                    endInRange = true;
                    break;
                }
            }

            if (startInRange && endInRange) {
                event = new TimetableEvent(event);

                event.setStart(start);
                event.setEnd(end);

                eventsInWeek.add(event);
            }
        }

        eventsInWeek.sort(TimetableEvent::compareTo);


        // Find all Free Slots
        ArrayList<TimeSlot> freeSlots = new ArrayList<>();

        // Get space at the start
        if (eventsInWeek.size() > 0) {
            TimetableEvent event = eventsInWeek.get(0);

            if (event.getStart().compareTo(dateTime) > 0) {
                TimeSlot timeSlot = new TimeSlot(dateTime, event.getStart());
            }
        }
        // Find space between events
        for (i = 0; i < eventsInWeek.size() - 1; i++) {
            TimetableEvent event = weeklyEvents.get(i);
            TimetableEvent nextEvent = weeklyEvents.get(i + 1);

            if (event.getEnd().compareTo(nextEvent.getStart()) >= 0)
                continue;

            TimeSlot timeSlot = new TimeSlot(event.getEnd(), nextEvent.getStart());
            freeSlots.add(timeSlot);
        }
        // Get space at the end
        if (eventsInWeek.size() > 1) {
            TimetableEvent event = eventsInWeek.get(0);

            LocalDateTime endDate = dateTime.plusDays(7);

            if (event.getEnd().compareTo(endDate) < 0) {
                TimeSlot timeSlot = new TimeSlot(event.getEnd(), endDate);
                freeSlots.add(timeSlot);
            }
        }

        return freeSlots;
    }

    public ArrayList<TimeSlot> getSlotsAvailableForRevision(LocalDate date) {
        ArrayList<TimeSlot> revisionSlots = new ArrayList<>();

        ArrayList<TimeSlot> temp = getFreeSlotsForWeekStarting(date);

        // Get all free slot with a minimum time of half an hour
        ArrayList<TimeSlot> freeSlotsLeft = new ArrayList<>();
        for (TimeSlot timeSlot : temp) {
            if (timeSlot.getDuration().getMinutes() >= 30) {
                freeSlotsLeft.add(timeSlot);
            }
        }

        // Add all free slots between half an hour and 2 hours
        temp = new ArrayList<>();
        for (TimeSlot timeSlot : freeSlotsLeft) {
            if (timeSlot.getDuration().isBetween(new Duration(30), new Duration(120))) {
                revisionSlots.add(timeSlot);
            } else {
                temp.add(timeSlot);
            }
        }
        freeSlotsLeft = temp;

        // Break down slots longer than 2 hours (make them in to slots of 1 hour)
        for (TimeSlot timeSlot : freeSlotsLeft) {
            int numSlots = timeSlot.getDuration().getMinutes() / 70;

            for (int i = 0; i < numSlots; i++) {
                LocalDateTime start = timeSlot.getStart().plusMinutes(70 * i);
                LocalDateTime end = start.plusMinutes(60);

                revisionSlots.add(new TimeSlot(start, end));
            }
        }

        return revisionSlots;
    }

    public ArrayList<CourseTimeSlot> GetCourseTimeSlots(LocalDate date) {

        ArrayList<TimeSlot>[] days = new ArrayList[7];
        for (int i = 0; i < 7; i++) {
            days[i] = new ArrayList<>();
        }


        for (TimeSlot timeSlot : getSlotsAvailableForRevision(date)) {
            for (int i = 0; i < 7; i++) {
                if (timeSlot.getStart().toLocalDate().equals(date.plusDays(i))) {
                    days[i].add(timeSlot);
                    break;
                }
            }
        }

        ArrayList<CourseTimeSlot> revisionTimeSlots = new ArrayList<>();

        for (Course course : courses) {
            int weeksLeft = (int) date.until(course.getEndDate(), ChronoUnit.WEEKS);

            int hoursToDoThisWeek = course.getRemainingStudyHours() / weeksLeft;
            int hoursToDoPerDay = hoursToDoThisWeek / 7;

            for (ArrayList<TimeSlot> day : days) {
                int minutesToGo = hoursToDoPerDay * 60;
                for (int i = 0; i < day.size(); i++) {
                    TimeSlot timeSlot = day.get(i);
                    if (timeSlot == null)
                        continue;
                    if (minutesToGo <= 0)
                        break;
                    revisionTimeSlots.add(new CourseTimeSlot(timeSlot.getStart(), timeSlot.getEnd(), course));
                    minutesToGo -= timeSlot.getDuration().getMinutes();
                    day.set(i, null);
                }
            }
        }

        return revisionTimeSlots;
    }
}