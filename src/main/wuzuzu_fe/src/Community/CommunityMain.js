import React, {useState} from 'react';
import {Box, Typography,} from '@mui/material';
import CommunityPostArea from "./CommunityPostArea";
import CreateCommunityPost from "./CreateCommunityPost";
import CommunityPostDetail from "./CommunityPostDetail";
import {getPostDetail} from "../api/CommunityApi";

export const communityAppState = {
    게시글조회중: 1,
    게시글상세조회중: 2,
    게시글작성중: 3
};

function CommunityMain() {

    const [state, setState] = useState(communityAppState.게시글조회중);
    const [selectedPost, setSelectedPost] = useState(null);

    function handleCreateClick() {
        setState(communityAppState.게시글작성중);
    }

    function handleBackClick() {
        setState(communityAppState.게시글조회중);
        setSelectedPost(null);
    }

    async function handlePostClick(post) {
        try {
            const response = await getPostDetail(post.communityPostId);
            setSelectedPost(response.data);
            setState(communityAppState.게시글상세조회중);
        } catch (error) {
            alert("글 상세 조회 실패");
        }
    }

    function switchUI() {
        switch (state) {
            case communityAppState.게시글조회중:
                return <CommunityPostArea
                    handleCreateClick={handleCreateClick}
                    handlePostClick={handlePostClick}/>
            case communityAppState.게시글상세조회중:
                return <CommunityPostDetail
                    post={selectedPost}
                    handleBackClick={handleBackClick}/>
            case communityAppState.게시글작성중:
                return <CreateCommunityPost
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
                        커뮤니티
                        <Typography variant="subtitle1"
                                    sx={{
                                        flexGrow: 1,
                                        textAlign: 'center',
                                        color: 'text.secondary'
                                    }}>
                            반려인들을 위한 육아 공간, 우쭈쭈 커뮤니티
                        </Typography>
                    </Typography>
                </Box>
                {switchUI()}
            </Box>
        </Box>
    );
}

export default CommunityMain;