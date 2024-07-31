package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.dao.TradeMapper;
import com.example.researchlab.board.model.vo.TradePost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeBoardServiceImpl implements TradeBoardService {

    private final TradeMapper tradeMapper;

    @Override
    @Transactional
    public int saveTradePost(TradePost tradePost) {
        int result = tradeMapper.saveTradePost(tradePost);

        if (result > 0 && !tradePost.getImageList().isEmpty()) {
            result = tradeMapper.saveImage(tradePost);
        }

        return result;
    }

    @Override
    public Page<TradePost> selectTradePosts(int page, int size) {
        int offset = page * size;

        List<TradePost> posts = tradeMapper.findAllPosts(size, offset);
        int total = tradeMapper.countAllPosts();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return new PageImpl<>(posts, pageable, total);
    }


}
