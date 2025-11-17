package com.example.open_mission.post.service;

import com.example.open_mission.post.dto.PostUpdateDto;
import com.example.open_mission.post.domain.Post;
import com.example.open_mission.post.dto.PostCreateDto;
import com.example.open_mission.post.exception.PostErrorCode;
import com.example.open_mission.post.exception.PostException;
import com.example.open_mission.post.repository.PostJpaRepository;
import com.example.open_mission.user.domain.User;
import com.example.open_mission.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostJpaRepository postJpaRepository;
    private final UserService userService;

    @Transactional
    @Override
    public Post createPost(PostCreateDto postCreateDto) {
        User author = userService.getUserById(postCreateDto.userId());
        Post post = Post.createPost(author, postCreateDto.title(), postCreateDto.content());

        return postJpaRepository.save(post);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Post> getAllPosts() {
        return postJpaRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Post getPostById(Long postId) {
        return postJpaRepository.findById(postId)
                .orElseThrow(() -> new PostException(PostErrorCode.POST_NOT_FOUND));
    }

    @Transactional
    @Override
    public Post updatePost(Long postId, PostUpdateDto postUpdateDto) {
        Post post = getPostById(postId);

        Post updatedPost = post.updatePost(postUpdateDto.title(), postUpdateDto.content());

        return updatedPost;
    }

    @Transactional
    @Override
    public void deletePost(Long postId) {
        Post post = getPostById(postId);

        postJpaRepository.delete(post);
    }
}
