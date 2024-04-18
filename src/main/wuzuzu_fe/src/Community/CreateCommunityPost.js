import React, {useEffect, useState} from "react";
import {
    Alert,
    Box,
    Button, Chip,
    MenuItem,
    Select,
    Snackbar,
    TextField,
    Typography,
} from "@mui/material";
import Grid from "@mui/material/Grid";
import {category} from "./CommunityPostArea";
import {createCommunityPost, uploadImage} from "../api/CommunityApi";
import {Add as AddIcon, Cancel as CancelIcon} from "@mui/icons-material";

function CreateCommunityPost({handleBackClick}) {
    const [selectedCategory, setSelectedCategory] = useState("");
    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [showError, setShowError] = useState(false);
    const [selectedImage, setSelectedImage] = useState(null);

    const handleCategoryChange = (event) => {
        setSelectedCategory(event.target.value);
    };

    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleContentChange = (event) => {
        setContent(event.target.value);
    };

    const handleImageChange = (event) => {
        const file = event.target.files[0];
        setSelectedImage(file);
    };

    const handleImageCancel = () => {
        setSelectedImage(null);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const response = await createCommunityPost(
                {
                    title: title,
                    content: content,
                    categoryName: selectedCategory,
                },
                selectedImage
            );

            handleBackClick();
        } catch (error) {
            console.error('Error creating post:', error);
            alert('업로드 실패...')
        }
    };

    useEffect(() => {
        if (showError) {
            const timer = setTimeout(() => {
                setShowError(false);
            }, 3000);

            return () => {
                clearTimeout(timer);
            };
        }
    }, [showError]);

    return (
        <div>
            <Typography variant="h4" align="center" gutterBottom>
                글 쓰기
            </Typography>
            <form onSubmit={handleSubmit}>
                <Grid container spacing={2} sx={{
                    width: "100%",
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    borderRadius: "10px",
                    p: 2,
                }}>
                    <Grid item xs={12} sm={2}>
                        <Select
                            value={selectedCategory}
                            onChange={handleCategoryChange}
                            sx={{
                                mb: 2,
                                backgroundColor: "#fff",
                                borderRadius: "10px"
                            }}
                            fullWidth
                            displayEmpty
                        >
                            <MenuItem value="" disabled>
                                카테고리 선택
                            </MenuItem>
                            {category
                            .filter(({value}) => value !== null)
                            .map(({value, name}) => (
                                <MenuItem key={value} value={value}>
                                    {name}
                                </MenuItem>
                            ))}
                        </Select>
                    </Grid>
                    <Grid item xs={12} sm={10}>
                        <TextField
                            label="제목"
                            value={title}
                            onChange={handleTitleChange}
                            sx={{
                                mb: 2,
                                backgroundColor: "#fff",
                                borderRadius: "10px"
                            }}
                            fullWidth
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            label="내용"
                            value={content}
                            onChange={handleContentChange}
                            multiline
                            rows={10}
                            fullWidth
                            sx={{
                                mb: 2,
                                backgroundColor: "#fff",
                                borderRadius: "10px"
                            }}
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <Button
                            variant="outlined"
                            component="label"
                            startIcon={<AddIcon/>}
                            sx={{mb: 2}}
                        >
                            사진 업로드
                            <input
                                type="file"
                                accept="image/*"
                                hidden
                                onChange={handleImageChange}
                            />
                        </Button>
                        {selectedImage && (
                            <Chip
                                label={selectedImage.name}
                                onDelete={handleImageCancel}
                                deleteIcon={<CancelIcon/>}
                                sx={{ml: 2}}
                            />
                        )}
                    </Grid>
                </Grid>
                <Box sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    alignItems: "center",
                    mt: 2,
                }}>
                    <Button
                        variant="contained"
                        onClick={handleBackClick}
                        sx={{borderRadius: "20px"}}
                    >
                        뒤로 가기
                    </Button>
                    <Button
                        type="submit"
                        variant="contained"
                        sx={{borderRadius: "20px"}}
                    >
                        글 작성
                    </Button>
                </Box>
            </form>
            <Snackbar open={showError} autoHideDuration={3000}
                      onClose={() => setShowError(false)}>
                <Alert severity="error" onClose={() => setShowError(false)}>
                    {errorMessage}
                </Alert>
            </Snackbar>
        </div>
    );
}

export default CreateCommunityPost;