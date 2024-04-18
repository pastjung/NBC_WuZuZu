import React, {useEffect, useState} from "react";
import ChattingRoomInfoList from "./ChattingRoomInfoList";
import {Modal} from "@mui/joy";
import ChattingRoom from "./ChattingRoom";
import * as StompJs from "@stomp/stompjs";
import {getRoomMessages} from "../api/MessageApi";
import CreateChattingRoom from "./CreateChattingRoom";
import {createChatRoom} from "../api/ChatRoomApi";

export const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '20%',
    height: '80%',
    bgcolor: "#ADD8E6"
};

export const listStyle = {
    overflowY: 'scroll',
    mx: 'calc(-1 * var(--ModalDialog-padding))',
    px: 'var(--ModalDialog-padding)',
    mb: "50px"
}

export const listStyle_mt = {
    overflowY: 'scroll',
    mx: 'calc(-1 * var(--ModalDialog-padding))',
    px: 'var(--ModalDialog-padding)',
    mt: "50px",
    mb: "50px"
}

export const chattingAppState = {
    채팅방조회중: 1,
    채팅방생성중: 2,
    채팅방입장: 3
};

function ChattingApp({open, handleClose}) {

    const [stompClient, setStompClient] = useState(null);
    const [state, setState] = useState(chattingAppState.채팅방조회중);
    const [currentRoom, setCurrentRoom] = useState(null);
    const [roomMessages, setRoomMessages] = useState([]);
    const [receiveMessage, setReceiveMessage] = useState(null);

    const handleEnterClick = (room) => {
        if (room) {
            setCurrentRoom(room);
        }
    }

    useEffect(() => {
        if (currentRoom !== null && stompClient === null) {
            const client = new StompJs.Client({
                brokerURL: `ws://${window.location.hostname}:8080/gs-guide-websocket`,
                onConnect: async message => {
                    console.log(message);
                    client.subscribe(
                        `/topic/chat-rooms/${currentRoom.chatRoomId}`,
                        onMessageReceived);
                    try {
                        const response = await getRoomMessages(
                            currentRoom.chatRoomId);
                        setRoomMessages(response.data.data);
                        setState(chattingAppState.채팅방입장);
                    } catch (error) {
                        alert("채팅방 정보 로드에 실패했습니다.");
                    }
                },
                onWebSocketError: error => {
                    alert('웹 소켓 에러');
                    setCurrentRoom(null);
                },
                onStompError: frame => {
                    alert('웹 소켓 에러');
                    setCurrentRoom(-1);
                },
            });

            client.activate();
            setStompClient(client);
        } else if (currentRoom === null && stompClient) {
            stompClient.deactivate();
            setStompClient(null);
        }
    }, [currentRoom]);

    const onMessageReceived = payload => {
        const message = JSON.parse(payload.body);
        setReceiveMessage(message);
    };

    const handleBackClick = () => {
        setState(chattingAppState.채팅방조회중);
        setCurrentRoom(null);
    }

    const handleCreateChatRoomClick = () => {
        setState(chattingAppState.채팅방생성중);
    }

    const onCreate = async (newRoom) => {
        try {
            const response = await createChatRoom(newRoom);
            alert("[" + response.data.chatRoomName + "] 채팅방 생성 완료!");
        } catch (error) {
            alert("채팅방 생성 실패");
        } finally {
            setState(chattingAppState.채팅방조회중);
        }
    }

    function switchUI() {
        switch (state) {
            case chattingAppState.채팅방조회중 :
                return <ChattingRoomInfoList
                    handleClose={handleClose}
                    handleEnterClick={handleEnterClick}
                    handleCreateChatRoomClick={handleCreateChatRoomClick}
                />
            case chattingAppState.채팅방생성중 :
                return <CreateChattingRoom
                    onCreate={onCreate}
                    handleBackClick={handleBackClick}
                />
            case chattingAppState.채팅방입장 :
                return <ChattingRoom
                    room={currentRoom}
                    onBackClick={handleBackClick}
                    stompClient={stompClient}
                    handleClose={handleClose}
                    receiveMessage={receiveMessage}
                    messages={roomMessages}
                />
        }
    }

    return (
        <Modal
            open={open}
            onClose={handleClose}
        >
            <div>
                {switchUI()}
            </div>
        </Modal>
    );
}

export default ChattingApp;