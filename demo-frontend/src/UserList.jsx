
import { useEffect, useState } from 'react';
import api from './api';

function UserList() {
    const [users, setUsers] = useState([]);
    const [userName, setUserName] = useState('');

    const [editingId, setEditingId] = useState(null);
    const [editName, setEditName] = useState('');

    const fetchUsers = async () => {
        try {
            const response = await api.get('/users');
            setUsers(response.data.data || response.data);
        } catch (error) {
            console.error("실패:", error);
        }
    };

    const createUser = async () => {
        if (!userName) return;
        try {
            await api.post('/users', { userName: userName });
            alert("등록 성공!");
            setUserName('');
            fetchUsers();
        } catch (error) {
            alert("등록 실패!");
        }
    };

    const deleteUser = async (userId) => {
        if (!window.confirm("삭제하시겠습니까?")) return;
        try {
            await api.delete(`/users/${userId}`);
            fetchUsers();
        } catch (error) {
            alert("삭제 실패!");
        }
    };

    const startEdit = (user) => {
        setEditingId(user.id);
        setEditName(user.userName);
    };

    const saveEdit = async (userId) => {
        try {
            await api.patch(`/users/${userId}`, { userName: editName });
            setEditingId(null);
            fetchUsers();
        } catch (error) {
            alert("수정 실패!");
        }
    };

    useEffect(() => { fetchUsers(); }, []);

    return (
        <div className="section-card">
            <h2>유저 관리</h2>

            <div className="input-row">
                <input
                    className="common-input"
                    placeholder="이름 입력"
                    value={userName}
                    onChange={(e) => setUserName(e.target.value)}
                />
                <button onClick={createUser} className="btn-black">등록</button>
            </div>

            <ul className="user-list">
                {Array.isArray(users) && users.map(user => (
                    <li key={user.id} className="user-item">
                        {editingId === user.id ? (
                            <div className="edit-row">
                                <input
                                    className="common-input small"
                                    value={editName}
                                    onChange={(e) => setEditName(e.target.value)}
                                />
                                <div className="btn-group">
                                    <button onClick={() => saveEdit(user.id)} className="btn-green small">저장</button>
                                    <button onClick={() => setEditingId(null)} className="btn-gray small">취소</button>
                                </div>
                            </div>
                        ) : (
                            <div className="view-row">
                                <div className="user-info">
                                    <span className="user-id">#{user.id}</span>
                                    <span className="user-name">{user.userName}</span>
                                </div>
                                <div className="btn-group">
                                    <button onClick={() => startEdit(user)} className="btn-orange small">수정</button>
                                    <button onClick={() => deleteUser(user.id)} className="btn-red small">삭제</button>
                                </div>
                            </div>
                        )}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default UserList;