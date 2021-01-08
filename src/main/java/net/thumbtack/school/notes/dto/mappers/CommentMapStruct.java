package net.thumbtack.school.notes.dto.mappers;

import net.thumbtack.school.notes.dto.request.comment.CommentRequest;
import net.thumbtack.school.notes.dto.response.comment.CommentResponse;
import net.thumbtack.school.notes.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct methods to transform user requests from comment requests to the comment models, and models to the responses
 */
@Mapper
public interface CommentMapStruct {
    CommentMapStruct INSTANCE = Mappers.getMapper(CommentMapStruct.class);

    /**
     * Transforms request to add comment to the comment model
     */
    @Mappings({
            @Mapping(target = "commentBody", source = "body"),
            @Mapping(target = "note.id", source = "noteId")
    })
    Comment requestAddComment(CommentRequest addRequest);

    /**
     * Transforms created comment model to the response
     */
    @Mappings({
            @Mapping(target = "id", source = "comment.id"),
            @Mapping(target = "body", source = "commentBody"),
            @Mapping(target = "noteId", source = "note.id"),
            @Mapping(target = "authorId", source = "author.id"),
            @Mapping(target = "created", source = "creationTime"),
            @Mapping(target = "revisionId", source = "noteRevision.revisionId")
    })
    CommentResponse responseAddComment(Comment comment);
}
