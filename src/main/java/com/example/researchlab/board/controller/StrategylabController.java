package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.BoardFileService;
import com.example.researchlab.board.model.service.StrategylabService;
import com.example.researchlab.board.model.vo.Post;
import com.example.researchlab.board.model.vo.PostImage;
import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.template.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/strategylab")
@RequiredArgsConstructor
public class StrategylabController {

    private final StrategylabService strategylabService;
    private final ResponseTemplate responseTemplate;
    private final BoardFileService boardFileService;
    private static final Logger logger = LoggerFactory.getLogger(StrategylabController.class);
    // 글 조회
    @GetMapping("/posts")
    public Page<Post> selectTradePosts(@RequestParam int page, @RequestParam int size) {
        logger.info("전체 게시글 조회: page={}, size={}", page, size);
        return strategylabService.selectTradePosts(page, size);
    }

    @GetMapping("/members/{memberId}/posts")
    public Page<Post> selectMyPosts(@RequestParam int page, @RequestParam int size, @PathVariable String memberId) {
        logger.info("내 게시글 조회: {}", memberId);
        return strategylabService.selectMyPosts(page, size, memberId);
    }

    // 글 쓰기
    @PostMapping("/posts")
    public ResponseEntity<ResponseData<Object>> saveTradePost(@RequestPart("tradePost") Post post,
                                                              @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {

        if (post.getTitle().isEmpty()) {
           return responseTemplate.fail("작성 실패", HttpStatus.BAD_REQUEST);
        }

        strategylabService.saveTradePost(post, images);
        return responseTemplate.success("작성 성공", null, HttpStatus.OK);
    }

    // 상세 조회
    @GetMapping("/posts/{postNo}")
    public Post selectPostDetail(@PathVariable int postNo){
        logger.info("게시글 조회 시도: {}", postNo);
        return strategylabService.selectPostDetail(postNo);
    }
    // 글 수정

    // 글삭제
    @DeleteMapping("/posts/{postNo}")
    public ResponseData<Object> deletePost(@PathVariable int postNo, @RequestParam String memberId){
        logger.info("글 삭제 시도: {}", postNo + "/" + memberId);
        ResponseData.ResponseDataBuilder<Object> responseDataBuilder =  ResponseData.builder();
        boolean result = strategylabService.deletePost(postNo, memberId);

        if(result) {
            logger.info("글 삭제 성공: {}", postNo);
            responseDataBuilder.responseCode("YY")
                               .resultMessage("삭제 성공")
                               .data(null);
        } else {
            responseDataBuilder.responseCode("NN")
                               .resultMessage("삭제 실패")
                               .data(null);
        }

        return responseDataBuilder.build();
    }


}