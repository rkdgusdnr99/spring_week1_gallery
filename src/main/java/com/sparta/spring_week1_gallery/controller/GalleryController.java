package com.sparta.spring_week1_gallery.controller;

import com.sparta.spring_week1_gallery.dto.GalleryRequestDto;
import com.sparta.spring_week1_gallery.dto.GalleryResponseDto;
import com.sparta.spring_week1_gallery.entity.Gallery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("/api")
public class GalleryController {

    private final JdbcTemplate jdbcTemplate;

    public GalleryController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/gall")
    public GalleryResponseDto createGallery(@RequestBody GalleryRequestDto requestDto) { // 3번 요청 완성
        // RequestDto -> Entity
        Gallery gallery = new Gallery(requestDto);

        // DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO gallery (user_pw, username, title, contents, updateAt) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update( con -> {
                    PreparedStatement preparedStatement = con.prepareStatement(sql,
                            Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, gallery.getUser_pw());
                    preparedStatement.setString(2, gallery.getUsername());
                    preparedStatement.setString(3, gallery.getTitle());
                    preparedStatement.setString(4, gallery.getContents());
                    preparedStatement.setTimestamp(5, gallery.getUpdatedAt());

                    return preparedStatement;
                },
                keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        gallery.setId(id);

        // Entity -> ResponseDto
        GalleryResponseDto galleryResponseDto = new GalleryResponseDto(gallery);

        return galleryResponseDto;
    }

    @GetMapping("/gall")
    public List<GalleryResponseDto> getGallery() { // 2번 요청 완성
        // DB 조회
        String sql = "SELECT title, username, contents, updateAt FROM gallery ORDER BY updateAt DESC";//order by로 내림차순

        return jdbcTemplate.query(sql, new RowMapper<GalleryResponseDto>() {
            @Override
            public GalleryResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                String title = rs.getString("title");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                Timestamp updateAt = rs.getTimestamp("updateAt");

                return new GalleryResponseDto(title, username, contents, updateAt);
            }
        });
    }

    @GetMapping("/gall/{id}")
    public GalleryResponseDto getOneGallery(@PathVariable Long id) { // 4번 요청 완성
        // DB 조회
        Gallery gallery = findById(id);

        if (gallery != null) {
            String sql = "SELECT title, username, updateAt, contents FROM gallery WHERE id = ?";// 해당 아이디 값으로 된 게시물 조회
            return jdbcTemplate.queryForObject(sql, new RowMapper<GalleryResponseDto>() {
                @Override
                public GalleryResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                    // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                    String title = rs.getString("title");
                    String username = rs.getString("username");
                    Timestamp updateAt = rs.getTimestamp("updateAt");
                    String contents = rs.getString("contents");
                    return new GalleryResponseDto(title, username, contents, updateAt);
                }
            }, id);
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @PutMapping("/gall/{id}")
    public GalleryResponseDto updateGallery(@PathVariable Long id, @RequestBody GalleryRequestDto requestDto) { // 5번 요청
        // 해당 메모가 DB에 존재하는지 확인
        Gallery gallery = findById(id);
        String getPasswordQuery = "SELECT user_pw FROM gallery WHERE id = ?";
        String actualPassword = jdbcTemplate.queryForObject(getPasswordQuery, String.class, id); // 비밀번호 저장

        if(gallery != null && actualPassword.equals(requestDto.getUser_pw())) {
            // memo 내용 수정
            String sql = "UPDATE gallery SET title = ?, username = ?, contents = ?, updateAt = ? WHERE id = ?";

            jdbcTemplate.update(sql, requestDto.getTitle(), requestDto.getUsername(), requestDto.getContents(), requestDto.getUpdateAt(), id);

            Gallery updatedGallery = findById(id);

            // Entity -> ResponseDto
            return new GalleryResponseDto(updatedGallery); // 게시글을 반환
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    @DeleteMapping("/gall/{id}")
    public String deleteGallery(@PathVariable Long id, @RequestBody GalleryRequestDto requestDto) { // 6번 요청
        // 해당 메모가 DB에 존재하는지 확인
        Gallery gallery = findById(id);
        String getPasswordQuery = "SELECT user_pw FROM gallery WHERE id = ?"; // 5번과 동일한 과정
        String actualPassword = jdbcTemplate.queryForObject(getPasswordQuery, String.class, id);
        if(gallery != null && actualPassword.equals(requestDto.getUser_pw())) {
            // memo 삭제
            String sql = "DELETE FROM gallery WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return "삭제 성공";
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    private Gallery findById(Long id) {
        // DB 조회
        String sql = "SELECT * FROM gallery WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if(resultSet.next()) {
                Gallery gallery = new Gallery();
                gallery.setUser_pw(resultSet.getString("user_pw"));
                gallery.setTitle(resultSet.getString("title"));
                gallery.setUsername(resultSet.getString("username"));
                gallery.setContents(resultSet.getString("contents"));
                gallery.setUpdatedAt(resultSet.getTimestamp("updateAt"));
                return gallery;
            } else {
                return null;
            }
        }, id);
    }
}
