package karpovich;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    /*
     * С учётом времменных ограничений:
     * 1.Реализовать чтение строк из файла, передаваемого в аргументе #1, и запись отсортированных строк в файл, передаваемый в аргументе #2. (сделано)
     * 2.Реализовать сортировку строк по а)алфавиту, б)длине строки, в)слову заданному аргументом #3 (порядковый номер слова в строке) при помощи стандартных средств. (сделано)
     * 3.Реализовать подсчёт повторяемых строк, каждая строка должна быть дополнена количеством повторений. (сделано)
     * TODO 4.Рефакторинг кода и юнит-тесты.
     *  - Разбиение программы на компоненты, повышает тестируемость и упрощает поддержку. Позволяет покрыть код юнит-тестами.
     *  ...
     */

    public static void main(String[] args) {
        if (args.length != 3) {
            printHelp();
            System.exit(1);
        }

        String inputFileName = args[0];
        String outputFileName = args[1];
        String sortOption = args[2];

        var lines = readLines(inputFileName);

        lines = appendCount(lines);

        switch (sortOption) {
           case "alphabet" -> lines.sort(String::compareTo);
           case "length" -> lines.sort(Comparator.comparingInt(String::length));
           default -> {
               try {
                   var wordNumber = Integer.parseInt(sortOption);
                   lines.sort(Comparator.comparing(s -> s.split(" ")[wordNumber - 1]));
               } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                   System.out.println("Invalid sort option: " + sortOption);

                   printHelp();
                   System.exit(1);
               }
           }
       }

        writeLines(outputFileName, lines);
    }

    public static void printHelp() {
        System.out.println("Usage: java -jar <jar_name> <input_file> <output_file> <sort_option>");
        System.out.println("sort_option: alphabet | length | <word_number>");
        System.out.println("word_number starts from 1");
    }

    public static List<String> readLines(String inputFileName) {
        var lines = new ArrayList<String>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + inputFileName);
            System.exit(1);
        }

        return lines;
    }

    public static List<String> appendCount(List<String> lines) {
        var stringCounts = new HashMap<String, Integer>();
        lines.forEach(line -> stringCounts.put(line, stringCounts.getOrDefault(line, 0) + 1));

        return lines.stream()
                .map(line -> line + " " + stringCounts.get(line))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void writeLines(String outputFileName, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (var line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing file: " + outputFileName);
            System.exit(1);
        }
    }
}