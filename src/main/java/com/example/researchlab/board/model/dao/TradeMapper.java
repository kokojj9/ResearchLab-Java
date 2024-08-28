package com.example.researchlab.board.model.dao;

import com.example.researchlab.board.model.vo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TradeMapper {
    int saveTradePost(Post tradePost);

    int saveImage(Post tradePost);

    List<Post> findAllPosts(@Param("pageSize") int pageSize, @Param("offset") int offset);

    int countAllPosts();

    Post selectPostDetail(int postNo);
}
