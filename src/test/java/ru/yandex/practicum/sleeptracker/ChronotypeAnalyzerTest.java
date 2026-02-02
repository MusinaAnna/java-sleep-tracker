package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.SleepQuality;
import ru.yandex.practicum.sleeptracker.model.function.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChronotypeAnalyzerTest {

    @Test
    void testApplyWithEmptyList() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();
        List<SleepingSession> emptyList = new ArrayList<>();

        SleepAnalysisResult result = analyzer.apply(emptyList);

        assertEquals("Хронотип пользователя", result.getFunctionTitle());
        assertEquals("Нет данных", result.getResult());
    }

    @Test
    void testOnlyDaytimeSessions() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        // Только дневные сессии (игнорируются)
        List<SleepingSession> sessions = List.of(
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 10, 0),
                        LocalDateTime.of(2025, 10, 1, 11, 0),
                        SleepQuality.GOOD
                ),
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 14, 0),
                        LocalDateTime.of(2025, 10, 2, 15, 30),
                        SleepQuality.NORMAL
                )
        );

        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("Хронотип пользователя", result.getFunctionTitle());
        assertEquals("Хронотип не определен", result.getResult());
    }

    @Test
    void testOwlDominant() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        // Больше совиных ночей
        List<SleepingSession> sessions = List.of(
                // Сова: засыпание 23:30, пробуждение 9:30
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD
                ),
                // Сова: засыпание 23:45, пробуждение 10:00
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 45),
                        LocalDateTime.of(2025, 10, 3, 10, 0),
                        SleepQuality.NORMAL
                ),
                // Голубь: засыпание 22:30, пробуждение 7:30
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 22, 30),
                        LocalDateTime.of(2025, 10, 4, 7, 30),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("Хронотип пользователя", result.getFunctionTitle());
        // Просто проверяем строку "Сова"
        assertEquals("Сова", result.getResult());
    }

    @Test
    void testLarkDominant() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        // Больше жаворонковых ночей
        List<SleepingSession> sessions = List.of(
                // Жаворонок: засыпание 21:00, пробуждение 6:00
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 21, 0),
                        LocalDateTime.of(2025, 10, 2, 6, 0),
                        SleepQuality.GOOD
                ),
                // Жаворонок: засыпание 20:30, пробуждение 5:30
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 20, 30),
                        LocalDateTime.of(2025, 10, 3, 5, 30),
                        SleepQuality.NORMAL
                ),
                // Голубь: засыпание 23:00, пробуждение 8:00
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 23, 0),
                        LocalDateTime.of(2025, 10, 4, 8, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("Хронотип пользователя", result.getFunctionTitle());
        // Просто проверяем строку "Жаворонок"
        assertEquals("Жаворонок", result.getResult());
    }

    @Test
    void testPigeonDominant() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        // Больше голубиных ночей
        List<SleepingSession> sessions = List.of(
                // Голубь: засыпание 22:30, пробуждение 7:30
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 30),
                        LocalDateTime.of(2025, 10, 2, 7, 30),
                        SleepQuality.GOOD
                ),
                // Голубь: засыпание 23:15, пробуждение 8:15
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 15),
                        LocalDateTime.of(2025, 10, 3, 8, 15),
                        SleepQuality.NORMAL
                ),
                // Сова: засыпание 00:30, пробуждение 10:00
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 0, 30),
                        LocalDateTime.of(2025, 10, 3, 10, 0),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("Хронотип пользователя", result.getFunctionTitle());
        // Просто проверяем строку "Голубь"
        assertEquals("Голубь", result.getResult());
    }

    @Test
    void testTieBreaker() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        // Равное количество - должен выбрать голубя
        List<SleepingSession> sessions = List.of(
                // Сова: засыпание 23:30, пробуждение 9:30
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 23, 30),
                        LocalDateTime.of(2025, 10, 2, 9, 30),
                        SleepQuality.GOOD
                ),
                // Жаворонок: засыпание 21:00, пробуждение 6:00
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 21, 0),
                        LocalDateTime.of(2025, 10, 3, 6, 0),
                        SleepQuality.NORMAL
                )
                // Нет голубиных ночей, но при равенстве выбираем голубя
        );

        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("Хронотип пользователя", result.getFunctionTitle());
        // При равенстве выбираем голубя
        assertEquals("Голубь", result.getResult());
    }

    @Test
    void testEdgeCases() {
        ChronotypeAnalyzer analyzer = new ChronotypeAnalyzer();

        // Граничные случаи по времени
        List<SleepingSession> sessions = List.of(
                // Ровно 22:00 и 7:00 - это НЕ жаворонок (строго до 22:00 и до 7:00)
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 1, 22, 0),
                        LocalDateTime.of(2025, 10, 2, 7, 0),
                        SleepQuality.GOOD
                ),
                // Ровно 23:00 и 9:00 - это НЕ сова (строго после 23:00 и после 9:00)
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 2, 23, 0),
                        LocalDateTime.of(2025, 10, 3, 9, 0),
                        SleepQuality.NORMAL
                ),
                // Жаворонок: 21:59 и 6:59
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 3, 21, 59),
                        LocalDateTime.of(2025, 10, 4, 6, 59),
                        SleepQuality.GOOD
                ),
                // Сова: 23:01 и 9:01
                new SleepingSession(
                        LocalDateTime.of(2025, 10, 4, 23, 1),
                        LocalDateTime.of(2025, 10, 5, 9, 1),
                        SleepQuality.GOOD
                )
        );

        SleepAnalysisResult result = analyzer.apply(sessions);

        assertEquals("Хронотип пользователя", result.getFunctionTitle());
        // В этом примере: 1 голубь, 1 голубь, 1 жаворонок, 1 сова
        // При равенстве (1:1:2) выбираем голубя
        assertEquals("Голубь", result.getResult());
    }
}