package com.mjc.school;

import com.mjc.school.repository.model.AuthorModel;
import com.mjc.school.repository.implementation.Author;
import com.mjc.school.repository.model.NewsModel;
import com.mjc.school.repository.implementation.News;
import com.mjc.school.сustomExceptions.InputValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NewsModelServiceTest {

    @Mock
    private News newsRepository;

    @Mock
    private Author authorRepository;

    @Mock
    private NewsMapper newsMapper;

    @InjectMocks
    private NewsService newsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllNews() throws IOException {
        List<NewsModel> mockNewsListModel = new ArrayList<>();
        when(newsRepository.getAllNews()).thenReturn(mockNewsListModel);

        List<NewsDTO> result = newsService.getAllNews();

        assertEquals(mockNewsListModel.size(), result.size());
        verify(newsMapper, times(mockNewsListModel.size())).newsToNewsDTO(any(NewsModel.class));
    }

    @Test
    public void testGetNewsByNonExistentId() {
        Long nonExistentId = 999L;
        try {
            newsService.getNewsById(nonExistentId);
            fail("Expected InputValidationException, but no exception was thrown.");
        } catch (InputValidationException e) {
            assertEquals(Constants.ERROR_CODE_PREFIX + Constants.ERROR_NON_EXISTENT_NEWS +
                    Constants.ERROR_MESSAGE_PREFIX + "News with id: " + nonExistentId + " does not exist.", e.getMessage());
        }
    }

    @Test
    public void testCreateNews() throws InputValidationException {
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setTitle("Test Title");
        newsDTO.setContent("Test Content");
        newsDTO.setAuthorId(1L);

        NewsModel newsModel = new NewsModel(newsDTO.getId(), newsDTO.getTitle(), newsDTO.getContent(), LocalDateTime.now(), LocalDateTime.now(), newsDTO.getAuthorId());

        when(authorRepository.getAuthorByID(newsDTO.getAuthorId())).thenReturn(new AuthorModel(newsDTO.getAuthorId(), "Author Name"));
        when(newsMapper.newsDTOToNews(newsDTO)).thenReturn(newsModel);
        when(newsRepository.createNews(any(NewsModel.class))).thenAnswer(invocation -> {
            NewsModel newsModelArgument = invocation.getArgument(0);
            newsModelArgument.setId(1L);
            return newsModelArgument;
        });

        Long generatedId = newsService.createNews(newsDTO);

        verify(newsRepository, times(1)).createNews(any(NewsModel.class));
        assertEquals(generatedId, newsDTO.getId());
        assertEquals(newsDTO.getCreateDate(), newsDTO.getLastUpdateDate());
    }

    @Test
    public void testRemoveNewsById() throws IOException {
        Long newsId = 1L;
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(newsId);

        LocalDateTime now = LocalDateTime.now();
        when(newsMapper.newsDTOToNews(newsDTO)).thenReturn(new NewsModel(newsId, "Sample Title", "Sample Content", now, now, 1L));
        when(newsRepository.getAllNews()).thenReturn(new ArrayList<>());

        assertThrows(InputValidationException.class, () -> {
            newsService.deleteNewsById(newsDTO);
        });
    }
    @Test
    public void testUpdateNews() throws InputValidationException {
        Long newsId = 1L;
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(newsId);
        newsDTO.setTitle("Updated Title");
        newsDTO.setContent("Updated Content");
        newsDTO.setAuthorId(1L);

        LocalDateTime now = LocalDateTime.now();
        NewsModel existingNewsModel = new NewsModel(newsId, "Original Title", "Original Content", now, now, 1L);

        when(newsRepository.readById(newsId)).thenReturn(existingNewsModel);


        AuthorModel author = new AuthorModel(1L, "Author Name");
        when(authorRepository.getAuthorByID(newsDTO.getAuthorId())).thenReturn(author);
        when(newsRepository.updateNews(existingNewsModel)).thenReturn(existingNewsModel);

        Long updatedId = newsService.updateNews(newsDTO);

        verify(newsRepository, times(1)).updateNews(existingNewsModel);
        assertEquals(newsId, updatedId);
        assertEquals("Updated Title", existingNewsModel.getTitle());
        assertEquals("Updated Content", existingNewsModel.getContent());
        assertEquals(newsDTO.getAuthorId(), existingNewsModel.getAuthorId());
    }
}