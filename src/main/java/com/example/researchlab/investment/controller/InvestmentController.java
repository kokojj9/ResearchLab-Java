package com.example.researchlab.investment.controller;

import com.example.researchlab.common.model.vo.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@RestController
@RequestMapping("/investment")
@RequiredArgsConstructor
public class InvestmentController {

    @Value("${SERVICE_KEY}")
    private String SERVICE_KEY;

    @ResponseBody
    @PostMapping(value = "/stock", produces = "application/json; charset=UTF-8")
    public String getStockInfo(String stockName) throws IOException {
        StringBuilder url = new StringBuilder();

        url.append("https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo");
        url.append("?serviceKey").append(SERVICE_KEY);
        url.append("&numOfRows=10");
        url.append("&pageNo=1");
        url.append("&likeItmsNm=").append(URLEncoder.encode(stockName, "UTF-8"));
        url.append("&resultType=json");

        URL requestUrl = new URL(url.toString());

        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String responseJson = br.readLine();

        br.close();
        urlConnection.disconnect();

        return responseJson;
    }








}
