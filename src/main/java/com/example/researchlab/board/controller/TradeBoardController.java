package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.TradeBoardService;
import com.example.researchlab.board.model.vo.TradePost;
import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.template.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tradeBoard")
@RequiredArgsConstructor
public class TradeBoardController {

    private final TradeBoardService tradeBoardService;
    private final ResponseTemplate responseTemplate;

    // 글 조회
    // 글 쓰기
    @PostMapping("/posts")
    public ResponseEntity<ResponseData<Object>> saveTradePost(TradePost tradePost) {
        if (tradePost.getTitle().isEmpty()) {
            return responseTemplate.fail("작성 실패", HttpStatus.BAD_REQUEST);
        }
        int result = tradeBoardService.saveTradePost(tradePost);

        if (result > 0) {
            return responseTemplate.success("작성 성공", null, HttpStatus.OK);
        }

        return responseTemplate.fail("작성 실패", HttpStatus.BAD_REQUEST);
    }
    // 상세 조회
    // 글 수정
    // 글 삭제

}
