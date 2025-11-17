package com.example.open_mission.post.controller;

import com.example.open_mission.post.domain.Post;
import com.example.open_mission.post.dto.GetAllPostResponse;
import com.example.open_mission.post.dto.PostCreateDto;
import com.example.open_mission.post.dto.PostResponseDto;
import com.example.open_mission.post.dto.PostUpdateDto;
import com.example.open_mission.post.service.PostService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody @Valid PostCreateDto postCreateDto) {
        Post createdPost = postService.createPost(postCreateDto);

        return ResponseEntity.ok(PostResponseDto.success(HttpStatus.OK, "게시물이 생성되었습니다."));
    }

    @GetMapping()
    public ResponseEntity<List<GetAllPostResponse>> getPost() {
        List<Post> posts = postService.getAllPosts();

        return ResponseEntity.ok(GetAllPostResponse.fromPost(posts));
    }


    @GetMapping("/{postId}")
    public ResponseEntity<GetAllPostResponse> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);

        return ResponseEntity.ok(GetAllPostResponse.fromPost(post));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody @Valid PostUpdateDto postUpdateDto) {
        Post post = postService.updatePost(postId, postUpdateDto);

        return ResponseEntity.ok(PostResponseDto.success(HttpStatus.OK, "게시물이 업데이트되었습니다."));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponseDto> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(PostResponseDto.success(HttpStatus.OK, "게시물 삭제가 완료되었습니다."));
    }
}

