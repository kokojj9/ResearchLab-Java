package com.example.researchlab.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger;

    @PostMapping
    public String getFileUrl(@RequestPart MultipartFile file) {
        logger.info("quill 이미지 요청: {}", file.getOriginalFilename());
        return "";
    }


}
