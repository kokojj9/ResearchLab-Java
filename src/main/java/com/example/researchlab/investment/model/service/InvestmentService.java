package com.example.researchlab.investment.model.service;

import com.example.researchlab.investment.model.vo.MyStockList;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;

public interface InvestmentService {

    String getStockInfo(String stockName) throws IOException;
    HttpURLConnection getHttpURLConnection(String encodedStockName) throws IOException;
    List<MyStockList> saveStockList(HashMap<String, Object> stocklist);
}
