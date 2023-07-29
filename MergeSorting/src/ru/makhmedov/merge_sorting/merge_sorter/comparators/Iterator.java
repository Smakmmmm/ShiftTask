package ru.makhmedov.merge_sorting.merge_sorter.comparators;

import ru.makhmedov.merge_sorting.file_handler.reader.Reader;
import ru.makhmedov.merge_sorting.file_handler.writer.Writer;
import ru.makhmedov.merge_sorting.merge_sorter.program_argument_setter.ProgramArgumentSetter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Iterator {
    private final boolean isAscendingOrder;
    private final boolean isStringType;
    private Map.Entry<Reader, String> comparedValue;
    private final Function<Map<Reader, String>, Map.Entry<Reader, String>> sorterFunction;
    private final Function<Reader, String> readerStringFunction = Reader::readString;
    private final List<Reader> inputFileList;
    private final Writer outputFileWriter;

    public Iterator(ProgramArgumentSetter programArgumentSetter) {
        isAscendingOrder = programArgumentSetter.isAscendingOrder();
        isStringType = programArgumentSetter.isStringType();
        inputFileList = programArgumentSetter.getInputFilesList();
        outputFileWriter = programArgumentSetter.getOutputFileWriter();

        if (isStringType) {
            if (isAscendingOrder) {
                sorterFunction = stringMap -> Collections.min(stringMap.entrySet(), Map.Entry.comparingByValue());
            } else {
                sorterFunction = stringMap -> Collections.max(stringMap.entrySet(), Map.Entry.comparingByValue());
            }
        } else {
            if (isAscendingOrder) {
                sorterFunction = stringMap -> {
                    Map.Entry<Reader, Integer> entry = Collections.min(getIntegerMap(stringMap).entrySet(),
                            Map.Entry.comparingByValue());

                    return new AbstractMap.SimpleEntry<>(entry.getKey(), String.valueOf(entry.getValue()));
                };
            } else {
                sorterFunction = stringMap -> {
                    Map.Entry<Reader, Integer> entry = Collections.max(getIntegerMap(stringMap).entrySet(),
                            Map.Entry.comparingByValue());

                    return new AbstractMap.SimpleEntry<>(entry.getKey(), String.valueOf(entry.getValue()));
                };
            }
        }
    }

    public boolean isAscendingOrder() {
        return isAscendingOrder;
    }

    public boolean isStringType() {
        return isStringType;
    }

    public List<Reader> getInputFileList() {
        return inputFileList;
    }

    public Writer getOutputFileWriter() {
        return outputFileWriter;
    }

    private Map<Reader, Integer> getIntegerMap(Map<Reader, String> stringMap) {
        Map<Reader, String> innerStringMap = new HashMap<>(stringMap);

        return innerStringMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            comparedValue = entry;

                            return Integer.parseInt(entry.getValue());
                        }
                ));
    }

    public Map.Entry<Reader, String> compare(Map<Reader, String> currentStringsMap) {
        try {
            return sorterFunction.apply(currentStringsMap);
        } catch (NumberFormatException e) {
            System.out.println("Эту строку нельзя перевести в целое число. Текст ошибки: " + e.getMessage());

            comparedValue.setValue(null);

            return comparedValue;
        }
    }

    public String readValue(Reader reader) {
        if (reader.isFileFinished()) {
            throw new NoSuchElementException("Файл закончился.");
        }

        String goodString;
        final String whitespace = " ";

        do {
            if (reader.isFileFinished()) {
                return "";
            }

            goodString = readerStringFunction.apply(reader);
        } while (goodString.isEmpty() || goodString.contains(whitespace));

        return goodString;
    }
}
