package ru.makhmedov.merge_sorting.file_handler.reader;

import java.io.*;

public class InputFileReader implements Reader {
    private BufferedReader bufferedReader;
    private final String fileName;
    private String currentString;
    private boolean isFileFinished = false;

    public InputFileReader(File file) {
        fileName = file.getName();

        try {
            FileReader fileReader = new FileReader(file);
            this.bufferedReader = new BufferedReader(fileReader);

            currentString = bufferedReader.readLine();

            if (currentString == null) {
                isFileFinished = true;
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean isFileFinished() {
        return isFileFinished;
    }
                                                    //TODO СДЕЛАТЬ ПРОВЕРКУ НА ПРОБЕЛЫ ПОСЛЕ ЧТЕНИЯ. - СДЕЛАЛ.
    @Override
    public String readString() {
        if (currentString == null) {
            isFileFinished = true;
            return "";
        }

        String resultString = currentString;
        String nextString;

        try {
            nextString = bufferedReader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return "";
        }

        if (nextString == null) {
            isFileFinished = true;
        } else {
            currentString = nextString;
        }

        return resultString;
    }
}
