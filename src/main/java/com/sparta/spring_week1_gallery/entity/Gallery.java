package com.sparta.spring_week1_gallery.entity;

import com.sparta.spring_week1_gallery.dto.GalleryRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.TimeZone;

@Getter
@Setter
@NoArgsConstructor
public class Gallery {
    private Long id;
    private String user_pw;
    private String title;
    private String username;
    private String contents;
    private Timestamp updatedAt;

    public Gallery(GalleryRequestDto responseDto) {
        this.user_pw = responseDto.getUser_pw();
        this.username = responseDto.getUsername();
        this.title = responseDto.getTitle();
        this.contents = responseDto.getContents();
        this.updatedAt = responseDto.getUpdateAt();
    }
}
