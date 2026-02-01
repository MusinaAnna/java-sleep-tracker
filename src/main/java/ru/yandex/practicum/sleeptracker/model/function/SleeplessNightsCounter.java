package ru.yandex.practicum.sleeptracker.model.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class SleeplessNightsCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, "Нет данных");
        }

        LocalDateTime firstSessionStart = sessions.stream()
                .map(SleepingSession::getSleepStart)
                .min(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDateTime lastSessionEnd = sessions.stream()
                .map(SleepingSession::getSleepEnd)
                .max(LocalDateTime::compareTo)
                .orElseThrow();

        LocalDate startDate = determineStartDate(firstSessionStart);

        LocalDate endDate = lastSessionEnd.toLocalDate();

        if (startDate.isAfter(endDate)) {
            return new SleepAnalysisResult(TITLE, 0);
        }

        long sleeplessNights = countSleeplessNights(sessions, startDate, endDate);

        return new SleepAnalysisResult(TITLE, sleeplessNights);
    }

    private LocalDate determineStartDate(LocalDateTime firstSessionStart) {
        if (firstSessionStart.getHour() >= 12) {
            return firstSessionStart.toLocalDate().plusDays(1);
        } else {
            return firstSessionStart.toLocalDate().minusDays(1);
        }
    }

    private long countSleeplessNights(List<SleepingSession> sessions, LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .filter(date -> isSleeplessNight(sessions, date))
                .count();
    }

    private boolean isSleeplessNight(List<SleepingSession> sessions, LocalDate date) {
        LocalDateTime nightStart = LocalDateTime.of(date, LocalTime.MIDNIGHT); // 00:00
        LocalDateTime nightEnd = LocalDateTime.of(date, LocalTime.of(6, 0));   // 06:00

        return sessions.stream()
                .noneMatch(session -> doesSessionIntersectNight(session, nightStart, nightEnd));
    }

    private boolean doesSessionIntersectNight(SleepingSession session, LocalDateTime nightStart,
                                              LocalDateTime nightEnd) {
        LocalDateTime sessionStart = session.getSleepStart();
        LocalDateTime sessionEnd = session.getSleepEnd();

        return sessionStart.isBefore(nightEnd) && sessionEnd.isAfter(nightStart);
    }
}