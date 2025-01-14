package com.mjc.school.repository.implementation;

import java.io.IOException;
import java.util.List;

public interface DataSource {
    List<String> readAllLines() throws IOException;
    public List<String> readAllAutors() throws IOException;
    void writeLines(List<String> lines) throws IOException;
    void createLines(String newsString) throws IOException;

    void writeNewsToFile (List<String> newsStrings) throws IOException;

    void writeAfterDelete(List<String> lines) throws IOException;
}

