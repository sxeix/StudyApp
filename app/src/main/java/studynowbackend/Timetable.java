
package studynowbackend;

import android.app.Activity;
import android.content.Context;

import com.example.studyapp.InputPage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Timetable {
    private static final String SAVE_FILE_NAME = "Timetable";

    private static Timetable instance = new Timetable();

    private ArrayList<TimetableEvent> events;
    private ArrayList<TimetableEvent> dailyEvents;
    private ArrayList<TimetableEvent> weeklyEvents;
    private ArrayList<TimetableEvent> monthlyEvents;
    private ArrayList<TimetableEvent> yearlyEvents;

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
                    // do nowt
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
                this.events.add(event); break;
            case Daily:
                this.dailyEvents.add(event); break;
            case Weekly:
                this.weeklyEvents.add(event); break;
            case Monthly:
                this.monthlyEvents.add(event); break;
            case Yearly:
                this.yearlyEvents.add(event); break;
        }
        save();
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
}