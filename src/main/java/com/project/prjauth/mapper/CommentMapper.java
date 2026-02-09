package com.project.prjauth.mapper;

import com.project.prjauth.dto.response.CommentResponse;
import com.project.prjauth.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "repliesCount", ignore = true)
    CommentResponse toCommentResponse(Comment comment);
}
