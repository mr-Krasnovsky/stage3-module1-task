package com.mjc.school;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-01T00:35:45+0300",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.2.jar, environment: Java 17.0.6 (Oracle Corporation)"
)
public class NewsMapperImpl implements NewsMapper {

    @Override
    public NewsDTO newsToNewsDTO(News news) {
        if ( news == null ) {
            return null;
        }

        NewsDTO newsDTO = new NewsDTO();

        newsDTO.setId( news.getId() );
        newsDTO.setTitle( news.getTitle() );
        newsDTO.setContent( news.getContent() );
        newsDTO.setCreateDate( news.getCreateDate() );
        newsDTO.setLastUpdateDate( news.getLastUpdateDate() );
        newsDTO.setAuthorId( news.getAuthorId() );

        return newsDTO;
    }

    @Override
    public News newsDTOToNews(NewsDTO newsDTO) {
        if ( newsDTO == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String content = null;
        LocalDateTime createDate = null;
        LocalDateTime lastUpdateDate = null;
        Long authorId = null;

        id = newsDTO.getId();
        title = newsDTO.getTitle();
        content = newsDTO.getContent();
        createDate = newsDTO.getCreateDate();
        lastUpdateDate = newsDTO.getLastUpdateDate();
        authorId = newsDTO.getAuthorId();

        News news = new News( id, title, content, createDate, lastUpdateDate, authorId );

        return news;
    }
}
