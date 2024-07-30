package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.dao.TradeMapper;
import com.example.researchlab.board.model.vo.TradePost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TradeBoardServiceImpl implements TradeBoardService{

    private final TradeMapper tradeMapper;
    @Override
    public int saveTradePost(TradePost tradePost) {
        return tradeMapper.saveTradePost(tradePost);
    }
}
