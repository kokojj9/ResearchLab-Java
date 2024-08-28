package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.dao.TradeMapper;
import com.example.researchlab.board.model.vo.Post;
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
    public int saveTradePost(Post post) {
        int result = tradeMapper.saveTradePost(post);

        if (result > 0 && !post.getImageList().isEmpty()) {
            result = tradeMapper.saveImage(post);
        }

        return result;
    }

    @Override
    public Page<Post> selectTradePosts(int page, int size) {
        int offset = page * size;

        List<Post> posts = tradeMapper.findAllPosts(size, offset);
        int total = tradeMapper.countAllPosts();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Post selectPostDetail(int postNo) {
        return tradeMapper.selectPostDetail(postNo);
    }

}
