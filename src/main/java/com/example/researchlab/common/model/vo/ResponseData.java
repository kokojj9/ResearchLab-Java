package com.example.researchlab.common.model.vo;

import com.example.researchlab.member.model.vo.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseData {
    private String responseCode;
    private String resultMessage;
    private Member data;
}
