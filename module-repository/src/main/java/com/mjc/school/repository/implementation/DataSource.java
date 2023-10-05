package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.News;

import java.io.IOException;
import java.util.List;

public interface DataSource {
    List<String> readAllLines() throws IOException;
    void writeLines(List<String> lines) throws IOException;
    void createLines(String newsString) throws IOException;

    void writeNewsToFile (List<String> newsStrings) throws IOException;

    void writeAfterDelete(List<String> lines) throws IOException;
}

