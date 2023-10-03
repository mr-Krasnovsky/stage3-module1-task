package com.mjc.school;

import java.io.IOException;
import java.util.List;

public interface NewsRepository {
    List<News> getAllNews() throws IOException;

    News getNewsById(Long id);

    void saveNews(News news);

    boolean removeNewsById(News removeNews);

    Long updateNews(News existingNews);
}
