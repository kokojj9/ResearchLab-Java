package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.TradeBoardService;
import com.example.researchlab.board.model.vo.TradePost;
import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.template.ResponseTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TradeBoardControllerTest {

    @InjectMocks
    private TradeBoardController tradeBoardController;

    @Mock
    private TradeBoardService tradeBoardService;

    @Mock
    private ResponseTemplate responseTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveTradePostSuccess() {
        TradePost tradePost = new TradePost();
        tradePost.setTitle("Test Title");
        tradePost.setContent("Test Content");

        when(tradeBoardService.saveTradePost(any(TradePost.class))).thenReturn(1);
        when(responseTemplate.success(anyString(), any(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.OK));

        ResponseEntity<ResponseData<Object>> response = tradeBoardController.saveTradePost(tradePost);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(responseTemplate).success("작성 성공", null, HttpStatus.OK);
    }

    @Test
    public void testSaveTradePostFailWhenSaveFails() {
        TradePost tradePost = new TradePost();
        tradePost.setTitle("Test Title");
        tradePost.setContent("Test Content");

        when(tradeBoardService.saveTradePost(any(TradePost.class))).thenReturn(0);
        when(responseTemplate.fail(anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.BAD_REQUEST));

        ResponseEntity<ResponseData<Object>> response = tradeBoardController.saveTradePost(tradePost);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(responseTemplate).fail("작성 실패", HttpStatus.BAD_REQUEST);
    }
}
