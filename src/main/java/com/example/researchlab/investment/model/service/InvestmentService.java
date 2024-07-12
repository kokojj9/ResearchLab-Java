package com.example.researchlab.investment.model.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;

public interface InvestmentService {

    String getStockInfo(String stockName) throws IOException;
    HttpURLConnection getHttpURLConnection(String encodedStockName) throws IOException;
    int saveStockList(HashMap<String, Object> stocklist);
}
