import React, {useEffect, useState} from "react";
import {getAllRooms, getMyRooms} from "../api/ChatRoomApi";
import {
    AppBar,
    Box,
    Button,
    IconButton,
    List,
    Menu,
    MenuItem,
    Select,
    Skeleton,
    TextField,
    Toolbar
} from "@mui/material";
import SearchIcon from "@mui/icons-material/Search";
import MoreIcon from "@mui/icons-material/MoreVert";
import {listStyle, style} from "./ChattingApp";
import ChattingRoomInfo from "./ChattingRoomInfo";
import {ModalDialog} from "@mui/joy";

function ChattingRoomInfoList({
    handleClose,
    handleEnterClick,
    handleCreateChatRoomClick
}) {

    const [menuItem, setMenuItem] = useState("my");
    const [roomList, setRoomList] = useState(null);
    const [selectedIndex, setSelectedIndex] = useState(-1);
    const [anchorEl, setAnchorEl] = useState(null);
    const open = Boolean(anchorEl);
    const handleMenuClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
    };

    async function getMyRoomList() {
        try {
            const response = await getMyRooms();
            setRoomList(response.data);
        } catch (error) {
            alert("채팅방 목록을 가져오는데 실패했습니다.");
            handleClose();
        }
    }

    async function getRoomList() {
        try {
            const response = await getAllRooms();
            setRoomList(response.data);
        } catch (error) {
            alert("채팅방 목록을 가져오는데 실패했습니다.");
            handleClose();
        }
    }

    useEffect(() => {
        if (menuItem === "my" && roomList === null) {
            getMyRoomList();
        } else if (menuItem === "all" && roomList === null) {
            getRoomList();
        }
    }, [menuItem, roomList]);

    const handleChange = (event) => {
        setRoomList(null);
        setMenuItem(event.target.value);
    }

    const handleListItemClick = (index) => {
        setSelectedIndex(index);
    }

    return (
        <ModalDialog
            sx={{
                ...style,
                bgcolor: 'white', // 원하는 배경색으로 변경
            }}
            variant="plain"
        >
            <Select
                labelId="demo-simple-select-filled-label"
                id="demo-simple-select-filled"
                defaultValue={"my"}
                onChange={handleChange}
                sx={{width: "100%", bgcolor: "#F2F5A9"}}
                disabled={roomList === null}
            >
                <MenuItem value={"my"}>내 채팅</MenuItem>
                <MenuItem value={"all"}>모든 채팅</MenuItem>
            </Select>
            <List spacing={2} sx={listStyle}>
                {roomList === null ? (
                    <Box>
                        <Skeleton variant="text" sx={{fontSize: '1rem'}}/>
                        <Skeleton variant="rounded" width={210} height={60}/>
                        <Skeleton variant="text" sx={{fontSize: '1rem'}}/>
                        <Skeleton variant="rounded" width={210} height={60}/>
                        <Skeleton variant="text" sx={{fontSize: '1rem'}}/>
                        <Skeleton variant="rounded" width={210} height={60}/>
                        <Skeleton variant="text" sx={{fontSize: '1rem'}}/>
                        <Skeleton variant="rounded" width={210} height={60}/>
                        <Skeleton variant="text" sx={{fontSize: '1rem'}}/>
                        <Skeleton variant="rounded" width={210} height={60}/>
                        <Skeleton variant="text" sx={{fontSize: '1rem'}}/>
                        <Skeleton variant="rounded" width={210} height={60}/>
                        <Skeleton variant="text" sx={{fontSize: '1rem'}}/>
                        <Skeleton variant="rounded" width={210} height={60}/>
                    </Box>
                ) : (
                    <Box>
                        {
                            roomList.map((room, index) => {
                                return (
                                    <ChattingRoomInfo
                                        key={room.chatRoomId}
                                        room={room}
                                        selected={selectedIndex === index}
                                        onClick={() => handleListItemClick(
                                            index)}
                                    />
                                )
                            })
                        }
                    </Box>
                )}
            </List>
            <AppBar position="fixed" color="primary"
                    sx={{top: 'auto', bottom: 0}}>
                <Toolbar>
                    <Button
                        variant="contained"
                        sx={{mr: "10px"}}
                        onClick={() => handleEnterClick(
                            roomList[selectedIndex])}
                        disabled={selectedIndex === -1}
                    >
                        입장
                    </Button>
                    <TextField
                        id="standard-textarea"
                        placeholder="검색"
                        variant="standard"
                        sx={{flexGrow: 1}}
                    />
                    <IconButton color="inherit"
                                disabled={roomList === null}
                    >
                        <SearchIcon/>
                    </IconButton>
                    <IconButton color="inherit"
                                id="basic-button"
                                aria-controls={open ? 'basic-menu' : undefined}
                                aria-haspopup="true"
                                aria-expanded={open ? 'true' : undefined}
                                onClick={handleMenuClick}
                                disabled={roomList === null}
                    >
                        <MoreIcon/>
                    </IconButton>
                    <Menu
                        id="basic-menu"
                        anchorEl={anchorEl}
                        open={open}
                        onClose={handleMenuClose}
                        anchorOrigin={{
                            vertical: 'top',
                            horizontal: 'right',
                        }}
                        transformOrigin={{
                            vertical: 'bottom',
                            horizontal: 'right',
                        }}
                        MenuListProps={{
                            'aria-labelledby': 'basic-button',
                        }}
                    >
                        <MenuItem onClick={handleCreateChatRoomClick}>방
                            생성</MenuItem>
                    </Menu>
                </Toolbar>
            </AppBar>
        </ModalDialog>
    );
}

export default ChattingRoomInfoList;