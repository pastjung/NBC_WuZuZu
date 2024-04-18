package com.sparta.wuzuzu.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.wuzuzu.domain.comment.entity.Comment;
import com.sparta.wuzuzu.domain.user.entity.User;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentResponse {

    private Long commentId;
    private Long userId;
    private String username;
    private String contents;


    public CommentResponse(Comment comment){
        User user = comment.getUser();
        this.commentId = comment.getCommentId();
        this.userId = user.getUserId();
        this.username = user.getUserName();
        this.contents = comment.getContents();
    }
}
