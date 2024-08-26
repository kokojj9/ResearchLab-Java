package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.vo.Post;
import org.springframework.data.domain.Page;

public interface TradeBoardService {
    int saveTradePost(Post tradePost);

    Page<Post> selectTradePosts(int page, int size);
}
