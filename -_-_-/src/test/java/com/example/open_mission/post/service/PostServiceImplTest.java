package com.example.open_mission.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.open_mission.post.dto.PostUpdateDto;
import com.example.open_mission.post.domain.Post;
import com.example.open_mission.post.dto.PostCreateDto;
import com.example.open_mission.post.exception.PostErrorCode;
import com.example.open_mission.post.exception.PostException;
import com.example.open_mission.post.repository.PostJpaRepository;
import com.example.open_mission.user.domain.User;
import com.example.open_mission.user.exception.UserErrorCode;
import com.example.open_mission.user.exception.UserException;
import com.example.open_mission.user.service.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostJpaRepository postJpaRepository;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("게시글 생성")
    void 게시글_생성() {

        Long userId = 1L;
        String title = "게시글 제목 test";
        String content = "게시글 내용 test";
        PostCreateDto createDto = new PostCreateDto(userId, title, content);

        User testAuthor = User.createUser("testUser");
        when(userService.getUserById(userId)).thenReturn(testAuthor);

        Post postToSave = Post.createPost(testAuthor, title, content);
        when(postJpaRepository.save(any(Post.class))).thenReturn(postToSave);

        Post createdPost = postService.createPost(createDto);

        assertThat(createdPost).isNotNull();
        assertThat(createdPost.getAuthor()).isEqualTo(testAuthor);
        assertThat(createdPost.getTitle()).isEqualTo(title);
        assertThat(createdPost.getContent()).isEqualTo(content);


        verify(userService, times(1)).getUserById(userId);
        verify(postJpaRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("사용자 부재를 인한 게시글 생성")
    void 사용자부재_게시글_생성오류() {

        Long userId = 2L;
        String title = "게시글 제목 test";
        String content = "게시글 내용 test";
        PostCreateDto createDto = new PostCreateDto(userId, title, content);


        UserException userNotFoundException = new UserException(
                UserErrorCode.USER_NOT_FOUND
        );
        when(userService.getUserById(userId)).thenThrow(userNotFoundException);


        UserException exception = assertThrows(UserException.class, () -> {
            postService.createPost(createDto);
        });

        assertThat(exception.getErrorCode()).isEqualTo(UserErrorCode.USER_NOT_FOUND);


        verify(userService, times(1)).getUserById(userId);
        verify(postJpaRepository, never()).save(any(Post.class));
    }


    @Test
    @DisplayName("모든 게시글 조회")
    void 모든게시글_조회() {

        User user = User.createUser("user1");
        Post post1 = Post.createPost(user, "게시글 제목 test1", "게시글 내용 test1");
        Post post2 = Post.createPost(user, "게시글 제목 test2", "게시글 내용 test2");
        List<Post> posts = List.of(post1, post2);

        when(postJpaRepository.findAll()).thenReturn(posts);

        List<Post> allPosts = postService.getAllPosts();

        assertThat(allPosts).isNotNull();
        assertThat(allPosts).hasSize(2);
        assertThat(allPosts).containsExactlyInAnyOrder(post1, post2);

        verify(postJpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("게시글 Id를 통한 조회")
    void 게시글_id_조회() {

        Long postId = 1L;
        User user = User.createUser("user");
        Post post = Post.createPost(user, "게시글 제목 test", "게시글 내용 test");

        when(postJpaRepository.findById(postId)).thenReturn(Optional.of(post));

        Post actualPost = postService.getPostById(postId);

        assertThat(actualPost).isNotNull();
        assertThat(actualPost).isEqualTo(post);
        assertThat(actualPost.getTitle()).isEqualTo("게시글 제목 test");

        verify(postJpaRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 ID가 없을 경우 조회")
    void 게시글_id_부재_조회오류() {
        Long postId = 2L;

        when(postJpaRepository.findById(postId)).thenReturn(Optional.empty());
        PostException exception = assertThrows(PostException.class, () -> {
            postService.getPostById(postId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);


        verify(postJpaRepository, times(1)).findById(postId);
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void 게시글_수정() {
        Long postId = 1L;
        PostUpdateDto updateDto = new PostUpdateDto("수정된 제목", "수정된 내용");

        User user = User.createUser("user");
        Post originalPost = Post.createPost(user, "원본 제목", "원본 내용");

        when(postJpaRepository.findById(postId)).thenReturn(Optional.of(originalPost));

        Post updatedPost = postService.updatePost(postId, updateDto);

        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedPost.getContent()).isEqualTo("수정된 내용");

        assertThat(updatedPost).isEqualTo(originalPost);

        verify(postJpaRepository, times(1)).findById(postId);
        verify(postJpaRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 수정 실패")
    void 게시글_수정_실패() {
        Long postId = 2L;
        PostUpdateDto updateDto = new PostUpdateDto("수정된 제목", "수정된 내용");

        when(postJpaRepository.findById(postId)).thenReturn(Optional.empty());

        PostException exception = assertThrows(PostException.class, () -> {
            postService.updatePost(postId, updateDto);
        });


        assertThat(exception.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);

        verify(postJpaRepository, times(1)).findById(postId);
        verify(postJpaRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 삭제")
    void 게시글_삭제() {

        Long postId = 1L;
        User user = User.createUser("user");
        Post existingPost = Post.createPost(user, "제목", "내용");

        when(postJpaRepository.findById(postId)).thenReturn(Optional.of(existingPost));


        doNothing().when(postJpaRepository).delete(existingPost);

        assertDoesNotThrow(() -> {
            postService.deletePost(postId);
        });


        verify(postJpaRepository, times(1)).findById(postId);
        verify(postJpaRepository, times(1)).delete(existingPost);
    }

    @Test
    @DisplayName("게시글 없음으로 인한 삭제 실패")
    void 게시글_없음_삭제_실패() {

        Long postId = 2L;

        when(postJpaRepository.findById(postId)).thenReturn(Optional.empty());

        PostException exception = assertThrows(PostException.class, () -> {
            postService.deletePost(postId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(PostErrorCode.POST_NOT_FOUND);

        verify(postJpaRepository, times(1)).findById(postId);
        verify(postJpaRepository, never()).delete(any(Post.class));
    }

}
