package com.example.open_mission.post.service;

import com.example.open_mission.post.dto.PostUpdateDto;
import com.example.open_mission.post.domain.Post;
import com.example.open_mission.post.dto.PostCreateDto;
import java.util.List;

public interface PostService {
    Post createPost(PostCreateDto postCreateDto);

    List<Post> getAllPosts();

    Post getPostById(Long postId);

    Post updatePost(Long postId, PostUpdateDto postUpdateDto);

    void deletePost(Long postId);
}
