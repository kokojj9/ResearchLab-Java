package com.example.researchlab.investment.model.service;

import com.example.researchlab.investment.model.dao.InvestmentMapper;
import com.example.researchlab.investment.model.vo.MyStockList;
import com.example.researchlab.member.controller.MemberController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
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
    @Transactional
    public int saveStockList(HashMap<String, Object> userStockList) {

        System.out.println(userStockList.get("stockList"));
        int isList = investmentMapper.saveStockList(userStockList);
        System.out.println(userStockList.get("stockList"));

        int isItem = 0;
        if (isList > 0) {
            isItem = investmentMapper.saveStockListItem(userStockList);
        }

        return isList * isItem;
    }
}
