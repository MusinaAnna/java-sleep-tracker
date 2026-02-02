package ru.yandex.practicum.sleeptracker.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class SleepingSession {

    private final LocalDateTime sleepStart;
    private final LocalDateTime sleepEnd;
    private final SleepQuality quality;

    private static final LocalTime OWL_SLEEP_START = LocalTime.of(23, 0);    // после 23:00
    private static final LocalTime OWL_WAKE_START = LocalTime.of(9, 0);      // после 9:00
    private static final LocalTime LARK_SLEEP_END = LocalTime.of(22, 0);     // до 22:00
    private static final LocalTime LARK_WAKE_END = LocalTime.of(7, 0);

    public SleepingSession(LocalDateTime sleepStart, LocalDateTime sleepEnd, SleepQuality quality) {
        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.quality = quality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getSleepEnd() {
        return sleepEnd;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationInMinutes() {
        return Duration.between(sleepStart, sleepEnd).toMinutes();
    }

    public boolean isBadQuality() {
        return quality == SleepQuality.BAD;
    }

    public static SleepingSession parseFromString(String line) {
        String[] parts = line.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Строка должна содержать 3 части, разделенные ';'");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), formatter);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), formatter);
        SleepQuality quality = SleepQuality.valueOf(parts[2].trim().toUpperCase());

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Время начала сна не может быть позже времени окончания");
        }

        return new SleepingSession(start, end, quality);
    }

    public boolean isNightSession() {
        int startHour = sleepStart.getHour();
        int endHour = sleepEnd.getHour();

        boolean startsLate = startHour >= 21;
        boolean startsEarly = startHour <= 9;
        boolean crossesMidnight = !sleepStart.toLocalDate().equals(sleepEnd.toLocalDate());

        return startsLate || startsEarly || crossesMidnight;
    }

    public Chronotype getSessionChronotype() {
        if (!isNightSession()) {
            return null;
        }

        LocalTime sleepTime = sleepStart.toLocalTime();
        LocalTime wakeTime = sleepEnd.toLocalTime();

        boolean isOwl = sleepTime.isAfter(OWL_SLEEP_START) &&
                wakeTime.isAfter(OWL_WAKE_START);
        boolean isLark = sleepTime.isBefore(LARK_SLEEP_END) &&
                wakeTime.isBefore(LARK_WAKE_END);

        if (isOwl) {
            return Chronotype.OWL;
        } else if (isLark) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }
}





