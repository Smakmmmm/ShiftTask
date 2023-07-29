package ru.makhmedov.merge_sorting.file_handler.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class OutputFileWriter implements Writer {
    private final BufferedWriter bufferedWriter;

    public OutputFileWriter(File outputFile) throws IOException {
        FileWriter fileWriter = new FileWriter(outputFile);
        bufferedWriter = new BufferedWriter(fileWriter);
    }

    @Override
    public void write(String string) {
        try {
            bufferedWriter.write(string);
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл. Текст ошибки: " + e.getMessage() + ". Незаписанная строка: " + string);
        }
    }

    @Override
    public void moveToNewLine() {
        try {
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл (новая строка). Текст ошибки: " + e.getMessage());
        }
    }
}
