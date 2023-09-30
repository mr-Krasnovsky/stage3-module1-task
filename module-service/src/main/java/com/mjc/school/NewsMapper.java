package com.mjc.school;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NewsMapper {
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "createDate", source = "createDate")
    @Mapping(target = "lastUpdateDate", source = "lastUpdateDate")
    @Mapping(target = "authorId", source = "authorId")

    NewsDTO newsToNewsDTO(News news);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "createDate", source = "createDate")
    @Mapping(target = "lastUpdateDate", source = "lastUpdateDate")
    @Mapping(target = "authorId", source = "authorId")
    News newsDTOToNews(NewsDTO newsDTO);
}
