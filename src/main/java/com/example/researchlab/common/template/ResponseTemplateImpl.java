package com.example.researchlab.common.template;

import com.example.researchlab.common.model.vo.ResponseData;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class ResponseTemplateImpl implements ResponseTemplate{

    @Override
    public ResponseEntity<ResponseData> success(ResponseData rd, HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<ResponseData>(rd, headers, httpStatus.OK);
    }

    @Override
    public ResponseEntity<ResponseData> fail(ResponseData rd, HttpStatus httpStatus) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<ResponseData>(headers, httpStatus);
    }
}
