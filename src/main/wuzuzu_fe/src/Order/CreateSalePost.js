import React, {useState} from "react";
import {
    Box,
    Button,
    Chip,
    Divider,
    IconButton,
    MenuItem,
    Paper,
    Select,
    TextField,
    Typography,
} from "@mui/material";
import Grid from "@mui/material/Grid";
import {category} from "./SalePostArea";
import PhotoCamera from "@mui/icons-material/PhotoCamera";
import {createSalePost, uploadImage} from "../api/SalePostApi";

function CreateSalePost({handleBackClick}) {
    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [goods, setGoods] = useState("");
    const [price, setPrice] = useState(0);
    const [stock, setStock] = useState(0);
    const [selectedCategory, setSelectedCategory] = useState("");
    const [image, setImage] = useState(null);

    const handleTitleChange = (event) => {
        setTitle(event.target.value);
    };

    const handleDescriptionChange = (event) => {
        setDescription(event.target.value);
    };

    const handleGoodsChange = (event) => {
        setGoods(event.target.value);
    };

    const handlePriceChange = (event) => {
        setPrice(event.target.value);
    };

    const handleStockChange = (event) => {
        setStock(event.target.value);
    };

    const handleCategoryChange = (event) => {
        setSelectedCategory(event.target.value);
    };

    const handleImageUpload = (event) => {
        setImage(event.target.files[0]);
    };

    const handleImageCancel = () => {
        setImage(null);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        try {
            const response = await createSalePost({
                    title: title,
                    description: description,
                    goods: goods,
                    price: price,
                    stock: stock,
                    category: selectedCategory,
                },
                image
            );

            handleBackClick();
        } catch (error) {
            console.error('Error creating post:', error);
            alert("업로드 실패...")
        }
    };

    return (
        <Box p={3} bgcolor="#FFF0F0">
            <form onSubmit={handleSubmit}>
                <Paper elevation={3} sx={{p: 3, borderRadius: "20px"}}>
                    <Typography variant="h5" align="center" gutterBottom>
                        🐾 거래 게시글 작성 🐾
                    </Typography>
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <TextField
                                label="제목"
                                value={title}
                                onChange={handleTitleChange}
                                fullWidth
                                required
                                sx={{borderRadius: "20px"}}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                label="내용"
                                value={description}
                                onChange={handleDescriptionChange}
                                multiline
                                rows={4}
                                fullWidth
                                required
                                sx={{borderRadius: "20px"}}
                            />
                        </Grid>
                        <Divider sx={{my: 2}}/>
                        <Grid item xs={12}>
                            <TextField
                                label="상품명"
                                value={goods}
                                onChange={handleGoodsChange}
                                fullWidth
                                required
                                sx={{borderRadius: "20px"}}
                            />
                        </Grid>
                        <Grid item xs={6}>
                            <TextField
                                label="가격"
                                value={price}
                                onChange={handlePriceChange}
                                type="number"
                                fullWidth
                                required
                                sx={{borderRadius: "20px"}}
                            />
                        </Grid>
                        <Grid item xs={6}>
                            <TextField
                                label="판매 수량"
                                value={stock}
                                onChange={handleStockChange}
                                type="number"
                                fullWidth
                                required
                                sx={{borderRadius: "20px"}}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <Select
                                value={selectedCategory}
                                onChange={handleCategoryChange}
                                fullWidth
                                displayEmpty
                                required
                                sx={{borderRadius: "20px"}}
                            >
                                <MenuItem value="" disabled>
                                    카테고리 선택
                                </MenuItem>
                                {category.map(({value, name}) => (
                                    <MenuItem key={value} value={value}>
                                        {name}
                                    </MenuItem>
                                ))}
                            </Select>
                        </Grid>
                        <Grid item xs={12}>
                            <Box display="flex" alignItems="center">
                                <IconButton
                                    color="primary"
                                    aria-label="upload picture"
                                    component="label"
                                >
                                    <input
                                        hidden
                                        accept="image/*"
                                        type="file"
                                        onChange={handleImageUpload}
                                    />
                                    <PhotoCamera/>
                                </IconButton>
                                {image && (
                                    <Box ml={2} display="flex"
                                         alignItems="center">
                                        <Chip
                                            label={image.name}
                                            color="primary"
                                            size="small"
                                            onDelete={handleImageCancel}
                                            sx={{mr: 1}}
                                        />
                                    </Box>
                                )}
                            </Box>
                        </Grid>
                    </Grid>
                </Paper>
                <Box mt={2} display="flex" justifyContent="space-between">
                    <Button variant="contained" onClick={handleBackClick}
                            sx={{borderRadius: "20px"}}>
                        뒤로 가기
                    </Button>
                    <Button type="submit" variant="contained" color="primary"
                            sx={{borderRadius: "20px"}}>
                        글 작성
                    </Button>
                </Box>
            </form>
        </Box>
    );
}

export default CreateSalePost;