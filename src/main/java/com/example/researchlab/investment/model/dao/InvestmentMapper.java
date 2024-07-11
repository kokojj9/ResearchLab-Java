package com.example.researchlab.investment.model.dao;

import com.example.researchlab.investment.model.vo.MyStockList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvestmentMapper {
    int saveStockList(MyStockList stockList);
}
