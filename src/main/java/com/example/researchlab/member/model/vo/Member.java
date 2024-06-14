package com.example.researchlab.member.model.vo;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor @Getter @Setter @ToString
public class Member {
    String memberId;
    String memberPwd;
    String email;
}
