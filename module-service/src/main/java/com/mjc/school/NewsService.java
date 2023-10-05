package com.mjc.school;

import com.mjc.school.repository.implementation.AuthorRepository;
import com.mjc.school.repository.model.News;
import com.mjc.school.repository.implementation.NewsRepository;
import com.mjc.school.сustomExceptions.InputValidationException;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class NewsService {
    private final NewsRepository newsRepository;
    private final AuthorRepository authorRepository;
    private final NewsMapper newsMapper = NewsMapper.INSTANSE;

    public NewsService(NewsRepository newsRepository,
                       AuthorRepository authorRepository) {
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
        News news = newsRepository.readById(newsId);
        if (news != null) {
            return newsMapper.newsToNewsDTO(news);
        } else {
            throw new InputValidationException(
                    Constants.ERROR_CODE_PREFIX +
                            Constants.ERROR_NON_EXISTENT_NEWS +
                            Constants.ERROR_MESSAGE_PREFIX +
                            "News with id: " +
                            newsId + " does not exist.");
        }
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
            return newsRepository.createNews(news).getId();
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
        long maxId = news.stream().mapToLong(News::getId).max().orElse(0L);
        return maxId + 1;
    }

    public boolean checkNews(NewsDTO newsDTO) throws InputValidationException {
        String newsTitle = newsDTO.getTitle();
        String newsContent = newsDTO.getContent();
        Long authorId = newsDTO.getAuthorId();
        if (newsTitle.length() < 5 || newsTitle.length() > 30) {
            throw new InputValidationException(
                    Constants.ERROR_CODE_PREFIX +
                            Constants.ERROR_INVALID_TITLE +
                            Constants.ERROR_MESSAGE_PREFIX
                            + "News title can not be less than 5 "
                            + "and more than 30 symbols. News title is: "
                            + newsTitle);
        } else if (newsContent.length() < 5 || newsContent.length() > 255) {
            throw new InputValidationException(
                    Constants.ERROR_CODE_PREFIX +
                            Constants.ERROR_INVALID_TITLE +
                            Constants.ERROR_MESSAGE_PREFIX
                            + "News content can not be less than 5 and more "
                            + "than 255 symbols. News title is: "
                            + newsContent);
        } else if (authorRepository.getAuthorByID(authorId) == null) {
            throw new InputValidationException(
                    Constants.ERROR_CODE_PREFIX +
                            Constants.ERROR_INVALID_TITLE +
                            Constants.ERROR_MESSAGE_PREFIX
                            + "Author Id does not exist. "
                            + "Author Id is: " + authorId);
        }
        return true;
    }

    public Boolean deleteNewsById(NewsDTO news) throws InputValidationException, IOException {
        if (news == null) {
            throw new InputValidationException(
                    Constants.ERROR_CODE_PREFIX +
                            Constants.ERROR_NON_EXISTENT_AUTHOR +
                            Constants.ERROR_MESSAGE_PREFIX +
                            "News object is null.");
        }

        News removeNews = newsMapper.newsDTOToNews(news);
        List<News> allNews = newsRepository.getAllNews();
        Boolean removed = false;

        for (News existingNews : allNews) {
            if (existingNews.getId().equals(removeNews.getId())) {
                removed = newsRepository.deleteNewsById(removeNews.getId());
                break;
            }
        }
        if (!removed) {
            throw new InputValidationException(Constants.ERROR_CODE_PREFIX +
                    Constants.ERROR_NON_EXISTENT_NEWS +
                    Constants.ERROR_MESSAGE_PREFIX + "News with "
                    + removeNews.getId() + " does not exist.");
        }
        return removed;
    }

    public Long updateNews(NewsDTO news) throws InputValidationException {
        try {
            checkNews(news);
        } catch (InputValidationException e) {
            System.out.println(e.getMessage());
            return null;
        }
        Long newsId = news.getId();
        News existingNews = newsRepository.readById(newsId);

        if (existingNews != null) {
            existingNews.setTitle(news.getTitle());
            existingNews.setContent(news.getContent());
            existingNews.setAuthorId(news.getAuthorId());
            existingNews.setLastUpdateDate(LocalDateTime.now());
            return newsRepository.updateNews(existingNews).getId();
        } else {
            throw new InputValidationException(
                    Constants.ERROR_CODE_PREFIX +
                            Constants.ERROR_NON_EXISTENT_NEWS +
                            Constants.ERROR_MESSAGE_PREFIX +
                            "News with id: " + newsId + " does not exist.");
        }
    }
}
