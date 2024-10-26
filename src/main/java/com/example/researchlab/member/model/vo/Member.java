package com.example.researchlab.member.model.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class Member {

    private int memberNo;
    @NotBlank
    private String memberId;
    @NotBlank
    private String memberPwd;
    private LocalDateTime enrollDate;
    private String email;
    private String status;

    private String token;     // JWT 토큰을 저장할 필드
    private String message;
}
