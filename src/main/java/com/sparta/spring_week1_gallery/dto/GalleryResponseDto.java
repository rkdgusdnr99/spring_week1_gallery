package com.sparta.spring_week1_gallery.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.spring_week1_gallery.entity.Gallery;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class GalleryResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String user_pw;
    private String title;
    private String username;
    private String contents;
    private Timestamp updatedAt;

    public GalleryResponseDto(Gallery gallery) {
        this.id = gallery.getId();
        this.user_pw = gallery.getUser_pw();
        this.title = gallery.getTitle();
        this.username = gallery.getUsername();
        this.contents = gallery.getContents();
        this.updatedAt = gallery.getUpdatedAt();
    }

    public GalleryResponseDto(String title, String username, String contents, Timestamp updatedAt) { // 2번(전체 게시글 관련)
        this.title = title;
        this.username = username;
        this.contents = contents;
        this.updatedAt = updatedAt;
    }

}




