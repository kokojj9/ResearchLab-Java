package com.example.researchlab.board.model.vo;

import lombok.*;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Post {

    private int postNo;
    private String title;
    private String content;
    private String writer;
    private Date createDate;
    private Date updateDate;
    private List<PostImage> imageList;
    private int viewCount;

    public Post(int postNo, String title, String content) {
        this.postNo = postNo;
        this.title = title;
        this.content = content;
    }
}
