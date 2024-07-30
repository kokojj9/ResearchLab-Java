package com.example.researchlab.board.model.dao;

import com.example.researchlab.board.model.vo.TradePost;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TradeMapper {
    int saveTradePost(TradePost tradePost);
}
