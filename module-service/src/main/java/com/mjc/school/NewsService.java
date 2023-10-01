package com.mjc.school;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper = NewsMapper.INSTANSE;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsDTO> getAllNews() throws IOException {
        List<News> newsList = newsRepository.getAllNews();
        List<NewsDTO> newsDTOList = new ArrayList<>();
        for (News news: newsList){
            NewsDTO newsDTO = newsMapper.newsToNewsDTO(news);
            newsDTOList.add(newsDTO);
        }
        return newsDTOList;
    }

    public NewsDTO getNewsById(int newsId) {
        News news = newsRepository.getNewsById(newsId);
        if (news != null) {
            return newsMapper.newsToNewsDTO(news);
        } else return null;
    }
}
