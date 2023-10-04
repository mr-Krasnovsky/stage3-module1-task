package com.mjc.school;

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

public class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private AuthorRepository authorRepository;

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
        List<News> mockNewsList = new ArrayList<>();
        when(newsRepository.getAllNews()).thenReturn(mockNewsList);

        List<NewsDTO> result = newsService.getAllNews();

        assertEquals(mockNewsList.size(), result.size());
        verify(newsMapper, times(mockNewsList.size())).newsToNewsDTO(any(News.class));
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

        when(authorRepository.getAuthorByID(newsDTO.getAuthorId())).thenReturn(new Author(newsDTO.getAuthorId(), "Author Name"));
        when(newsMapper.newsDTOToNews(newsDTO)).thenReturn(new News(newsDTO.getId(), newsDTO.getTitle(), newsDTO.getContent(), LocalDateTime.now(), LocalDateTime.now(), newsDTO.getAuthorId()));

        Long generatedId = newsService.createNews(newsDTO);

        verify(newsRepository, times(1)).saveNews(any(News.class));
        assertEquals(generatedId, newsDTO.getId());
        assertEquals(newsDTO.getCreateDate(), newsDTO.getLastUpdateDate());
    }

    @Test
    public void testRemoveNewsById() throws IOException {
        Long newsId = 1L;
        NewsDTO newsDTO = new NewsDTO();
        newsDTO.setId(newsId);

        LocalDateTime now = LocalDateTime.now();
        when(newsMapper.newsDTOToNews(newsDTO)).thenReturn(new News(newsId, "Sample Title", "Sample Content", now, now, 1L));
        when(newsRepository.getAllNews()).thenReturn(new ArrayList<>());

        assertThrows(InputValidationException.class, () -> {
            newsService.removeNewsById(newsDTO);
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
        News existingNews = new News(newsId, "Original Title", "Original Content", now, now, 1L);

        when(newsRepository.getNewsById(newsId)).thenReturn(existingNews);


        Author author = new Author(1L, "Author Name");
        when(authorRepository.getAuthorByID(newsDTO.getAuthorId())).thenReturn(author);
        when(newsRepository.updateNews(existingNews)).thenReturn(1L);

        Long updatedId = newsService.updateNews(newsDTO);

        verify(newsRepository, times(1)).updateNews(existingNews);
        assertEquals(newsId, updatedId);
        assertEquals("Updated Title", existingNews.getTitle());
        assertEquals("Updated Content", existingNews.getContent());
        assertEquals(newsDTO.getAuthorId(), existingNews.getAuthorId());
    }
}