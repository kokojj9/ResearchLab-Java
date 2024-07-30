package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.dao.TradeMapper;
import com.example.researchlab.board.model.vo.TradePost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradeBoardServiceImpl implements TradeBoardService{

    private final TradeMapper tradeMapper;
    @Override
    @Transactional
    public int saveTradePost(TradePost tradePost) {
        int result = tradeMapper.saveTradePost(tradePost);

        if(result > 0) {
            result = tradeMapper.saveImage(tradePost.getImageList());
        }

        return result;
    }
}
