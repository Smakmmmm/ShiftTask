package ru.makhmedov.merge_sorting.main;

import ru.makhmedov.merge_sorting.merge_sorter.MergeSorter;
import ru.makhmedov.merge_sorting.merge_sorter.comparators.Iterator;
import ru.makhmedov.merge_sorting.merge_sorter.program_argument_setter.ProgramArgumentSetter;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            ProgramArgumentSetter programArgumentSetter = new ProgramArgumentSetter();
            programArgumentSetter.setPreferences(args);

            Iterator iterator = new Iterator(programArgumentSetter);
            MergeSorter mergeSorter = new MergeSorter(iterator);
            mergeSorter.sort();
        } catch (IllegalArgumentException e) {
            System.out.println("Возникла ошибка: " + e.getMessage() + System.lineSeparator() + Arrays.toString(e.getStackTrace()));
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}
