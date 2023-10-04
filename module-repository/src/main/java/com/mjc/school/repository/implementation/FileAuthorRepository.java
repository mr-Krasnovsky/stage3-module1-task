package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.Author;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileAuthorRepository implements AuthorRepository {
    private static final String authorsFile = "module-repository/src/main/resources/author.txt";


    @Override
    public Author getAuthorByID(Long id) {
        List<Author> allAuthors = new ArrayList<>();
        try {
            List<String> authors = Files.readAllLines(Path.of(authorsFile));
            allAuthors = createAuthors(authors);
            for (Author author : allAuthors) {
                if (author.getId() == id) {
                    return author;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Author> createAuthors(List<String> authors) {
        List<Author> allAuthors = new ArrayList<>();
        for (String str : authors) {
            String[] author = str.split(";");
            if (author.length == 2) {
                allAuthors.add(new Author(Long.parseLong(author[0]), author[1]));
            }
        }
        return allAuthors;
    }


}
