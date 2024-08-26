package com.example.researchlab.board.model.dao;

import com.example.researchlab.board.model.vo.PostImage;
import com.example.researchlab.board.model.vo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface TradeMapper {
    int saveTradePost(Post tradePost);

    int saveImage(Post tradePost);

    List<Post> findAllPosts(@Param("pageSize") int pageSize, @Param("offset") int offset);

    int countAllPosts();
}
