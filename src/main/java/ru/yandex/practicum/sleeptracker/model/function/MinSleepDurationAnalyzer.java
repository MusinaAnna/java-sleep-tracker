package ru.yandex.practicum.sleeptracker.model.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class MinSleepDurationAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Минимальная продолжительность сессии сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, "Нет данных");
        }

        long minDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .min()
                .orElse(0);

        return new SleepAnalysisResult(TITLE, minDuration + " минут");
    }
}