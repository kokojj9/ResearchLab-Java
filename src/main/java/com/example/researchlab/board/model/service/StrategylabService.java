package com.example.researchlab.board.model.service;

import com.example.researchlab.board.model.vo.Post;
import com.example.researchlab.common.model.vo.ResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StrategylabService {
    Post selectPostDetail(int postNo);

    int saveTradePost(Post tradePost, List<MultipartFile> images) throws IOException;

    Page<Post> selectTradePosts(int page, int size);

    int deletePost(int postNo, String memberId) throws IOException;

    Page<Post> selectMyPosts(int page, int size, String memberId);

    int updatePost(int postNo, Post post, List<MultipartFile> images) throws IOException;
}
