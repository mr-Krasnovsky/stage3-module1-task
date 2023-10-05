package com.mjc.school;

import com.mjc.school.repository.model.NewsModel;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-10-05T10:05:34+0400",
    comments = "version: 1.4.2.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.2.jar, environment: Java 17.0.6 (Oracle Corporation)"
)
public class NewsMapperImpl implements NewsMapper {

    @Override
    public NewsDTO newsToNewsDTO(NewsModel newsModel) {
        if ( newsModel == null ) {
            return null;
        }

        NewsDTO newsDTO = new NewsDTO();

        newsDTO.setId( newsModel.getId() );
        newsDTO.setTitle( newsModel.getTitle() );
        newsDTO.setContent( newsModel.getContent() );
        newsDTO.setCreateDate( newsModel.getCreateDate() );
        newsDTO.setLastUpdateDate( newsModel.getLastUpdateDate() );
        newsDTO.setAuthorId( newsModel.getAuthorId() );

        return newsDTO;
    }

    @Override
    public NewsModel newsDTOToNews(NewsDTO newsDTO) {
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

        NewsModel newsModel = new NewsModel( id, title, content, createDate, lastUpdateDate, authorId );

        return newsModel;
    }
}
