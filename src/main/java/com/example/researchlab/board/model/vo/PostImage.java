package com.example.researchlab.board.model.vo;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class PostImage {

    private int imageNo;
    private int postNo;
    private String originalName;
    private String storedName;
    private Date createDate;
}
