package com.example.researchlab.investment.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.investment.model.service.InvestmentService;
import com.example.researchlab.investment.model.vo.MyStockList;
import com.example.researchlab.member.model.vo.Member;
import com.example.researchlab.template.ResponseTemplate;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class InvestmentControllerTest {

    @InjectMocks
    private InvestmentController investmentController;

    @Mock
    private InvestmentService investmentService;

    @Mock
    private HttpSession session;

    @Mock
    private ResponseTemplate responseTemplate;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveStockListSuccess() {
        // 리스트 저장 성공 테스트
        // 필요 객체 생성 및 입력 값 설정
        Member member = new Member();
        member.setMemberNo("1");
        member.setMemberId("testUser");
        MyStockList stockList = new MyStockList();

        when(session.getAttribute("loginMember")).thenReturn(member);
        when(investmentService.saveStockList(any(HashMap.class))).thenReturn(1);
        when(responseTemplate.success(anyString(), any(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.OK));
        // 동작
        ResponseEntity<ResponseData<Object>> response = investmentController.saveStockList(stockList, session);
        
        //결과 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(responseTemplate).success("리스트 저장 성공", null, HttpStatus.OK);
    }

    @Test
    public void testSaveStockListFailWhenNoMember(){
        // 비로그인 유저의 요청일 경우 예외처리
        MyStockList stockList = new MyStockList();

        when(session.getAttribute("loginMember")).thenReturn(null);
        when(responseTemplate.fail(anyString(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.BAD_REQUEST));
        // 동작
        ResponseEntity<ResponseData<Object>> response = investmentController.saveStockList(stockList, session);

        //결과 검증
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(responseTemplate).fail("잘못된 요청", HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveStockListFailWhenNoStockList(){
        // 종목 정보가 없는 요청일 경우
    }

    @Test
    public void testSaveStockListFailWhenSaveFails() {
        // 서버 오류 일 경우
    }

}
