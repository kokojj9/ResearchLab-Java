package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.vo.TradePost;
import com.example.researchlab.common.model.vo.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TradeBoardService {
    int saveTradePost(TradePost tradePost);

    Page<TradePost> selectTradePosts(int page, int size);
}
