package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.AuthorModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileAuthor implements Author {
    private final DataSource dataSource;

    public FileAuthor(DataSource dataSource) {

        this.dataSource = dataSource;
    }


    @Override
    public AuthorModel getAuthorByID(Long id) {
        List<AuthorModel> allAuthors = new ArrayList<>();
        try {
            List<String> authors = dataSource.readAllAutors();
            allAuthors = getAuthors(authors);
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

    private List<AuthorModel> getAuthors(List<String> authors) {
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
