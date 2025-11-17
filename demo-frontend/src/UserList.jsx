// src/UserList.jsx
import { useEffect, useState } from 'react';
import api from './api';

function UserList() {
    const [users, setUsers] = useState([]);
    const [userName, setUserName] = useState('');

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const response = await api.get('/users');
            setUsers(response.data.data);
        } catch (error) {
            console.error("목록 가져오기 실패:", error);
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
            alert("등록 실패! (이미 있는 이름일 수도?)");
        }
    };

    return (
        <div style={{ border: '2px solid blue', padding: '20px', margin: '20px' }}>
            <h2>👤 유저 관리</h2>

            <div style={{ marginBottom: '10px' }}>
                <input
                    placeholder="이름을 입력하세요"
                    value={userName}
                    onChange={(e) => setUserName(e.target.value)}
                />
                <button onClick={createUser} style={{ marginLeft: '5px' }}>등록</button>
            </div>

            <ul>
                {users.map(user => (
                    <li key={user.id}>
                        <b>{user.id}번:</b> {user.userName}
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default UserList;