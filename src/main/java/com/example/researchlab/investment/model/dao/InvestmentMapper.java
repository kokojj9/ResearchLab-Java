package com.example.researchlab.investment.model.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;

@Mapper
public interface InvestmentMapper {
    int saveStockList(HashMap<String, Object> userStockList);

    int saveStockListItem(HashMap<String, Object> userStockList);
}
