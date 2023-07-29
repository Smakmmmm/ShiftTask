package ru.makhmedov.merge_sorting.file_handler.reader;

public interface Reader {
    boolean isFileFinished();

    String readString();

    String getFileName();
}
