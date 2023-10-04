package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.Author;

public interface AuthorRepository {
    Author getAuthorByID(Long id);
}
