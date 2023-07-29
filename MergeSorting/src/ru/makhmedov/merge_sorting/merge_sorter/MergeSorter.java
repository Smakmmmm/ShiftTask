package ru.makhmedov.merge_sorting.merge_sorter;

import ru.makhmedov.merge_sorting.file_handler.reader.Reader;
import ru.makhmedov.merge_sorting.file_handler.writer.Writer;
import ru.makhmedov.merge_sorting.merge_sorter.comparators.Iterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeSorter {
    private final Iterator iterator;
    private final Map<Reader, String> currentStrings = new HashMap<>();
    private final Writer fileWriter;

    public MergeSorter(Iterator iterator) {
        this.iterator = iterator;
        fileWriter = iterator.getOutputFileWriter();
        List<Reader> readerList = new ArrayList<>(iterator.getInputFileList());

        for (Reader reader : readerList) {
            String valueString = iterator.readValue(reader);

            if (!valueString.isEmpty()) {
                currentStrings.put(reader, valueString);
            } else {
                System.out.println("Файл " + reader.getFileName() + " не содержит подходящих строк, поэтому не будет участвовать в сортировке.");
            }
        }
    }

    public void sort() {
        if (currentStrings.size() < 2) {
            System.out.println("Для сортировки слиянием нужно минимум два корректных файла. Сейчас их: " + currentStrings.size());
            return;
        }

        boolean needNewLine = false;
        String previousValue = "";
        boolean needCompareValues = false;
        boolean needWrite = true;

        while (!currentStrings.isEmpty()) {
            Map.Entry<Reader, String> sortedValue = iterator.compare(currentStrings);

            if (sortedValue.getValue() == null) {
                currentStrings.remove(sortedValue.getKey());

                if (!sortedValue.getKey().isFileFinished()) {
                    currentStrings.put(sortedValue.getKey(), iterator.readValue(sortedValue.getKey()));
                    continue;
                }

                continue;
            }

            if (!sortedValue.getKey().isFileFinished()) {
                currentStrings.put(sortedValue.getKey(), iterator.readValue(sortedValue.getKey()));
            } else {
                currentStrings.remove(sortedValue.getKey());
            }

            if (needCompareValues) {
                needWrite = isCorrectValue(previousValue, sortedValue.getValue(), iterator.isAscendingOrder(), iterator.isStringType());

                if (!needWrite) {
                    needNewLine = false;
                }
            }

            if (needNewLine) {
                fileWriter.moveToNewLine();
            }

            if (needWrite) {
                fileWriter.write(sortedValue.getValue());
            }

            previousValue = sortedValue.getValue();
            needCompareValues = true;
            needNewLine = true;
        }
    }

    private boolean isCorrectValue(String previousValue, String currentValue, boolean isAscendingOrder, boolean isStringType) {
        if (isStringType) {
            if (isAscendingOrder && currentValue.compareTo(previousValue) >= 0) {
                return true;
            }

            return !isAscendingOrder && currentValue.compareTo(previousValue) <= 0;
        }

        int currentIntValue = Integer.parseInt(currentValue);
        int previousIntValue = Integer.parseInt(previousValue);

        if (isAscendingOrder && (currentIntValue - previousIntValue) >= 0) {
            return true;
        }

        return !isAscendingOrder && (currentIntValue - previousIntValue) <= 0;
    }
}
