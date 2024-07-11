package com.example.researchlab.investment.model.service;

import com.example.researchlab.investment.model.dao.InvestmentMapper;
import com.example.researchlab.investment.model.vo.MyStockList;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {

    @Value("${SERVICE_KEY}")
    private String SERVICE_KEY;

    private final InvestmentMapper investmentMapper;

    @Override
    public String getStockInfo(String encodedStockName) throws IOException {
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

    @Override
    public HttpURLConnection getHttpURLConnection(String encodedStockName) throws IOException {
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

    @Override
    public int saveStockList(MyStockList stockList) {
        return investmentMapper.saveStockList(stockList);
    }
}
