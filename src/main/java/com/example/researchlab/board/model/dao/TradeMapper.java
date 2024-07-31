package com.example.researchlab.board.model.dao;

import com.example.researchlab.board.model.vo.PostImage;
import com.example.researchlab.board.model.vo.TradePost;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TradeMapper {
    int saveTradePost(TradePost tradePost);

    int saveImage(TradePost tradePost);
}
