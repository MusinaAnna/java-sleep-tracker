package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;
import ru.yandex.practicum.sleeptracker.model.function.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SleepTrackerApp {

    private final List<Function<List<SleepingSession>, SleepAnalysisResult>> analyticFunctions = List.of(
            new SleepingSessionCounter(),
            new MinSleepDurationAnalyzer(),
            new MaxSleepDurationAnalyzer(),
            new AverageSleepDurationAnalyzer(),
            new BadQualitySleepCounter(),
            new SleeplessNightsCounter(),
            new ChronotypeAnalyzer()
    );

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Ошибка: не указан путь к файлу с данными");
            System.out.println("Использование: java SleepTrackerApp <путь_к_файлу>");
            System.out.println("Пример: java SleepTrackerApp data/sleep_log.txt");
            return;
        }

        String filePath = args[0];
        SleepTrackerApp app = new SleepTrackerApp();

        try {
            List<SleepingSession> sessions = app.readFile(app.getFile(filePath));

            if (sessions.isEmpty()) {
                System.out.println("Файл не содержит корректных данных о сне");
                return;
            }


            List<SleepAnalysisResult> results = app.analyzeSessions(sessions);

            System.out.println("\nРезультаты анализа сна:");
            results.forEach(System.out::println);

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + filePath);
            System.out.println("Убедитесь, что путь указан правильно и файл существует");
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении программы: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<SleepAnalysisResult> analyzeSessions(List<SleepingSession> sessions) {
        return analyticFunctions.stream()
                .map(function -> function.apply(sessions))
                .toList();
    }

    private List<SleepingSession> readFile(File file) {
        List<SleepingSession> sessions = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(fileReader)) {
            sessions = reader.lines()
                    .map(this::parseLine)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            if (sessions.isEmpty()) {
                System.out.println("Прочитан пустой файл " + file.getName());
            }


        } catch (IOException exception) {
            System.out.println("Произошла ошибка при чтении из файла " + file.getName());
        }
        return sessions;
    }

    private Optional<SleepingSession> parseLine(String line) {
        try {
            return Optional.of(SleepingSession.parseFromString(line));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private File getFile(String filename) throws FileNotFoundException {
        Path filePath = Paths.get(filename);
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("Не существует файла с именем %s", filename));
        }
        return file;
    }


}