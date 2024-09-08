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
    @GetMapping
    public Page<Post> selectTradePosts(@RequestParam int page, @RequestParam int size) {
        logger.info("전체 게시글 조회: page={}, size={}", page, size);
        return strategylabService.selectTradePosts(page, size);
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
            int result = strategylabService.saveTradePost(post);

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
        return strategylabService.selectPostDetail(postNo);
    }
    // 글 수정

    // 글삭제
    @DeleteMapping("/{postNo}")
    public ResponseData<Object> deletePost(@PathVariable int postNo, @RequestParam String memberId){
        logger.info("글 삭제 시도: {}", postNo + "/" + memberId);
        ResponseData.ResponseDataBuilder<Object> responseDataBuilder =  ResponseData.builder();

        boolean result = strategylabService.deletePost(postNo, memberId);

        if(result) {
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
