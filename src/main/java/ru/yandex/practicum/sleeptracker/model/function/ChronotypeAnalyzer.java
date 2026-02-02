package ru.yandex.practicum.sleeptracker.model.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.Chronotype;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChronotypeAnalyzer implements Function<List<SleepingSession>, SleepAnalysisResult> {
    public static final String TITLE = "Хронотип пользователя";

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(TITLE, "Нет данных");
        }

        List<Chronotype> nightSessionChronotypes = sessions.stream()
                .filter(SleepingSession::isNightSession)
                .map(SleepingSession::getSessionChronotype)
                .filter(chr -> chr != null)
                .collect(Collectors.toList());

        if (nightSessionChronotypes.isEmpty()) {
            return new SleepAnalysisResult(TITLE, "Хронотип не определен");
        }

        Map<Chronotype, Long> chronotypeCounts = nightSessionChronotypes.stream()
                .collect(Collectors.groupingBy(
                        chronotype -> chronotype,
                        Collectors.counting()
                ));

        Chronotype dominantChronotype = determineDominantChronotype(chronotypeCounts);

        String result = formatResult(dominantChronotype);

        return new SleepAnalysisResult(TITLE, result);
    }

    private Chronotype determineDominantChronotype(Map<Chronotype, Long> counts) {
        long owlCount = counts.getOrDefault(Chronotype.OWL, 0L);
        long larkCount = counts.getOrDefault(Chronotype.LARK, 0L);
        long pigeonCount = counts.getOrDefault(Chronotype.PIGEON, 0L);

        if (owlCount > larkCount && owlCount > pigeonCount) {
            return Chronotype.OWL;
        } else if (larkCount > owlCount && larkCount > pigeonCount) {
            return Chronotype.LARK;
        } else {
            return Chronotype.PIGEON;
        }
    }

    private String formatResult(Chronotype dominant) {
        switch (dominant) {
            case OWL:
                return "Сова";
            case LARK:
                return "Жаворонок";
            case PIGEON:
                return "Голубь";
            default:
                return "Не определен";
        }
    }
}