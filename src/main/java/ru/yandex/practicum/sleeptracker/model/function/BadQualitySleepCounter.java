package ru.yandex.practicum.sleeptracker.model.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class BadQualitySleepCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Количество сессий с плохим качеством сна";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        long badQualityCount = sessions.stream()
                .filter(SleepingSession::isBadQuality)
                .count();

        return new SleepAnalysisResult(TITLE, badQualityCount);
    }
}