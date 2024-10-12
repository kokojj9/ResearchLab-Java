package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.vo.PostImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardFileServiceImpl implements BoardFileService {

    private final Logger logger;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public String saveFile(MultipartFile upfile) throws IOException {
        String originName = upfile.getOriginalFilename();

        String ext = originName.substring(originName.lastIndexOf("."));
        String currentTime = new SimpleDateFormat("yyyyMMdd").format(new Date());
        int random = (int) (Math.random() * 90000) + 10000;
        String changeName = currentTime + "_" + random + ext;

        File file = new File(uploadDir, changeName);
        upfile.transferTo(file);  // 파일 저장

        return "upfiles/boardImages/" + changeName;
    }

    @Override
    public void deleteFiles(List<PostImage> imageList) throws IOException {
        for (PostImage file : imageList) {
            String fileName = file.getStoredName();

            try {
                deleteImages(fileName);
            } catch (IOException e) {
                logger.error("파일 삭제 실패: {}", fileName);
            }
        }
    }

    private void deleteImages(String fileName) throws IOException {
        String root = System.getProperty("user.dir");
        String filePath = root +  "/src/main/resources/static/" + fileName;
        File file = new File(filePath);

        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("파일 삭제 실패");
            }
        } else {
            throw new IOException("파일이 존재하지 않습니다.");
        }
    }

}
