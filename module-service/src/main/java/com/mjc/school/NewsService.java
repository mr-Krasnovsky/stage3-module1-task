package com.mjc.school;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsService {
    private NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsDTO> getAllNews() throws IOException {
        List<News> newsList = newsRepository.getAllNews();
        List<NewsDTO> newsDTOList = new ArrayList<>();
        for (News news: newsList){
            NewsDTO newsDTO = new NewsDTO();
            newsDTO.setId(news.getId());
            newsDTO.setTitle(news.getTitle());
            newsDTO.setContent(news.getContent());
            newsDTO.setCreateDate(news.getCreateDate());
            newsDTO.setLastUpdateDate(news.getLastUpdateDate());
            newsDTO.setAuthorId(news.getAuthorId());

            newsDTOList.add(newsDTO);
        }
        return newsDTOList;
    }
}
