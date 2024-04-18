import React, {useState} from "react";
import {
    Box,
    Button,
    Chip,
    IconButton,
    TextField,
    Typography
} from "@mui/material";
import {ModalDialog} from "@mui/joy";
import {
    Add as AddIcon,
    PhotoCamera as PhotoCameraIcon
} from "@mui/icons-material";
import {style} from "./ChattingApp";

function CreateChattingRoom({onCreate, handleBackClick}) {
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [hashtags, setHashtags] = useState([]);
    const [newHashtag, setNewHashtag] = useState("");
    const [coverImage, setCoverImage] = useState(null);

    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    };

    const handleNewHashtagChange = (event) => {
        setNewHashtag(event.target.value);
    };

    const handleAddHashtag = () => {
        if (newHashtag.trim() !== "") {
            setHashtags([...hashtags, newHashtag.trim()]);
            setNewHashtag("");
        }
    };

    const handleDeleteHashtag = (hashtag) => {
        setHashtags(hashtags.filter((tag) => tag !== hashtag));
    };

    const handleImageUpload = (event) => {
        const file = event.target.files[0];
        setCoverImage(file);
    };

    const handleCreateRoom = () => {
        const newRoom = {
            chatRoomName: title,
            description: description,
            chatRoomTags: hashtags,
            coverImage: coverImage,
        };
        onCreate(newRoom);
    };

    return (
        <ModalDialog
            sx={{
                ...style,
                bgcolor: "white",
                borderRadius: "10px",
                boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.1)",
                p: 3,
            }}
            variant="plain"
        >
            <Typography variant="h6" component="h2" sx={{mb: 1}}>
                새로운 채팅방 생성
            </Typography>
            <TextField
                label="채팅방 제목"
                value={title}
                onChange={handleTitleChange}
                fullWidth
                margin="normal"
                variant="outlined"
                InputProps={{
                    sx: {
                        borderRadius: "20px",
                        bgcolor: "#F5F5F5",
                        "& fieldset": {border: "none"},
                    },
                }}
            />
            <TextField
                label="설명"
                value={description}
                onChange={handleDescriptionChange}
                fullWidth
                multiline
                rows={4}
                margin="normal"
                variant="outlined"
                InputProps={{
                    sx: {
                        borderRadius: "20px",
                        bgcolor: "#F5F5F5",
                        "& fieldset": {border: "none"},
                    },
                }}
            />
            <Box sx={{display: "flex", alignItems: "center", mt: 1}}>
                <TextField
                    label="해시태그"
                    value={newHashtag}
                    onChange={handleNewHashtagChange}
                    fullWidth
                    margin="normal"
                    variant="outlined"
                    InputProps={{
                        sx: {
                            borderRadius: "20px",
                            bgcolor: "#F5F5F5",
                            "& fieldset": {border: "none"},
                        },
                        endAdornment: (
                            <IconButton onClick={handleAddHashtag}>
                                <AddIcon/>
                            </IconButton>
                        ),
                    }}
                />
            </Box>
            <Box sx={{display: "flex", flexWrap: "wrap", mt: 1}}>
                {hashtags.map((hashtag) => (
                    <Chip
                        key={hashtag}
                        label={`#${hashtag}`}
                        onDelete={() => handleDeleteHashtag(hashtag)}
                        sx={{
                            mr: 1,
                            mb: 1,
                            bgcolor: "#FFE0B2",
                            color: "#E65100"
                        }}
                    />
                ))}
            </Box>
            <Box sx={{mt: 2}}>
                <input
                    accept="image/*"
                    id="cover-image-upload"
                    type="file"
                    onChange={handleImageUpload}
                    style={{display: "none"}}
                />
                <label htmlFor="cover-image-upload">
                    <Button
                        variant="outlined"
                        component="span"
                        startIcon={<PhotoCameraIcon/>}
                        sx={{
                            borderRadius: "20px",
                            bgcolor: "#E0F7FA",
                            color: "#00ACC1",
                            "&:hover": {bgcolor: "#B2EBF2"},
                        }}
                    >
                        커버 이미지 업로드
                    </Button>
                </label>
            </Box>
            <Box sx={{display: "flex", justifyContent: "flex-end", mt: 1}}>
                <Button
                    variant="contained"
                    onClick={handleCreateRoom}
                    sx={{
                        mr: 1,
                        borderRadius: "20px",
                        bgcolor: "#4CAF50",
                        "&:hover": {bgcolor: "#388E3C"},
                    }}
                >
                    생성
                </Button>
                <Button
                    variant="outlined"
                    onClick={handleBackClick}
                    sx={{
                        borderRadius: "20px",
                        color: "#F44336",
                        borderColor: "#F44336",
                        "&:hover": {bgcolor: "#FFEBEE", borderColor: "#F44336"},
                    }}
                >
                    취소
                </Button>
            </Box>
        </ModalDialog>
    );
}

export default CreateChattingRoom;