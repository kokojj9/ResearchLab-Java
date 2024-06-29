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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

@RestController
@RequestMapping("/investment")
@RequiredArgsConstructor
public class InvestmentController {

    @Value("${SERVICE_KEY}")
    private String SERVICE_KEY;

    @GetMapping(value = "/findStock", produces = "application/json; charset=UTF-8")
    public String getStockInfo(@RequestParam String stockName) throws IOException {
        // 2일전 or 월,화 일 경우에는 금요일로 조회하도록
        // 공공데이터 api는 2일전 종가로 조회 되기 때문에 추후에 고도화를 통해 바꿀 예정
        String encodedStockName = URLEncoder.encode(stockName, "UTF-8");

        HttpURLConnection urlConnection = getHttpURLConnection(encodedStockName);
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

    private HttpURLConnection getHttpURLConnection(String encodedStockName) throws IOException {
        String url = "https://apis.data.go.kr/1160100/service/GetStockSecuritiesInfoService/getStockPriceInfo"
                   + "?serviceKey=" + SERVICE_KEY
                   + "&numOfRows=10"
                   + "&pageNo=1"
                   + "&likeItmsNm=" + encodedStockName
                   + "&resultType=json";

        URL requestUrl = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection) requestUrl.openConnection();
        urlConnection.setRequestMethod("GET");
        return urlConnection;
    }


}
