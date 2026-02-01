package ru.yandex.practicum.sleeptracker.model.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class MaxSleepDurationAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Максимальная продолжительность сессии сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, "Нет данных");
        }

        long maxDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .max()
                .orElse(0);

        return new SleepAnalysisResult(TITLE, maxDuration + " минут");
    }
}