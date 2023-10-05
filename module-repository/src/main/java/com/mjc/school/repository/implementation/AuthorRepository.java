package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.AuthorModel;

public interface AuthorRepository {
    AuthorModel getAuthorByID(Long id);
}
