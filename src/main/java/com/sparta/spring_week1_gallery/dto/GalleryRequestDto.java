package com.sparta.spring_week1_gallery.dto;

import lombok.Getter;

import java.sql.Timestamp;
@Getter
public class GalleryRequestDto { // 정보 주는 Dto
    private Long id;
    private String user_pw;

    private String title;
    private String username;
    private String contents;
    private Timestamp updatedAt;
}
