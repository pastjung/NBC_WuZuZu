import React, {useEffect, useRef, useState} from "react";
import {ModalDialog} from "@mui/joy";
import {listStyle_mt, style} from "./ChattingApp";
import {AppBar, Box, IconButton, List, TextField, Toolbar} from "@mui/material";
import ArrowBackIcon from "@mui/icons-material/ArrowBack";
import SearchIcon from "@mui/icons-material/Search";
import MenuIcon from "@mui/icons-material/Menu";
import SendIcon from "@mui/icons-material/Send";
import MyMessage from "./MyMessage";
import OtherMessage from "./OtherMessage";

export default function ChattingRoom({
    room,
    onBackClick,
    messages,
    stompClient,
    receiveMessage,
    handleClose
}) {

    const myUserId = Number(localStorage.getItem("userId"));
    const [chatMessages, setChatMessages] = useState(messages);
    const [messageInput, setMessageInput] = useState("");
    const listRef = useRef(null);
    const [isAtBottom, setIsAtBottom] = useState(true);

    useEffect(() => {
        if (isAtBottom) {
            scrollToBottom();
        }
    }, [chatMessages, isAtBottom]);

    const scrollToBottom = () => {
        if (listRef.current) {
            listRef.current.scrollTop = listRef.current.scrollHeight;
        }
    };

    const handleScroll = () => {
        if (listRef.current) {
            const {scrollTop, clientHeight, scrollHeight} = listRef.current;
            const isBottom = scrollHeight - scrollTop === clientHeight;
            setIsAtBottom(isBottom);
        }
    };

    useEffect(() => {
        if (receiveMessage) {
            setChatMessages(prev => [...prev, receiveMessage]);
        }
    }, [receiveMessage])

    function sendMessage() {
        if (stompClient && stompClient.connected) {
            try {
                stompClient.publish({
                    destination: `/app/chat-rooms/${room.chatRoomId}/messages`,
                    headers: {
                        Authorization: localStorage.getItem("Authorization")
                    },
                    body: JSON.stringify({
                        content: messageInput
                    }),
                });
                setMessageInput('');
            } catch (error) {
                alert("메세지 전송 실패");
            }
        }
    }

    return (
        <ModalDialog sx={style} variant="plain">
            <AppBar position="fixed" color="primary"
                    sx={{top: 0, bottom: 'auto'}}>
                <Toolbar>
                    <IconButton color="inherit" onClick={onBackClick}>
                        <ArrowBackIcon/>
                    </IconButton>
                    <Box sx={{flexGrow: 1}}>{room.chatRoomName}</Box>
                    <IconButton color="inherit">
                        <SearchIcon/>
                    </IconButton>
                    <IconButton color="inherit" aria-label="open drawer">
                        <MenuIcon/>
                    </IconButton>
                </Toolbar>
            </AppBar>
            <List ref={listRef} spacing={2} sx={listStyle_mt}
                  onScroll={handleScroll}>
                {chatMessages.map(
                    ({
                        userId,
                        messageId,
                        userName,
                        content,
                        createdAt
                    }) => (
                        <React.Fragment key={messageId}>
                            {userId === myUserId ? (
                                <MyMessage message={content}/>
                            ) : (
                                <OtherMessage message={content}
                                              avatarUrl="https://example.com/avatar.jpg"
                                              userName={userName}/>
                            )}
                        </React.Fragment>
                    ))
                }
            </List>
            <AppBar position="fixed" color="primary"
                    sx={{top: 'auto', bottom: 0}}>
                <Toolbar>
                    <TextField
                        hiddenLabel
                        id="filled-hidden-label-normal"
                        variant="filled"
                        sx={{flexGrow: 1}}
                        value={messageInput}
                        onChange={e => setMessageInput(e.target.value)}
                    />
                    <IconButton color="inherit" aria-label="send"
                                onClick={sendMessage}
                                disabled={messageInput.length === 0}>
                        <SendIcon/>
                    </IconButton>
                </Toolbar>
            </AppBar>
        </ModalDialog>
    );
}
