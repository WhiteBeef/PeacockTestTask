package ru.whitebeef;

import ru.whitebeef.entity.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class SolutionWithoutClassGroup {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        BufferedReader reader = null;
        FileReader fileReader = null;
        InputStreamReader inputStreamReader = null;
        GZIPInputStream gzipInputStream = null;

        try {
            if (args.length < 1) {
                System.out.println("Вы не указали файл!");
                return;
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
                return;
            }

            List<String> lines = new ArrayList<>();

            Map<Pair<String, Integer>, Integer> groups = new HashMap<>();

            Map<Integer, Set<Integer>> lineIndexes = new HashMap<>();

            Map<Integer, Integer> parents = new HashMap<>();
            int groupIndex = 0;

            String line;
            int lineIndex = 0;

            while ((line = reader.readLine()) != null) {
                if (!line.matches("^(\"[^\"^;]*\";)*(\"[^\"^;]*\");?")) {
                    continue;
                }
                lines.add(line);
                String[] stringArr = line.split(";");
                int group = -1;

                for (int i = 0; i < stringArr.length; i++) {
                    String value = stringArr[i].substring(1, stringArr[i].length() - 1);
                    if (value.isEmpty()) {
                        continue;
                    }
                    Pair<String, Integer> pair = new Pair<>(value, i);

                    if (!groups.containsKey(pair)) {
                        if (group == -1) {
                            group = groupIndex++;
                            groups.put(pair, group);
                            lineIndexes.computeIfAbsent(group, k -> new HashSet<>()).add(lineIndex);
                        } else {
                            groups.put(pair, group);
                            lineIndexes.computeIfAbsent(group, k -> new HashSet<>()).add(lineIndex);

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
                                tempGroup = parents.get(tempGroup);
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

                lineIndex++;
            }
            try (FileWriter writer = new FileWriter("output.txt")) {
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
            } catch (IOException e) {
                System.out.println("Произошла ошибка при записи в файл: " + e.getMessage());
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
        System.out.println("Время выполнения: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}
