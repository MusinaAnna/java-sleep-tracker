package ru.yandex.practicum.sleeptracker.model.function;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SleeplessNightsCounterTest {

    @Test
    void testApplyWithEmptyList() {
        SleeplessNightsCounter counter = new SleeplessNightsCounter();
        List<SleepingSession> emptyList = new ArrayList<>();

        SleepAnalysisResult result = counter.apply(emptyList);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals("Нет данных", result.getResult());
    }

    @Test
    void testAllNightsHaveSleep() {
        SleeplessNightsCounter counter = new SleeplessNightsCounter();

        List<SleepingSession> sessions = List.of(
                // Ночь с 1 на 2 октября: сон 23:00-07:00
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0),
                        SleepQuality.GOOD
                ),
                // Ночь со 2 на 3 октября: сон 01:00-05:00
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 1, 0),
                        LocalDateTime.of(2025, 10, 2, 5, 0),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals(0L, result.getResult()); // Используем 0L (Long)
    }

    @Test
    void testOneSleeplessNight() {
        // Одна бессонная ночь
        SleeplessNightsCounter counter = new SleeplessNightsCounter();

        List<SleepingSession> sessions = List.of(
                // Ночь с 1 на 2 октября: есть сон
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                // Ночь со 2 на 3 октября: нет сна (только дневной сон)
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 14, 0),
                        LocalDateTime.of(2025, 10, 2, 15, 0),
                        SleepQuality.NORMAL
                ),
                // Ночь с 3 на 4 октября: есть сон
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 23, 30),
                        LocalDateTime.of(2025, 10, 4, 7, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals(1L, result.getResult()); // Используем 1L (Long)
    }

    @Test
    void testFirstSessionAfterNoon() {
        // Первая сессия началась после 12:00
        SleeplessNightsCounter counter = new SleeplessNightsCounter();

        List<SleepingSession> sessions = List.of(
                // Сессия началась после 12:00
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 14, 0),
                        LocalDateTime.of(2025, 10, 1, 15, 0),
                        SleepQuality.NORMAL
                ),
                // Ночь с 1 на 2 октября: есть сон
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals(0L, result.getResult()); // Используем 0L (Long)
    }

    @Test
    void testSessionEndsAtMidnight() {
        // Сессия заканчивается ровно в полночь
        SleeplessNightsCounter counter = new SleeplessNightsCounter();

        List<SleepingSession> sessions = List.of(
                // Сессия с 23:00 до 00:00 (не пересекает ночь, так как заканчивается в начале)
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 0),
                        LocalDateTime.of(2025, 10, 2, 0, 0),
                        SleepQuality.NORMAL
                ),
                // Вторая сессия с 06:00 до 07:00 (не пересекает ночь, так как начинается в конце)
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals(1L, result.getResult()); // Используем 1L (Long)
    }

    @Test
    void testCrossMonthBoundary() {
        // Тест на переход между месяцами
        SleeplessNightsCounter counter = new SleeplessNightsCounter();

        List<SleepingSession> sessions = List.of(
                // Сон в конце октября
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 30, 22, 0),
                        LocalDateTime.of(2025, 10, 31, 6, 0),
                        SleepQuality.GOOD
                ),
                // Бессонная ночь с 31 октября на 1 ноября
                // Сон только днем 1 ноября
                new SleepingSession(
                        LocalDateTime.of(2025, 11, 1, 14, 0),
                        LocalDateTime.of(2025, 11, 1, 15, 0),
                        SleepQuality.NORMAL
                ),
                // Сон в начале ноября
                new SleepingSession(
                        LocalDateTime.of(2025, 11, 1, 23, 0),
                        LocalDateTime.of(2025, 11, 2, 7, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = counter.apply(sessions);

        assertEquals("Количество бессонных ночей", result.getFunctionTitle());
        assertEquals(1L, result.getResult()); // Используем 1L (Long)
    }
}