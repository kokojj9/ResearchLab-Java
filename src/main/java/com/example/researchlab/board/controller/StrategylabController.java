package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.BoardFileService;
import com.example.researchlab.board.model.service.StrategylabService;
import com.example.researchlab.board.model.vo.Post;
import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.template.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/strategylab")
@RequiredArgsConstructor
@Slf4j
public class StrategylabController {

    private final StrategylabService strategylabService;
    private final ResponseTemplate responseTemplate;
    private final BoardFileService boardFileService;

    @GetMapping("/posts")
    public Page<Post> selectTradePosts(@RequestParam int page, @RequestParam int size) {
        log.info("전체 게시글 조회: page={}, size={}", page, size);
        return strategylabService.selectTradePosts(page, size);
    }

    @GetMapping("/members/{memberId}/posts")
    public Page<Post> selectMyPosts(@RequestParam int page, @RequestParam int size, @PathVariable String memberId) {
        log.info("내 게시글 조회: {}", memberId);
        return strategylabService.selectMyPosts(page, size, memberId);
    }

    @GetMapping("/posts/{postNo}")
    public Post selectPostDetail(@PathVariable int postNo) {
        log.info("상세 조회 시도: {}", postNo);
        return strategylabService.selectPostDetail(postNo);
    }

    @PostMapping("/posts")
    public ResponseEntity<ResponseData<Object>> saveTradePost(@RequestPart("post") Post post,
                                                              @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList) throws IOException {

        if (post.getTitle().isEmpty()) {
            return responseTemplate.fail("작성 실패", HttpStatus.BAD_REQUEST);
        }

        strategylabService.saveTradePost(post, imageList);
        log.info("글 작성: {}", post.getTitle());
        return responseTemplate.success("작성 성공", null, HttpStatus.OK);
    }

    @PutMapping("/posts/{postNo}")
    public ResponseEntity<ResponseData<Object>> updatePost(@PathVariable int postNo,
                                                           @RequestPart("post") Post post,
                                                           @RequestPart(value = "imageList", required = false) List<MultipartFile> imageList) throws IOException {

        if (post.getTitle().isEmpty()) {
            return responseTemplate.fail("수정 실패", HttpStatus.BAD_REQUEST);
        }

        strategylabService.updatePost(postNo, post, imageList);
        log.info("게시글 수정: {}", postNo);
        return responseTemplate.success("게시글 수정 성공", null, HttpStatus.OK);
    }

    @DeleteMapping("/posts/{postNo}")
    public ResponseEntity<ResponseData<Object>> deletePost(@PathVariable int postNo, @RequestParam String memberId) throws IOException {
        log.info("글 삭제 시도: {}, {}", postNo, memberId);
        int result = strategylabService.deletePost(postNo, memberId);

        if (result > 0) {
            log.info("글 삭제 성공: {}", postNo);
            return responseTemplate.success("삭제 성공", null, HttpStatus.OK);
        } else {
            return responseTemplate.fail("삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }



}
