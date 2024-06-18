package com.example.researchlab.common.template;

import com.example.researchlab.common.model.vo.ResponseData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface ResponseTemplate {

    ResponseEntity<ResponseData> success(ResponseData rd, HttpStatus httpStatus);

    ResponseEntity<ResponseData> fail(ResponseData rd, HttpStatus httpStatus);

}
