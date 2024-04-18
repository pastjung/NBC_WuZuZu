import React, {useState} from 'react';
import {Box, Typography,} from '@mui/material';
import SalePostArea from "./SalePostArea";
import CreateSalePost from "./CreateSalePost";
import SalePostDetail from "./SalePostDetail";
import {communityAppState} from "../Community/CommunityMain";
import {getPostDetail} from "../api/SalePostApi";

export const orderAppState = {
    상품조회중: 1,
    상품상세조회중: 2,
    상품등록중: 3,
    상품구매중: 4
};

function OrderMain() {

    const [state, setState] = useState(orderAppState.상품조회중);
    const [selectedPost, setSelectedPost] = useState(null);

    function handleCreateClick() {
        setState(orderAppState.상품등록중);
    }

    function handleBackClick() {
        setState(orderAppState.상품조회중);
        setSelectedPost(null);
    }

    async function handlePostClick(post) {
        try {
            const response = await getPostDetail(post.salePostId);
            setSelectedPost(response.data.data);
            setState(communityAppState.게시글상세조회중);
        } catch (error) {
            alert("글 상세 조회 실패");
        }
    }

    function switchUI() {
        switch (state) {
            case orderAppState.상품조회중:
                return <SalePostArea
                    handleCreateClick={handleCreateClick}
                    handlePostClick={handlePostClick}/>
            case orderAppState.상품상세조회중:
                return <SalePostDetail
                    post={selectedPost}
                    handleBackClick={handleBackClick}/>
            case orderAppState.상품등록중:
                return <CreateSalePost
                    handleBackClick={handleBackClick}/>
            default:
                return null;
        }
    }

    return (
        <Box sx={{
            display: 'flex',
            justifyContent: 'center',
            backgroundColor: "#FBEFEF"
        }}>
            <Box sx={{p: 2, width: '70%', maxWidth: '1080px'}}>
                <Box sx={{display: "flex", alignItems: "center", mb: 2}}>
                    <Typography variant="h3"
                                sx={{flexGrow: 1, textAlign: 'center'}}>
                        거래 게시판
                        <Typography variant="subtitle1"
                                    sx={{
                                        flexGrow: 1,
                                        textAlign: 'center',
                                        color: 'text.secondary'
                                    }}>
                            반려인들을 위한 육아 공간, 우쭈쭈 거래 게시판
                        </Typography>
                    </Typography>
                </Box>
                {switchUI()}
            </Box>
        </Box>
    );
}

export default OrderMain;