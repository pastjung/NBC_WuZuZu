import {
    Avatar,
    Box,
    CardActionArea,
    CardMedia,
    Grid,
    Typography
} from "@mui/material";
import {Card, CardContent} from "@mui/joy";
import {
    Comment as CommentIcon,
    Favorite as FavoriteIcon,
    Visibility as VisibilityIcon
} from "@mui/icons-material";
import React from "react";

function CommunityPost({post, handlePostClick}) {
    return (
        <Grid item xs={12} sm={6} md={6}>
            <Card sx={{
                maxHeight: "400px",
                minHeight: "150px",
                display: "flex",
            }}>
                <CardActionArea onClick={() => handlePostClick(post)}>
                    {post.image ? (
                        <CardMedia component="img" height="150"
                                   image={post.image}
                                   alt={post.title}/>
                    ) : null}
                    <CardContent>
                        <Typography
                            variant="h6"
                            component="div"
                            sx={{
                                pt: 2,
                                pb: 2,
                                overflow: "hidden",
                                textOverflow: "ellipsis",
                                whiteSpace: "nowrap",
                            }}
                        >
                            {post.title}
                        </Typography>
                        <Typography
                            variant="body2"
                            color="text.secondary"
                            sx={{
                                overflow: "hidden",
                                textOverflow: "ellipsis",
                                display: "-webkit-box",
                                WebkitLineClamp: "2",
                                WebkitBoxOrient: "vertical",
                            }}
                        >
                            {post.contents}
                        </Typography>
                        <Box
                            sx={{
                                display: 'flex',
                                alignItems: 'center',
                                mt: 2
                            }}>
                            <Avatar alt={post.username}
                                    src='https://example.com/avatar.jpg'
                                    sx={{width: 24, height: 24, mr: 1}}/>
                            <Typography
                                variant="body2">{post.username}</Typography>
                            <Typography variant="body2"
                                        color="text.secondary"
                                        sx={{ml: 'auto'}}>
                                {post.date}
                            </Typography>
                        </Box>
                        <Box
                            sx={{
                                display: 'flex',
                                alignItems: 'center',
                                mt: 2
                            }}>
                            <FavoriteIcon fontSize="small" sx={{mr: 1}}/>
                            <Typography
                                variant="body2">{post.likeCount}</Typography>
                            <VisibilityIcon fontSize="small"
                                            sx={{ml: 2, mr: 1}}/>
                            <Typography
                                variant="body2">{post.views}</Typography>
                            <CommentIcon fontSize="small" sx={{ml: 2, mr: 1}}/>
                            <Typography
                                variant="body2">{post.comments}</Typography>
                        </Box>
                    </CardContent>
                </CardActionArea>
            </Card>
        </Grid>
    );
}

export default CommunityPost;