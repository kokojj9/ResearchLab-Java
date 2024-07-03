package com.example.researchlab.investment.model.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;

public interface InvestmentService {

    String getStockInfo(String stockName) throws IOException;
    HttpURLConnection getHttpURLConnection(String encodedStockName) throws IOException;


}
