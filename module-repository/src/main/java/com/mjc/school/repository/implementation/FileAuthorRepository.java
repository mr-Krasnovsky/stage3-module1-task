package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.AuthorModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAuthorRepository implements AuthorRepository {
    private final DataSource dataSource;

    public FileAuthorRepository(DataSource dataSource) {

        this.dataSource = dataSource;
    }


    @Override
    public AuthorModel getAuthorByID(Long id) {
        List<AuthorModel> allAuthors = new ArrayList<>();
        try {
            List<String> authors = dataSource.readAllAutors();
            allAuthors = createAuthors(authors);
            for (AuthorModel author : allAuthors) {
                if (author.getId() == id) {
                    return author;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<AuthorModel> createAuthors(List<String> authors) {
        List<AuthorModel> allAuthors = new ArrayList<>();
        for (String str : authors) {
            String[] author = str.split(";");
            if (author.length == 2) {
                allAuthors.add(new AuthorModel(Long.parseLong(author[0]), author[1]));
            }
        }
        return allAuthors;
    }


}
