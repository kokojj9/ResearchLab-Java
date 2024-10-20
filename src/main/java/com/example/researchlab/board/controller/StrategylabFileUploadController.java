package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.BoardFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/strategy/fileUpload")
@RequiredArgsConstructor
@Slf4j
public class StrategylabFileUploadController {

    @Value("file.upload-dir")
    private String uploadDir;

    private final BoardFileService boardFileService;

    @PostMapping
    public ResponseEntity<?> getFileUrl(@RequestPart("image") MultipartFile image) {
        log.info("quill 이미지 삽입: {}", image.getOriginalFilename());
        try {
            String imageUrl = boardFileService.saveFile(image);

            Map<String, String> response = new HashMap<>();
            response.put("imgUrl", imageUrl);

            return ResponseEntity.ok().body(response);
        } catch (IOException e) {
            log.error("이미지 업로드 실패");
            return ResponseEntity.status(500).body("이미지 업로드 실패");
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFile(){
        log.info("게시글 작성 취소");

        return null;
    }



}
