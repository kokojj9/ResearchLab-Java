package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.TradeBoardService;
import com.example.researchlab.board.model.vo.TradePost;
import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.template.ResponseTemplate;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @Mock
    private HttpSession session;

    @Mock
    private ServletContext servletContext;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        when(session.getServletContext()).thenReturn(servletContext);
        when(servletContext.getRealPath(anyString())).thenReturn("/mock/path");

        // 파일 저장 관련 모킹
        doNothing().when(tradeBoardController).saveFile(any(HttpSession.class), any(MultipartFile.class));
    }

    @Test
    public void testSaveTradePostSuccess() throws IOException {
        TradePost tradePost = new TradePost();
        tradePost.setTitle("Test Title");
        tradePost.setContent("Test Content");

        MockMultipartFile mockFile = new MockMultipartFile("images", "test.jpg", "image/jpeg", "test data".getBytes());
        List<MultipartFile> images = new ArrayList<>();
        images.add(mockFile);

        when(tradeBoardService.saveTradePost(any(TradePost.class))).thenReturn(1);
        when(responseTemplate.success(anyString(), any(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.OK));

        ResponseEntity<ResponseData<Object>> response = tradeBoardController.saveTradePost(tradePost, images, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(responseTemplate).success("작성 성공", null, HttpStatus.OK);
    }

    @Test
    public void testSaveTradePostFailWhenSaveFails() throws IOException {
        TradePost tradePost = new TradePost();
        tradePost.setTitle("Test Title");
        tradePost.setContent("Test Content");

        MockMultipartFile mockFile = new MockMultipartFile("images", "test.jpg", "image/jpeg", "test data".getBytes());
        List<MultipartFile> images = new ArrayList<>();
        images.add(mockFile);

        when(tradeBoardService.saveTradePost(any(TradePost.class))).thenReturn(0);
        when(responseTemplate.fail(anyString(), any(HttpStatus.class)))
                .thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.BAD_REQUEST));

        ResponseEntity<ResponseData<Object>> response = tradeBoardController.saveTradePost(tradePost, images, session);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(responseTemplate).fail("작성 실패", HttpStatus.BAD_REQUEST);
    }
}

