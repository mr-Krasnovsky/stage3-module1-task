package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.News;

import java.io.IOException;
import java.util.List;

public interface NewsRepository {
    List<News> getAllNews() throws IOException;

    News readById(Long id);

    News createNews(News news);

    Boolean deleteNewsById(News removeNews);

    News updateNews(News existingNews);
}
