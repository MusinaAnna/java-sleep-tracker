package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.function.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleepingSessionCounterTest {

    @Test
    void testApplyWithEmptyList() {
        SleepingSessionCounter counter = new SleepingSessionCounter();
        List<SleepingSession> emptyList = new ArrayList<>();

        SleepAnalysisResult result = counter.apply(emptyList);

        assertEquals("Количество сессий сна", result.getFunctionTitle());
        assertEquals(0L, result.getResult());
    }

    @Test
    void testApplyWithThreeSessions() {
        SleepingSessionCounter counter = new SleepingSessionCounter();

        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 15),
                        LocalDateTime.of(2025, 10, 2, 8, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 8, 0),
                        SleepQuality.NORMAL
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 14, 30),
                        LocalDateTime.of(2025, 10, 3, 15, 20),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Количество сессий сна", result.getFunctionTitle());
        assertEquals(3L, result.getResult());
    }
}