package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.vo.Post;
import com.example.researchlab.common.model.vo.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StrategylabService {
    Post selectPostDetail(int postNo);

    int saveTradePost(Post tradePost);

    Page<Post> selectTradePosts(int page, int size);

    boolean deletePost(int postNo, String memberId);

    List<Post> selectMyPosts(String memberId);
}
