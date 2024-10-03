package com.example.researchlab.board.model.service;

import com.example.researchlab.board.controller.StrategylabController;
import com.example.researchlab.board.model.dao.TradeMapper;
import com.example.researchlab.board.model.vo.Post;
import com.example.researchlab.board.model.vo.PostImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final TradeMapper tradeMapper;
    private final BoardFileService boardFileService;
    private static final Logger logger = LoggerFactory.getLogger(StrategylabController.class);

    @Override
    public Page<Post> selectTradePosts(int page, int size) {
        return getPosts(page, size, null);
    }

    @Override
    public Page<Post> selectMyPosts(int page, int size, String memberId) {
        return getPosts(page, size, memberId);
    }

    @Transactional
    @Override
    public int updatePost(int postNo, Post post, List<MultipartFile> images) throws IOException {
        int result = tradeMapper.updatePost(post);

        if(result > 0 && !post.getImageList().isEmpty()){
            List<PostImage> imageList = setImages(images);
            post.setImageList(imageList);

            result = tradeMapper.saveImage(post);
            tradeMapper.deleteImage(postNo);
            deleteFiles(imageList);
        }

        return result;
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
    public int saveTradePost(Post post, List<MultipartFile> images) throws IOException {
        int result = tradeMapper.saveTradePost(post);

        if (result > 0 && !post.getImageList().isEmpty()) {
            List<PostImage> imageList = setImages(images);
            post.setImageList(imageList);

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
                logger.error("이미지 저장 오류: {}", image.getOriginalFilename());
                throw e; // 예외를 던져 트랜잭션 롤백 처리
            }
        }

        return imageList;
    }

    @Override
    public Post selectPostDetail(int postNo) {
        return tradeMapper.selectPostDetail(postNo);
    }

    @Transactional
    @Override
    public int deletePost(int postNo, String memberId) throws IOException {
        Post post = tradeMapper.selectPostDetail(postNo);

        if (post == null){
           return 0;
        }

        tradeMapper.deletePost(postNo, memberId);
        tradeMapper.deleteImage(postNo);
        deleteFiles(post.getImageList());

        return 1;
    }

    private void deleteFiles(List<PostImage> imageList) throws IOException {
        for(PostImage file: imageList){
            String fileName = file.getStoredName();
            try {
                boardFileService.deleteFile(fileName);
            } catch (IOException e) {
                logger.error("파일 삭제 실패: {}", fileName);
            }
        }
    }


}
