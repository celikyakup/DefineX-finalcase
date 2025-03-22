package patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.request.CommentRequest;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.dto.response.CommentResponse;
import patika.dev.definexjavaspringbootbootcamp.advancedTaskManagement.entity.Comment;


import java.util.List;

@Mapper
public interface CommentMapper {
    Comment asEntity(CommentRequest commentRequest);

    CommentResponse asOutput(Comment comment);

    List<CommentResponse> asOutput(List<Comment> comments);

    void update(@MappingTarget Comment entity, CommentRequest commentRequest);
}
