package com.example.researchlab.board.model.vo;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class TradePost {

    private int postNo;
    @NonNull
    private String title;
    private String content;
    private String writer;
    private Date createDate;
    private Date updateDate;
}
