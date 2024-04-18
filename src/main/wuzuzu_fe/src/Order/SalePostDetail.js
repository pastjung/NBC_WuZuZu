import React, {useState} from 'react';
import {
    Avatar,
    Box,
    Button,
    Card,
    CardContent,
    CardMedia,
    Chip,
    Divider,
    MenuItem,
    Typography,
} from '@mui/material';
import {Visibility as VisibilityIcon} from '@mui/icons-material';
import {requestPay} from "../Payment/Payment";
import {TextField} from "@mui/material/";

function SalePostDetail({post, handleBackClick}) {

    const [quantity, setQuantity] = useState(1);

    const handleQuantityChange = (event) => {
        const value = parseInt(event.target.value);
        if (value >= 1 && value <= post.stock) {
            setQuantity(value);
        }
    };

    const handleBuyClick = async () => {
        const buyer = {
            email: localStorage.getItem('email'),
            userName: localStorage.getItem('userName'),
        };
        await requestPay({
            user: buyer,
            salePost: post,
            count: quantity,
        });
    };

    return (
        <Box sx={{display: 'flex', justifyContent: 'center'}}>
            <Card sx={{
                maxWidth: '800px',
                width: '100%',
                borderRadius: '20px',
                boxShadow: '0px 4px 10px rgba(0, 0, 0, 0.1)'
            }}>
                {(post.imageUrls && post.imageUrls.length > 0) && (
                    <CardMedia
                        component="img"
                        height="400"
                        image={post.imageUrls[0]}
                        alt={post.title}
                        sx={{
                            borderTopLeftRadius: '20px',
                            borderTopRightRadius: '20px'
                        }}
                    />
                )}
                <CardContent>
                    <Typography variant="h4" component="div" gutterBottom>
                        {post.title}
                    </Typography>
                    <Box sx={{display: 'flex', alignItems: 'center', mb: 2}}>
                        <Avatar
                            alt={post.author}
                            src='https://example.com/avatar.jpg'
                            sx={{width: 32, height: 32, mr: 1}}
                        />
                        <Typography
                            variant="subtitle1">{post.author}</Typography>
                    </Box>
                    <Chip label={post.category} color="primary" size="small"
                          sx={{mb: 2}}/>
                    <Divider sx={{mb: 2}}/>
                    <Box sx={{mb: 2}}>
                        <Typography variant="subtitle1"
                                    sx={{fontWeight: 'bold'}}>상품명</Typography>
                        <Typography variant="body1">{post.goods}</Typography>
                    </Box>
                    <Box sx={{mb: 2}}>
                        <Typography variant="subtitle1"
                                    sx={{fontWeight: 'bold'}}>가격</Typography>
                        <Typography variant="body1">{post.price}원</Typography>
                    </Box>
                    <Box sx={{mb: 2}}>
                        <Typography variant="subtitle1"
                                    sx={{fontWeight: 'bold'}}>재고</Typography>
                        <Typography variant="body1">{post.stock}개</Typography>
                    </Box>
                    <Box sx={{mb: 4}}>
                        <Typography variant="subtitle1"
                                    sx={{fontWeight: 'bold'}}>상품 설명</Typography>
                        <Typography variant="body1"
                                    sx={{whiteSpace: 'pre-line'}}>{post.description}</Typography>
                    </Box>
                    <Box sx={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'space-between',
                        mb: 2
                    }}>
                        <Box sx={{display: 'flex', alignItems: 'center'}}>
                            <VisibilityIcon fontSize="small" sx={{mr: 1}}/>
                            <Typography
                                variant="body2">{post.views}</Typography>
                        </Box>
                        <Button variant="contained" onClick={handleBackClick}
                                sx={{borderRadius: '20px'}}>
                            목록으로
                        </Button>
                    </Box>
                    <Box sx={{mb: 2, display: 'flex', alignItems: 'center'}}>
                        <TextField
                            type="number"
                            value={quantity}
                            onChange={handleQuantityChange}
                            inputProps={{
                                min: 1,
                                max: post.stock,
                            }}
                            sx={{mr: 2, width: '100px'}}
                        />
                        <Button
                            variant="contained"
                            color="primary"
                            sx={{borderRadius: '20px'}}
                            onClick={handleBuyClick}
                        >
                            구매하기
                        </Button>
                    </Box>
                </CardContent>
            </Card>
        </Box>
    );
}

export default SalePostDetail;