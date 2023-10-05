package com.mjc.school;

import com.mjc.school.repository.model.NewsModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {

    NewsMapper INSTANSE = Mappers.getMapper(NewsMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "createDate", source = "createDate")
    @Mapping(target = "lastUpdateDate", source = "lastUpdateDate")
    @Mapping(target = "authorId", source = "authorId")
    NewsDTO newsToNewsDTO(NewsModel newsModel);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "createDate", source = "createDate")
    @Mapping(target = "lastUpdateDate", source = "lastUpdateDate")
    @Mapping(target = "authorId", source = "authorId")
    NewsModel newsDTOToNews(NewsDTO newsDTO);
}
