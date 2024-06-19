package com.example.researchlab.member.model.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class Member {


    String memberNo;
    @NotBlank
    String memberId;
    @NotBlank
    String memberPwd;
    String email;
}
