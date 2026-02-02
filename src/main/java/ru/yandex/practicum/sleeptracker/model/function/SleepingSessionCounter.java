package ru.yandex.practicum.sleeptracker.model.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

public class SleepingSessionCounter implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Количество сессий сна";


    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sleepingSessions) {
        Integer quantity = sleepingSessions.size();
        return new SleepAnalysisResult(TITLE, (long) sleepingSessions.size());
    }
}
