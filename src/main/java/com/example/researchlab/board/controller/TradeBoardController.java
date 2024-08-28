package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.BoardFileService;
import com.example.researchlab.board.model.service.TradeBoardService;
import com.example.researchlab.board.model.vo.Post;
import com.example.researchlab.board.model.vo.PostImage;
import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.member.controller.MemberController;
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
@RequestMapping("/tradeBoard")
@RequiredArgsConstructor
public class TradeBoardController {

    private final TradeBoardService tradeBoardService;
    private final ResponseTemplate responseTemplate;
    private final BoardFileService boardFileService;
    private static final Logger logger = LoggerFactory.getLogger(TradeBoardController.class);
    // 글 조회
    @GetMapping
    public Page<Post> selectTradePosts(@RequestParam int page, @RequestParam int size) {
        logger.info("전체 게시글 조회: page={}, size={}", page, size);
        return tradeBoardService.selectTradePosts(page, size);
    }

    // 글 쓰기
    @PostMapping("/posts")
    public ResponseEntity<ResponseData<Object>> saveTradePost(@RequestPart("tradePost") Post post,
                                                              @RequestPart(value = "images", required = false) List<MultipartFile> images) throws IOException {
        ResponseEntity<ResponseData<Object>> rd;

        if (post.getTitle().isEmpty()) {
            rd = responseTemplate.fail("작성 실패", HttpStatus.BAD_REQUEST);
        } else {
            if (images != null) {
                post.setImageList(setImages(images));
            }
            int result = tradeBoardService.saveTradePost(post);

            rd = result > 0 ? responseTemplate.success("작성 성공", null, HttpStatus.OK) :
                    responseTemplate.fail("작성 실패", HttpStatus.BAD_REQUEST);
        }
        return rd;
    }

    private List<PostImage> setImages(List<MultipartFile> images) throws IOException {
        List<PostImage> imageList = new ArrayList<>();
        for (MultipartFile image : images) {
            String storedFileName = boardFileService.saveFile(image);
            PostImage postImage = new PostImage();
            postImage.setOriginalName(image.getOriginalFilename());
            postImage.setStoredName(storedFileName);
            imageList.add(postImage);
        }
        return imageList;
    }

    // 상세 조회
    @GetMapping("/{postNo}")
    public Post selectPostDetail(@PathVariable int postNo){
        logger.info("게시글 조회 시도: {}", postNo);
        return tradeBoardService.selectPostDetail(postNo);
    }
    // 글 수정
    // 글 삭제

}
