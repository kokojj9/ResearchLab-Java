package com.example.researchlab.common.model.vo;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseData<T> {
    private String responseCode;
    private String resultMessage;
    private T data;
}
