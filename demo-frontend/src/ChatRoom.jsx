import { useEffect, useState, useRef } from 'react';
import * as StompJs from '@stomp/stompjs';
import SockJS from 'sockjs-client';

function ChatRoom() {
    const client = useRef(null);
    const [chatMessages, setChatMessages] = useState([]);
    const [message, setMessage] = useState('');
    const [sender, setSender] = useState(''); // 내 닉네임

    useEffect(() => {
        connect();
        return () => disconnect();
    }, []);

    const connect = () => {
        client.current = new StompJs.Client({
            webSocketFactory: () => new SockJS('http://localhost:8080/ws-stomp'),
            onConnect: () => {
                subscribe();
            },
        });
        client.current.activate();
    };

    const subscribe = () => {
        client.current.subscribe('/sub/chat/room', (msg) => {
            const newMessage = JSON.parse(msg.body);
            setChatMessages((prev) => [...prev, newMessage]);
        });
    };

    const sendMessage = () => {
        if (!message || !sender) {
            alert("닉네임과 내용을 입력하세요.");
            return;
        }

        client.current.publish({
            destination: '/pub/chat/message',
            body: JSON.stringify({
                sender: sender,
                content: message,
            }),
        });
        setMessage('');
    };

    const disconnect = () => {
        if (client.current) {
            client.current.deactivate();
        }
    };

    return (
        <div style={{
            border: '2px solid #fae100',
            padding: '20px',
            margin: '20px',
            borderRadius: '10px',
            backgroundColor: '#b2c7d9'
        }}>
            <h2 style={{ color: '#333', margin: '0 0 15px 0' }}>💬 실시간 채팅방</h2>

            <div style={{
                height: '400px',
                overflowY: 'auto',
                padding: '10px',
                display: 'flex',
                flexDirection: 'column',
                gap: '10px'
            }}>
                {chatMessages.map((msg, index) => {
                    const isMe = msg.sender === sender;

                    return (
                        <div key={index} style={{
                            display: 'flex',
                            justifyContent: isMe ? 'flex-end' : 'flex-start',
                        }}>
                            {/* 메시지 내용 */}
                            <div style={{
                                maxWidth: '70%',
                                padding: '10px 15px',
                                borderRadius: '10px',
                                backgroundColor: isMe ? '#fae100' : '#ffffff',
                                color: '#000',
                                boxShadow: '0 1px 2px rgba(0,0,0,0.1)',
                                wordBreak: 'break-word'
                            }}>

                                {!isMe && (
                                    <div style={{ fontSize: '0.8rem', color: '#666', marginBottom: '4px' }}>
                                        {msg.sender}
                                    </div>
                                )}
                                {msg.content}
                            </div>
                        </div>
                    );
                })}
            </div>

            <div style={{ display: 'flex', gap: '5px', marginTop: '10px' }}>
                <input
                    placeholder="내 닉네임"
                    value={sender}
                    onChange={(e) => setSender(e.target.value)}
                    style={{ width: '80px', padding: '10px', borderRadius: '5px', border: '1px solid #ddd' }}
                />
                <input
                    placeholder="메시지 입력..."
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && sendMessage()}
                    style={{ flex: 1, padding: '10px', borderRadius: '5px', border: '1px solid #ddd' }}
                />
                <button
                    onClick={sendMessage}
                    style={{
                        backgroundColor: '#fae100',
                        border: 'none',
                        padding: '10px 20px',
                        borderRadius: '5px',
                        cursor: 'pointer',
                        fontWeight: 'bold'
                    }}
                >
                    전송
                </button>
            </div>
        </div>
    );
}

export default ChatRoom;