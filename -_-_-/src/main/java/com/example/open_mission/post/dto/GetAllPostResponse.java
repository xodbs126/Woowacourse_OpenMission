package com.example.open_mission.post.dto;

import com.example.open_mission.post.domain.Post;
import java.util.List;

public record GetAllPostResponse(Long id, Long userId, String userName, String title, String content) {

    public static List<GetAllPostResponse> fromPost(List<Post> posts) {
        return posts.stream()
                .map(post -> new GetAllPostResponse(
                        post.getId(),
                        post.getAuthor().getId(),
                        post.getAuthor().getUserName(),
                        post.getTitle(),
                        post.getContent()))
                .toList();
    }

    public static GetAllPostResponse fromPost(Post post) {
        return new GetAllPostResponse(
                post.getId(),
                post.getAuthor().getId(),
                post.getAuthor().getUserName(),
                post.getTitle(),
                post.getContent());
    }
}
