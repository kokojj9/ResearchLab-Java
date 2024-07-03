package com.example.researchlab.investment.controller;

import com.example.researchlab.investment.model.service.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/investment")
@RequiredArgsConstructor
public class InvestmentController {

    private final InvestmentService investmentService;
    @GetMapping(value = "/findStock", produces = "application/json; charset=UTF-8")
    public String getStockInfo(@RequestParam String stockName) throws IOException {
        // 여러날짜 데이터가 조회되어 가장 최근날짜 데이터만 필터링하는 로직은 프론트에서 구성함
        // 공공데이터 api는 2일전 종가로 조회 되기 때문에 추후에 고도화를 통해 바꿀 예정
        String encodedStockName = URLEncoder.encode(stockName, "UTF-8");

        investmentService.getStockInfo(encodedStockName);

        return null;
    }






}
