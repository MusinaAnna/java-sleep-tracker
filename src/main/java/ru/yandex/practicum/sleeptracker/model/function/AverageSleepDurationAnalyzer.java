package ru.yandex.practicum.sleeptracker.model.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class AverageSleepDurationAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Средняя продолжительность сессии сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, "Нет данных");
        }

        double averageDuration = sessions.stream()
                .mapToLong(SleepingSession::getDurationInMinutes)
                .average()
                .orElse(0.0);

        return new SleepAnalysisResult(TITLE, String.format("%.1f минут", averageDuration).replace(",", "."));
    }
}