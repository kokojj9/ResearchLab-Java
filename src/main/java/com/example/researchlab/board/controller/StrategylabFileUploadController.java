package com.example.researchlab.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/strategy/fileUpload")
@RequiredArgsConstructor
@Slf4j
public class StrategylabFileUploadController {

    @PostMapping
    public String getFileUrl(@RequestPart("image") MultipartFile image) {
        log.info("quill 이미지 요청: {}", image.getOriginalFilename());
        return "";
    }


}
