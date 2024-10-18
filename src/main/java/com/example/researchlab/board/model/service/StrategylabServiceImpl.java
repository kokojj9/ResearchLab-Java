package com.example.researchlab.board.model.service;

import com.example.researchlab.board.controller.StrategylabController;
import com.example.researchlab.board.model.dao.TradeMapper;
import com.example.researchlab.board.model.vo.Post;
import com.example.researchlab.board.model.vo.PostImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategylabServiceImpl implements StrategylabService {

    private final BoardFileService boardFileService;
    private final TradeMapper tradeMapper;

    @Override
    public Page<Post> selectTradePosts(int page, int size) {
        return getPosts(page, size, null);
    }

    @Override
    public Page<Post> selectMyPosts(int page, int size, String memberId) {
        return getPosts(page, size, memberId);
    }

    private PageImpl<Post> getPosts(int page, int size, String memberId) {
        int offset = page * size;

        List<Post> posts = tradeMapper.findPosts(size, offset, memberId);
        int total = tradeMapper.countAllPosts();

        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return new PageImpl<>(posts, pageable, total);
    }

    @Transactional
    @Override
    public int saveTradePost(Post post, List<MultipartFile> imageList) throws IOException {
        int result = tradeMapper.saveTradePost(post);

        if (result > 0 && imageList != null) {
            List<PostImage> images = setImages(imageList);
            post.setImageList(images);

            result = tradeMapper.saveImage(post);
        }

        return result;
    }

    @Transactional
    @Override
    public int updatePost(int postNo, Post post, List<MultipartFile> imageList) throws IOException {
        int result = tradeMapper.updatePost(post);

        if (result > 0 && imageList != null) {
            List<PostImage> images = setImages(imageList);
            post.setImageList(images);

            tradeMapper.deleteImage(postNo);
            // deleteFiles(images);
            result = tradeMapper.saveImage(post);
        }

        return result;
    }

    private List<PostImage> setImages(List<MultipartFile> images) throws IOException {
        List<PostImage> imageList = new ArrayList<>();

        for (MultipartFile image : images) {
            try {
                String storedFileName = boardFileService.saveFile(image);
                PostImage postImage = new PostImage();
                postImage.setOriginalName(image.getOriginalFilename());
                postImage.setStoredName(storedFileName);
                imageList.add(postImage);
            } catch (IOException e) {
                log.error("이미지 저장 오류: {}", image.getOriginalFilename());
                throw e;
            }
        }

        return imageList;
    }

    @Override
    public Post selectPostDetail(int postNo) {
        tradeMapper.increaseViewCount(postNo);
        return tradeMapper.selectPostDetail(postNo);
    }

    @Transactional
    @Override
    public int deletePost(int postNo, String memberId) throws IOException {
        Post post = tradeMapper.selectPostDetail(postNo);

        if (post == null) {
            return 0;
        }

        tradeMapper.deletePost(postNo, memberId);
        tradeMapper.deleteImage(postNo);
        boardFileService.deleteFiles(post.getImageList());

        return 1;
    }
}
