package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.vo.PostImage;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardFileService {
    String saveFile(MultipartFile image) throws IOException;

    void deleteFiles(List<PostImage> imageList) throws IOException;

}
