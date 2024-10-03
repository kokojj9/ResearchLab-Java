package com.example.researchlab.board.controller;

import com.example.researchlab.board.model.service.StrategylabService;
import com.example.researchlab.board.model.vo.Post;
import com.example.researchlab.common.model.vo.ResponseData;
import com.example.researchlab.template.ResponseTemplate;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StrategylabControllerTest {

    @InjectMocks
    private StrategylabController strategylabController;

    @Mock
    private StrategylabService strategylabService;

    @Mock
    private ResponseTemplate responseTemplate;

    @Test
    public void selectTradePosts() {
        // given
        Post post1 = new Post(1, "title1", "content1");
        Post post2 = new Post(2, "title2", "content2");
        Post post3 = new Post(3, "title3", "content3");

        List<Post> posts = Arrays.asList(post1, post2, post3);
        Page<Post> page = new PageImpl<>(posts, PageRequest.of(0, 15), posts.size());

        // when
        when(strategylabController.selectTradePosts(0, 15)).thenReturn(page);
        Page<Post> result = strategylabController.selectTradePosts(0, 15);

        // then
        assertEquals(3, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(posts, result.getContent());
    }

    @Test
    public void saveTradePost() throws IOException {
        // given
        Post post = new Post(1, "title1", "content1");
        MultipartFile images = Mockito.mock(MultipartFile.class);
        List<MultipartFile> imageList = Collections.singletonList(images);

        ResponseEntity<ResponseData<Object>> success = ResponseEntity.ok().build();
        when(responseTemplate.success("작성 성공", null, HttpStatus.OK)).thenReturn(success);
        // when
        ResponseEntity<ResponseData<Object>> result = strategylabController.saveTradePost(post, imageList);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deletePost() throws IOException {
        // given
        int postNo = 1;
        String memberId = "testMember";

        ResponseEntity<ResponseData<Object>> success = ResponseEntity.ok().build();
        when(responseTemplate.success("삭제 성공", null, HttpStatus.OK)).thenReturn(success);
        when(strategylabService.deletePost(postNo, memberId)).thenReturn(1);

        // when
        ResponseEntity<ResponseData<Object>> result = strategylabController.deletePost(postNo, memberId);

        // then
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

}
