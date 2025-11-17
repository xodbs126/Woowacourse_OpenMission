package com.example.open_mission.post.domain;

import com.example.open_mission.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT", length = 1000)
    private String content;

    @Builder
    public Post(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    protected Post() {
    }

    public static Post createPost(User author, String title, String content) {
        return Post.builder()
                .author(author)
                .title(title)
                .content(content)
                .build();
    }

    public Post updatePost(String title, String content) {
        this.title = title;
        this.content = content;
        return this;
    }
}

