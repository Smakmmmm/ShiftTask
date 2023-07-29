package ru.makhmedov.merge_sorting.merge_sorter.program_argument_setter;

import ru.makhmedov.merge_sorting.file_handler.reader.InputFileReader;
import ru.makhmedov.merge_sorting.file_handler.reader.Reader;
import ru.makhmedov.merge_sorting.file_handler.writer.OutputFileWriter;
import ru.makhmedov.merge_sorting.file_handler.writer.Writer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProgramArgumentSetter {
    private int argIndex = 0;
    private boolean isAscendingOrder = false;
    private boolean isStringType = false;

    private Writer outputFileWriter;

    private final List<Reader> inputFilesList = new ArrayList<>();

    public boolean isAscendingOrder() {
        return isAscendingOrder;
    }

    public boolean isStringType() {
        return isStringType;
    }

    public Writer getOutputFileWriter() {
        return outputFileWriter;
    }

    public List<Reader> getInputFilesList() {
        return inputFilesList;
    }

    public void setPreferences(String[] args) throws IOException {
        if (args.length < 4) {
            throw new IllegalArgumentException("Неправильное количество аргументов. Минимум 4. Сейчас: " + args.length + ".");
        }

        setSortingOrder(args);

        setOutputFileWriter(args);

        setInputFilesList(args);
    }

    private void setSortingOrder(String[] args) {
        for (int i = 0; i < 2; i++) {
            if ("-a".equalsIgnoreCase(args[argIndex]) || "-d".equalsIgnoreCase(args[argIndex])) {
                if ("-a".equalsIgnoreCase(args[argIndex])) {
                    isAscendingOrder = true;
                }

                argIndex++;
            }

            if ("-s".equalsIgnoreCase(args[argIndex]) || "-i".equalsIgnoreCase(args[argIndex])) {
                if ("-s".equalsIgnoreCase(args[argIndex])) {
                    isStringType = true;
                }

                argIndex++;
            }
        }

        if (argIndex == 0) {
            throw new IllegalArgumentException("Неправильно введены параметры сортировки. (Порядок сортировки/Тип данных)");
        }
    }

    private void setOutputFileWriter(String[] args) throws IOException {
        File file = new File(args[argIndex]);

        checkFileExtension(file);

        outputFileWriter = new OutputFileWriter(file);


        argIndex++;
    }

    private void setInputFilesList(String[] args) {
        int filesCount = args.length - argIndex;

        int MIN_FILES_COUNT = 2;

        if (filesCount < MIN_FILES_COUNT) {
            throw new IllegalArgumentException("Минимальное количество файлов 2. Сейчас их:" + filesCount + ".");
        }

        for (int i = filesCount; i < args.length; i++) {
            File currentFile = new File(args[argIndex]);
            argIndex++;

            checkFileExtension(currentFile);
            checkFileExistence(currentFile);

            this.inputFilesList.add(new InputFileReader(currentFile));
        }
    }

    private void checkFileExtension(File file) {
        String fileName = file.getName();

        if (fileName.lastIndexOf('.') != -1 && fileName.lastIndexOf('.') != 0) {
            if (!fileName.substring(fileName.lastIndexOf('.') + 1).equals("txt")) {
                throw new IllegalArgumentException("Расширение файлов должно быть \"txt\"");
            }
        }
    }

    private void checkFileExistence(File file) {
        if (!file.exists()) {
            throw new IllegalArgumentException("Файл " + file.getName() + " не найден.");
        }
    }
}
