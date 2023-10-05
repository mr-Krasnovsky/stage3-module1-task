package com.mjc.school.repository.implementation;

import com.mjc.school.repository.model.NewsModel;

import java.io.IOException;
import java.util.List;

public interface News {
    List<NewsModel> getAllNews() throws IOException;

    NewsModel readById(Long id);

    NewsModel createNews(NewsModel newsModel);

    Boolean deleteNewsById(Long id);

    NewsModel updateNews(NewsModel existingNewsModel);
}
