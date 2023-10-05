package com.mjc.school;

import com.mjc.school.repository.implementation.Author;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.implementation.News;
import com.mjc.school.сustomExceptions.InputValidationException;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class NewsService {
    private final News newsRepository;
    private final Author authorRepository;
    private final NewsMapper newsMapper = NewsMapper.INSTANSE;

    public NewsService(News newsRepository,
                       Author authorRepository) {
        this.newsRepository = newsRepository;
        this.authorRepository = authorRepository;
    }

    public List<NewsDTO> getAllNews() {
        List<NewsModel> newsModelList = null;
        try {
            newsModelList = newsRepository.getAllNews();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        List<NewsDTO> newsDTOList = new ArrayList<>();
        for (NewsModel newsModel : newsModelList) {
            NewsDTO newsDTO = newsMapper.newsToNewsDTO(newsModel);
            newsDTOList.add(newsDTO);
        }
        return newsDTOList;
    }

    public NewsDTO getNewsById(Long newsId) throws InputValidationException {
        NewsModel newsModel = newsRepository.readById(newsId);
        if (newsModel != null) {
            return newsMapper.newsToNewsDTO(newsModel);
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

            NewsModel newsModel = newsMapper.newsDTOToNews(newsDTO);
            return newsRepository.createNews(newsModel).getId();
        } catch (InputValidationException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private Long generateId() {

        List<NewsModel> newsModels = null;
        try {
            newsModels = newsRepository.getAllNews();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (newsModels.isEmpty()) {
            return 1L;
        }
        long maxId = newsModels.stream().mapToLong(NewsModel::getId).max().orElse(0L);
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

        NewsModel removeNewsModel = newsMapper.newsDTOToNews(news);
        List<NewsModel> allNews = newsRepository.getAllNews();
        Boolean removed = false;

        for (NewsModel existingNewsModel : allNews) {
            if (existingNewsModel.getId().equals(removeNewsModel.getId())) {
                removed = newsRepository.deleteNewsById(removeNewsModel.getId());
                break;
            }
        }
        if (!removed) {
            throw new InputValidationException(Constants.ERROR_CODE_PREFIX +
                    Constants.ERROR_NON_EXISTENT_NEWS +
                    Constants.ERROR_MESSAGE_PREFIX + "News with "
                    + removeNewsModel.getId() + " does not exist.");
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
        NewsModel existingNewsModel = newsRepository.readById(newsId);

        if (existingNewsModel != null) {
            existingNewsModel.setTitle(news.getTitle());
            existingNewsModel.setContent(news.getContent());
            existingNewsModel.setAuthorId(news.getAuthorId());
            existingNewsModel.setLastUpdateDate(LocalDateTime.now());
            return newsRepository.updateNews(existingNewsModel).getId();
        } else {
            throw new InputValidationException(
                    Constants.ERROR_CODE_PREFIX +
                            Constants.ERROR_NON_EXISTENT_NEWS +
                            Constants.ERROR_MESSAGE_PREFIX +
                            "News with id: " + newsId + " does not exist.");
        }
    }
}
