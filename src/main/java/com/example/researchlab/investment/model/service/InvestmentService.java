package com.example.researchlab.investment.model.service;

import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.investment.model.vo.MyStockList;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

public interface InvestmentService {

    String getStockInfo(String stockName) throws IOException;
    HttpURLConnection getHttpURLConnection(String encodedStockName) throws IOException;
    int saveStockList(MyStockList stocklist);
}
