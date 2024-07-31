package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.BoardFileService;
import com.example.researchlab.board.model.service.TradeBoardService;
import com.example.researchlab.board.model.vo.PostImage;
import com.example.researchlab.board.model.vo.TradePost;
import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.template.ResponseTemplate;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tradeBoard")
@RequiredArgsConstructor
public class TradeBoardController {

    private final TradeBoardService tradeBoardService;
    private final ResponseTemplate responseTemplate;
    private final BoardFileService boardFileService;

    // 글 조회
    // 글 쓰기
    @PostMapping("/posts")
    public ResponseEntity<ResponseData<Object>> saveTradePost(@RequestPart("tradePost") TradePost tradePost,
                                                              @RequestPart("images") List<MultipartFile> images,
                                                              HttpSession session) throws IOException {
        ResponseEntity<ResponseData<Object>> rd = null;

        if (tradePost.getTitle().isEmpty()) {
            rd = responseTemplate.fail("작성 실패", HttpStatus.BAD_REQUEST);
        } else {
            if(!images.get(0).getOriginalFilename().equals("")){
                List<PostImage> imageList = new ArrayList<>();

                for (MultipartFile image : images) {
                    if (!image.isEmpty()) {
                        String storedFileName = boardFileService.saveFile(session, image);
                        PostImage postImage = new PostImage();
                        postImage.setOriginalName(image.getOriginalFilename());
                        postImage.setStoredName(storedFileName);
                        imageList.add(postImage);
                    }
                }
                tradePost.setImageList(imageList);
            }

            int result = tradeBoardService.saveTradePost(tradePost);

            rd = result > 0 ? responseTemplate.success("작성 성공", null, HttpStatus.OK) :
                              responseTemplate.fail("작성 실패", HttpStatus.BAD_REQUEST);
        }
        return rd;
    }

    // 상세 조회
    // 글 수정
    // 글 삭제

}
