// CommentList.js
import React, {useEffect, useState} from 'react';
import {
    Avatar,
    Box,
    Button,
    Divider,
    IconButton,
    TextField,
    Typography
} from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import {createComment, deleteComment, getComments} from "../api/CommunityApi";

function CommentList({post}) {

    const [comments, setComments] = useState([]);
    const [comment, setComment] = useState('');
    const userName = localStorage.getItem('userName');
    const userId = Number(localStorage.getItem('userId'));

    const handleCommentChange = (event) => {
        setComment(event.target.value);
    };

    const handleCommentSubmit = async () => {
        if (comment.trim() !== '') {
            try {
                await createComment(post.communityPostId, {contents: comment});
                setComment('');
                await fetchComments();
            } catch (e) {
                alert("댓글 작성 실패...");
            }
        }
    };

    const handleCommentDelete = async (commentId) => {
        try {
            const confirmed = window.confirm("댓글을 삭제하시겠습니까?");

            if (confirmed) {
                await deleteComment(commentId);
                await fetchComments();
            }
        } catch (e) {
            alert("댓글 삭제 실패...");
        }
    };

    async function fetchComments() {
        try {
            const response = await getComments(post.communityPostId);
            if (response.data) {
                setComments(response.data);
            }
        } catch (e) {
            alert("댓글 조회 실패...");
        }
    }

    useEffect(() => {
        fetchComments();
    }, []);

    return (
        <Box mt={4}>
            <Box mt={4}>
                <Box display="flex" alignItems="center" mb={2}>
                    <Avatar alt={userName}
                            src={'https://example.com/avatar.jpg'}
                            sx={{width: 32, height: 32, mr: 1}}/>
                    <TextField
                        variant="outlined"
                        placeholder="댓글을 입력하세요..."
                        value={comment}
                        onChange={handleCommentChange}
                        InputProps={{
                            style: {
                                borderRadius: 20,
                                padding: '8px 16px',
                            },
                        }}
                        fullWidth
                    />
                    <Button
                        variant="contained"
                        color="primary"
                        type="submit"
                        onClick={handleCommentSubmit}
                        sx={{
                            borderRadius: 20,
                            ml: 2,
                            minWidth: 'auto',
                            padding: '8px 16px',
                        }}
                    >
                        등록
                    </Button>
                </Box>
                <Divider/>
            </Box>
            <Typography variant="h6" gutterBottom>
                댓글 ({comments.length})
            </Typography>
            {comments.map((comment) => (
                <Box key={comment.commentId} mt={2}>
                    <Box display="flex" alignItems="center">
                        <Avatar alt={comment.username}
                                src={'https://example.com/avatar.jpg'}
                                sx={{width: 32, height: 32, mr: 1}}/>
                        <Typography
                            variant="subtitle2">{comment.username}</Typography>
                        {userId === comment.userId && (
                            <IconButton edge="end" aria-label="delete"
                                        sx={{ml: 'auto'}}
                                        onClick={() => handleCommentDelete(
                                            comment.commentId)}>
                                <CloseIcon/>
                            </IconButton>
                        )}
                    </Box>
                    <Typography variant="body1" mt={1}>
                        {comment.contents}
                    </Typography>
                    <Divider sx={{mt: 2}}/>
                </Box>
            ))}
        </Box>
    );
}

export default CommentList;