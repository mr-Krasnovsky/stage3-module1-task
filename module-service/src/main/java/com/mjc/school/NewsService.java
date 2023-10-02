package com.mjc.school;

import com.mjc.school.CustomExceptions.InputValidationException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NewsService {
    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final NewsMapper newsMapper = NewsMapper.INSTANSE;

    public NewsService(NewsRepository newsRepository, AuthorRepository authorRepository) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
    }

    public List<NewsDTO> getAllNews() {
        List<News> newsList = null;
        try {
            newsList = newsRepository.getAllNews();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        List<NewsDTO> newsDTOList = new ArrayList<>();
        for (News news : newsList) {
            NewsDTO newsDTO = newsMapper.newsToNewsDTO(news);
            newsDTOList.add(newsDTO);
        }
        return newsDTOList;
    }

    public NewsDTO getNewsById(Long newsId) throws InputValidationException {
        News news = newsRepository.getNewsById(newsId);
        if (news != null) {
            return newsMapper.newsToNewsDTO(news);
        } else
            throw new InputValidationException("ERROR_CODE: 000001 ERROR_MESSAGE: News with id " + newsId + " does not exist.");
    }

    public Long createNews(NewsDTO newsDTO) {
        try {
            checkNews(newsDTO);
            Long generatedId = generateId();
            LocalDateTime createDate = LocalDateTime.now();
            newsDTO.setId(generatedId);
            newsDTO.setCreateDate(createDate);
            newsDTO.setLastUpdateDate(createDate);

            News news = newsMapper.newsDTOToNews(newsDTO);
            newsRepository.saveNews(news);
            return generatedId;
        } catch (InputValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Long generateId() {

        List<News> news = null;
        try {
            news = newsRepository.getAllNews();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (news.isEmpty()) {
            return 1L;
        }
        Long maxId = news.stream().mapToLong(News::getId).max().orElse(0L);
        return maxId + 1;
    }

    public boolean checkNews(NewsDTO newsDTO) throws InputValidationException {
        String newsTitle = newsDTO.getTitle();
        String newsContent = newsDTO.getContent();
        Long authorId = newsDTO.getAuthorId();
        if (newsTitle.length() < 5 || newsTitle.length() > 30) {
            throw new InputValidationException("ERROR_CODE: 000012 ERROR_MESSAGE: News title can not be less than 5 and more than 30 symbols. News title is: " + newsTitle);
        } else if (newsContent.length() < 5 || newsContent.length() > 255) {
            throw new InputValidationException("ERROR_CODE: 000012 ERROR_MESSAGE: News content can not be less than 5 and more than 255 symbols. News title is: " + newsContent);
        } else if (authorRepository.getAuthorByID(authorId) == null) {
            throw new InputValidationException("ERROR_CODE: 000012 ERROR_MESSAGE: Author Id does not exist. Author Id is: " + authorId);
        }
        return true;
    }

}
