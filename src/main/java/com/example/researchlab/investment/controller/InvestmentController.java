package com.example.researchlab.investment.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/investment")
@RequiredArgsConstructor
public class InvestmentController {

    @Value("${SERVICE_KEY}")
    private String SERVICE_KEY;

    @GetMapping(value = "/findStock", produces = "application/json; charset=UTF-8")
    public String getStockInfo(@RequestParam String stockName) throws IOException {
        String encodedStockName = URLEncoder.encode(stockName, "UTF-8");

        String url = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo"
                   + "?serviceKey=" + SERVICE_KEY
                   + "&numOfRows=10"
                   + "&pageNo=1"
                   + "&likeItmsNm=" + encodedStockName
                   + "&resultType=json";

        URL requestUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8")); // 인코딩 설정
        StringBuilder responseJson = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            responseJson.append(line);
        }

        br.close();
        urlConnection.disconnect();

        return responseJson.toString();
    }








}
