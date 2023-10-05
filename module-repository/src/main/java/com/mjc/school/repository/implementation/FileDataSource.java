package com.mjc.school.repository.implementation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileDataSource implements DataSource {
    private static final String newsFile = "module-repository/src/main/resources/content.txt";
    private static final String authorsFile = "module-repository/src/main/resources/author.txt";

    @Override
    public List<String> readAllLines() throws IOException {
        return Files.readAllLines(Path.of(newsFile));
    }

    @Override
    public List<String> readAllAutors() throws IOException {
        return Files.readAllLines(Path.of(authorsFile));
    }

    @Override
    public void writeLines(List<String> lines) throws IOException {
        Files.write(Path.of(newsFile), lines, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void createLines(String newsString) throws IOException {
        Files.write(Path.of(newsFile), newsString.getBytes(), StandardOpenOption.APPEND);
    }

    @Override
    public void writeNewsToFile(List<String> newsStrings) throws IOException {
        Files.write(Path.of(newsFile), String.join("\n", newsStrings).getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public void writeAfterDelete(List<String> lines) throws IOException {
        Files.write(Path.of(newsFile), String.join("\n", lines).getBytes());
    }
}
