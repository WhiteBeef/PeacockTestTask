package ru.whitebeef;

import ru.whitebeef.entity.Group;
import ru.whitebeef.entity.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class PeacockTestTask {

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
            if (args[0].endsWith(".txt")) {
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

            List<String> strings = new ArrayList<>(1000000);
            Map<Pair<Long, Integer>, Group> map = new HashMap<>(1000000);

            String line;
            int lineIndex = 0;
            while ((line = reader.readLine()) != null) {
                if (!line.matches("^(\"[0-9]*\";)*(\"[0-9]*\")")) {
                    System.out.println("Строка '" + line + "' является некорректной. Пропускаю..");
                    continue;
                }
                strings.add(line);
                String[] stringArr = line.split(";");
                Group group = null;

                for (int i = 0; i < stringArr.length; i++) {
                    String s = stringArr[i].substring(1, stringArr[i].length() - 1);
                    if (s.isEmpty()) {
                        continue;
                    }

                    Long value = Long.parseLong(s);
                    Pair<Long, Integer> pair = new Pair<>(value, i);

                    if (group == null) {
                        if (map.containsKey(pair)) {
                            group = map.get(pair);
                            group.addNumber(value);
                            group.addLineIndex(lineIndex);
                        } else {
                            group = new Group(value);
                            group.addLineIndex(lineIndex);
                            map.put(pair, group);
                        }
                    } else {
                        if (map.containsKey(pair)) {
                            group = group.unionGroup(map.get(pair));
                            group.addNumber(value);
                            group.addLineIndex(lineIndex);
                        } else {
                            group.addNumber(value);
                            group.addLineIndex(lineIndex);
                            map.put(pair, group);
                        }
                    }
                }
                lineIndex++;
            }
            try (FileWriter writer = new FileWriter("output.txt")) {
                Set<Group> outputGroups = new HashSet<>();
                for (var entry : map.entrySet()) {
                    Group group = entry.getValue();
                    Group parent = group;
                    while (!(group.getParent() != null || group.getParent() != group)) {
                        parent = group.getParent();
                        group = parent;
                    }

                    if (parent.getLineIndexes().size() > 1) {
                        outputGroups.add(parent);
                    }
                }

                int groupNumber = 1;
                writer.write("Количество групп: " + outputGroups.size() + "\n");
                for (Group group : outputGroups) {
                    writer.write("Группа " + groupNumber++ + "\n");
                    for (int i : group.getLineIndexes()) {
                        writer.write(strings.get(i) + "\n");
                    }
                }
            } catch (IOException e) {
                System.out.println("Произошла ошибка при записи в файл: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Произошла неизвестная ошибка: " + e.getMessage());
            e.printStackTrace();
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
                e.printStackTrace();
            }
        }
        System.out.println("Успех!");
        System.out.println("Время выполнения: " + (System.currentTimeMillis() - startTime) + "ms");
    }
}