package com.example.researchlab.board.model.dao;

import com.example.researchlab.board.model.vo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TradeMapper {
    int saveTradePost(Post tradePost);

    int saveImage(Post tradePost);

    List<Post> findPosts(@Param("pageSize") int pageSize, @Param("offset") int offset, @Param("memberId") String memberId);

    int countAllPosts();

    Post selectPostDetail(int postNo);

    void deletePost(int postNo, String memberId);

    void deleteImage(int postNo);

    int updatePost(Post post);
}
