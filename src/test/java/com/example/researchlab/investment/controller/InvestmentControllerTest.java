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
import java.util.List;

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
        // Arrange
        Member member = new Member();
        member.setMemberNo("1");
        member.setMemberId("testUser");
        MyStockList stockList = new MyStockList();
        List<MyStockList> updatedList = List.of(stockList);

        when(session.getAttribute("loginMember")).thenReturn(member);
        when(investmentService.saveStockList(any(HashMap.class))).thenReturn(updatedList);
        when(responseTemplate.success(anyString(), any(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.OK));

        // Act
        ResponseEntity<ResponseData<Object>> response = investmentController.saveStockList(stockList, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(responseTemplate).success("리스트 저장 성공", updatedList, HttpStatus.OK);
    }

    @Test
    public void testSaveStockListFailWhenNoMember() {
        // Arrange
        MyStockList stockList = new MyStockList();

        when(session.getAttribute("loginMember")).thenReturn(null);
        when(responseTemplate.fail(anyString(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<ResponseData<Object>> response = investmentController.saveStockList(stockList, session);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(responseTemplate).fail("잘못된 요청", HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveStockListFailWhenNoStockList() {
        // Arrange
        Member member = new Member();
        member.setMemberNo("1");
        member.setMemberId("testUser");

        when(session.getAttribute("loginMember")).thenReturn(member);
        when(responseTemplate.fail(anyString(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<ResponseData<Object>> response = investmentController.saveStockList(null, session);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(responseTemplate).fail("잘못된 요청", HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testSaveStockListFailWhenSaveFails() {
        // Arrange
        Member member = new Member();
        member.setMemberNo("1");
        member.setMemberId("testUser");
        MyStockList stockList = new MyStockList();

        when(session.getAttribute("loginMember")).thenReturn(member);
        when(investmentService.saveStockList(any(HashMap.class))).thenReturn(null);
        when(responseTemplate.fail(anyString(), any(HttpStatus.class))).thenReturn(new ResponseEntity<>(new ResponseData<>(), HttpStatus.BAD_REQUEST));

        // Act
        ResponseEntity<ResponseData<Object>> response = investmentController.saveStockList(stockList, session);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(responseTemplate).fail("잘못된 요청", HttpStatus.BAD_REQUEST);
    }
}
