import {Box, Button, Grid, IconButton} from "@mui/material";
import React, {useEffect, useState} from "react";
import SalePost from "./SalePost";
import {getPosts} from "../api/SalePostApi";
import {Refresh as RefreshIcon} from "@mui/icons-material";

export const category = [
    {
        value: null,
        name: '전체',
    },
    {
        value: "사료",
        name: '사료',
    },
    {
        value: "장난감",
        name: '장난감',
    },
    {
        value: "용품",
        name: '용품',
    },
    {
        value: "간식",
        name: '간식',
    },
    {
        value: "의류",
        name: '의류',
    },
];

function SalePostArea({handleCreateClick, handlePostClick}) {
    const [postList, setPostList] = useState();

    useEffect(() => {
        fetchPosts();
    }, []);

    async function fetchPosts() {
        try {
            const response = await getPosts();
            setPostList(response.data.data);
        } catch (e) {
            alert("상품 리스트 조회 실패...")
        }
    }

    const handleRefreshClick = () => {
        fetchPosts();
    };

    return (
        <div>
            <Box sx={{
                m: 2,
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                flexGrow: 1
            }}>
                <IconButton
                    color="primary"
                    onClick={handleRefreshClick}
                >
                    <RefreshIcon/>
                </IconButton>
                <Button variant="contained" onClick={handleCreateClick}>
                    상품 등록하기
                </Button>
            </Box>
            <Grid container spacing={2}>
                {postList && postList.map((post) => {
                    return (
                        <SalePost
                            key={post.salePostId}
                            post={post}
                            handlePostClick={handlePostClick}
                        />
                    )
                })}
            </Grid>
        </div>
    );
}

export default SalePostArea;