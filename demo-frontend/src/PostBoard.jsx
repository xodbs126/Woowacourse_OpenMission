
import { useEffect, useState } from 'react';
import api from './api';

function PostBoard() {
    const [posts, setPosts] = useState([]);


    const [authorId, setAuthorId] = useState('');
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');


    const [editingPostId, setEditingPostId] = useState(null);
    const [editTitle, setEditTitle] = useState('');
    const [editContent, setEditContent] = useState('');

    const fetchPosts = async () => {
        try {
            const response = await api.get('/posts');
            setPosts(response.data.data || response.data);
        } catch (error) {
            console.error("게시글 조회 실패:", error);
        }
    };

    const createPost = async () => {
        if (!authorId || !title || !content) {
            alert("작성자 ID, 제목, 내용을 모두 입력해주세요.");
            return;
        }
        try {
            await api.post('/posts', {
                userId: Number(authorId),
                title: title,
                content: content
            });
            alert("게시글 등록 성공!");
            setTitle('');
            setContent('');
            fetchPosts();
        } catch (error) {
            alert("등록 실패! (존재하지 않는 유저 ID일 수 있습니다)");
        }
    };


    const deletePost = async (postId) => {
        if(!window.confirm("정말 삭제하시겠습니까?")) return;
        try {
            await api.delete(`/posts/${postId}`);
            alert("삭제되었습니다.");
            fetchPosts();
        } catch (error) {
            alert("삭제 실패!");
        }
    }

    const startEdit = (post) => {
        setEditingPostId(post.id);
        setEditTitle(post.title);
        setEditContent(post.content);
    }


    const cancelEdit = () => {
        setEditingPostId(null);
    }

    const saveEdit = async (postId) => {
        try {
            await api.patch(`/posts/${postId}`, {
                title: editTitle,
                content: editContent
            });
            alert("게시글이 수정되었습니다.");
            setEditingPostId(null);
            fetchPosts();
        } catch (error) {
            alert("수정 실패!");
        }
    }

    useEffect(() => {
        fetchPosts();
    }, []);

    return (
        <div className="section-card">
            <h2>📝 게시판</h2>

            <div className="input-column">
                <div className="input-row">
                    <input
                        type="number"
                        className="common-input"
                        placeholder="ID(숫자)"
                        value={authorId}
                        onChange={(e) => setAuthorId(e.target.value)}
                        style={{ width: '80px' }}
                    />
                    <input
                        className="common-input"
                        placeholder="제목을 입력하세요"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        style={{ flex: 1 }}
                    />
                </div>
                <textarea
                    className="common-input"
                    placeholder="내용을 입력하세요"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    rows="3"
                />
                <button onClick={createPost} className="btn-green full-width">게시글 등록</button>
            </div>

            <hr className="divider"/>

            <div className="post-list-container">
                {Array.isArray(posts) && posts.map(post => (
                    <div key={post.id} className="post-card">
                        {editingPostId === post.id ? (
                            <div className="edit-column">
                                <input
                                    className="common-input"
                                    value={editTitle}
                                    onChange={(e) => setEditTitle(e.target.value)}
                                />
                                <textarea
                                    className="common-input"
                                    value={editContent}
                                    onChange={(e) => setEditContent(e.target.value)}
                                    rows="3"
                                />
                                <div className="btn-group right">
                                    <button onClick={() => saveEdit(post.id)} className="btn-green small">저장</button>
                                    <button onClick={cancelEdit} className="btn-gray small">취소</button>
                                </div>
                            </div>
                        ) : (

                            <>
                                <h3 className="post-title">{post.title}</h3>
                                <p className="post-content">{post.content}</p>
                                <div className="post-footer">
                                    <span className="post-author">🖊️ {post.userName}</span>
                                    <div className="btn-group">
                                        <button onClick={() => startEdit(post)} className="btn-orange small">수정</button>
                                        <button onClick={() => deletePost(post.id)} className="btn-red small">삭제</button>
                                    </div>
                                </div>
                            </>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}

export default PostBoard;