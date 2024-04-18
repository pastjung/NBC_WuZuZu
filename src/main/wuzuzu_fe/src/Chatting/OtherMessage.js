import React from 'react';
import {Avatar, Box, ListItemAvatar, Typography} from '@mui/material';

const OtherMessage = ({message, avatarUrl, userName}) => {
    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'flex-start',
                marginBottom: '10px',
            }}
        >
            <ListItemAvatar>
                <Avatar alt={userName} src={avatarUrl}/>
            </ListItemAvatar>
            <Box
                sx={{
                    display: 'flex',
                    flexDirection: 'column',
                }}
            >
                <Typography variant="caption" color="textSecondary">
                    {userName}
                </Typography>
                <Box
                    sx={{
                        backgroundColor: '#ffffff',
                        borderRadius: '10px',
                        padding: '10px',
                        maxWidth: '70%',
                        boxShadow: '0px 2px 5px rgba(0, 0, 0, 0.1)',
                    }}
                >
                    <Typography variant="caption" component="body1">{message}</Typography>
                </Box>
            </Box>
        </Box>
    );
};

export default OtherMessage;