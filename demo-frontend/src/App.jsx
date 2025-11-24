
import UserList from "./UserList";
import PostBoard from "./PostBoard";
import ChatRoom from "./ChatRoom";
import "./App.css";

function App() {
    return (
        <div>
            <h1>🚀 우아한 테크코스 오픈 미션</h1>

            <ChatRoom />

            <div className="main-container">
                <div className="user-section">
                    <UserList />
                </div>
                <div className="board-section">
                    <PostBoard />
                </div>
            </div>
        </div>
    );
}

export default App;