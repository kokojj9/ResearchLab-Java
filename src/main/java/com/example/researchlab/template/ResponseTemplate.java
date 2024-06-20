package com.example.researchlab.template;

import com.example.researchlab.common.model.vo.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ResponseTemplate {

    <T> ResponseEntity<ResponseData<T>> success(String resultMessage, T data, HttpStatus httpStatus);

    <T> ResponseEntity<ResponseData<T>> fail(String resultMessage, HttpStatus httpStatus);

}
