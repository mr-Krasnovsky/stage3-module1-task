package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.News;

import java.io.IOException;
import java.util.List;

public interface NewsRepository {
    List<News> getAllNews() throws IOException;

    News getNewsById(Long id);

    void createNews(News news);

    boolean deleteNewsById(News removeNews);

    Long updateNews(News existingNews);
}
