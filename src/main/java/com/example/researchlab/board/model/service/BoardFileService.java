package com.example.researchlab.board.model.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BoardFileService {
    String saveFile(MultipartFile image) throws IOException;

    void deleteFile(String fileName) throws IOException;
}
