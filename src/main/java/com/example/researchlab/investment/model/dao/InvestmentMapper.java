package com.example.researchlab.investment.model.dao;

import com.example.researchlab.investment.model.vo.MyStockList;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface InvestmentMapper {
    int saveStockList(HashMap<String, Object> userStockList);

    int saveStockListItem(HashMap<String, Object> userStockList);

    List<MyStockList> getStockList(String memberNo);
}
