package ru.whitebeef;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class SolutionWithoutClassGroup {
    private static final List<String> lines = new ArrayList<>();
    private static final Map<String, Integer> groups = new HashMap<>();
    private static final Map<Integer, Set<Integer>> lineIndexes = new HashMap<>();
    private static final Map<Integer, Integer> parents = new HashMap<>();

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        long startTime = System.currentTimeMillis();

        if (!readFile(args)) {
            return;
        }
        System.gc();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("(Файл прочитан) Потребление памяти: " + usedMemory / (1024.0 * 1024.0));
        int groupIndex = 0;
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            String[] stringArr = lines.get(lineIndex).split(";");
            int group = -1;
            if (lineIndex % 100000 == 0) {
                System.out.println(lineIndex);
            }
            for (int i = 0; i < stringArr.length; i++) {
                String value = stringArr[i];
                if (value.length() == 2) {
                    continue;
                }

                String pair = value + i;

                if (!groups.containsKey(pair)) {
                    if (group == -1) {
                        group = groupIndex++;
                        groups.put(pair, group);
                        lineIndexes.computeIfAbsent(group, k -> new HashSet<>()).add(lineIndex);
                    } else {
                        groups.put(pair, group);
                    }
                } else {
                    if (group == -1) {
                        int tempGroup = groups.get(pair);
                        while (parents.get(tempGroup) != null) {
                            tempGroup = parents.get(tempGroup);
                        }
                        group = tempGroup;
                        groups.put(pair, group);
                        lineIndexes.computeIfAbsent(group, k -> new HashSet<>()).add(lineIndex);
                    } else {
                        int tempGroup = groups.get(pair);

                        while (parents.get(tempGroup) != null) {
                            int temp = tempGroup;
                            tempGroup = parents.get(tempGroup);
                            parents.put(temp, group);
                        }
                        if (group == tempGroup) {
                            continue;
                        }
                        parents.put(tempGroup, group);
                        lineIndexes.computeIfAbsent(group, k -> new HashSet<>()).addAll(lineIndexes.get(tempGroup));
                        lineIndexes.remove(tempGroup);
                    }
                }
            }
        }
        usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Потребление памяти: " + usedMemory / (1024.0 * 1024.0));
        writeToFile(args);

        usedMemory = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Потребление памяти: " + usedMemory / (1024.0 * 1024.0));
        System.out.println("Время выполнения: " + (System.currentTimeMillis() - startTime) + "ms");
    }

    private static boolean readFile(String[] args) {
        BufferedReader reader = null;
        FileReader fileReader = null;
        InputStreamReader inputStreamReader = null;
        GZIPInputStream gzipInputStream = null;
        try {
            if (args.length < 1) {
                System.out.println("Вы не указали файл!");
                return false;
            }

            Path filePath = Path.of(args[0]);
            if (args[0].endsWith(".txt") || args[0].endsWith(".csv")) {
                fileReader = new FileReader(filePath.toFile());
                reader = new BufferedReader(fileReader);
            } else if (args[0].endsWith(".gz")) {
                gzipInputStream = new GZIPInputStream(Files.newInputStream(filePath));
                inputStreamReader = new InputStreamReader(gzipInputStream);
                reader = new BufferedReader(inputStreamReader);
            } else {
                System.out.println("Неизвестное расширение файла!");
                return false;
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("^(\"[^\"^;]*\";)*(\"[^\"^;]*\");?")) {
                    continue;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Произошла неизвестная ошибка: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (fileReader != null) {
                    fileReader.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (gzipInputStream != null) {
                    gzipInputStream.close();
                }

            } catch (IOException e) {
                System.out.println("Произошла непредвиденная ошибка при закрытии файлов " + e.getMessage());
            }
        }
        return true;
    }

    private static void writeToFile(String[] args) {
        Path filePath = Path.of(args[0]);
        try (FileWriter writer = new FileWriter(filePath.getParent().toAbsolutePath() + "output.txt")) {
            List<Set<Integer>> output = new ArrayList<>();

            for (int group : lineIndexes.keySet()) {
                while (parents.get(group) != null) {
                    group = parents.get(group);
                }
                if (lineIndexes.containsKey(group)) {
                    if (lineIndexes.get(group).size() > 1) {
                        output.add(lineIndexes.get(group));
                    }
                }
            }


            int groupNumber = 1;
            writer.write("Количество групп: " + output.size() + "\n");
            System.out.println("Количество групп: " + output.size());
            output.sort((o1, o2) -> Integer.compare(o2.size(), o1.size()));
            for (Set<Integer> set : output) {
                writer.write("Группа " + groupNumber++ + "\n");
                for (int i : set) {
                    writer.write(lines.get(i) + "\n");
                }
            }
        } catch (
                IOException e) {
            System.out.println("Произошла ошибка при записи в файл: " + e.getMessage());
        }
    }
}
