package com.example.researchlab.template;

import com.example.researchlab.common.model.vo.ResponseData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class ResponseTemplateImpl implements ResponseTemplate{

    @Override
    public <T> ResponseEntity<ResponseData<T>> success(String resultMessage, T data, HttpStatus httpStatus) {
        ResponseData<T> rd = ResponseData.<T>builder()
                                         .data(data)
                                         .resultMessage(resultMessage)
                                         .responseCode("YY")
                                         .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<ResponseData<T>>(rd, headers, httpStatus);
    }

    @Override
    public <T> ResponseEntity<ResponseData<T>> fail(String resultMessage, HttpStatus httpStatus) {
        ResponseData<Void> rd = ResponseData.<Void>builder()
                                            .data(null)
                                            .resultMessage(resultMessage)
                                            .responseCode("NN")
                                            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<ResponseData<T>>(headers, httpStatus);
    }
}
