import React from 'react';
import {Box, Typography} from '@mui/material';

function MyMessage({message}) {
    return (
        <Box
            sx={{
                display: 'flex',
                justifyContent: 'flex-end',
                marginBottom: '10px',
            }}
        >
            <Box
                sx={{
                    backgroundColor: '#ffe934',
                    borderRadius: '10px',
                    padding: '10px',
                    maxWidth: '70%',
                    boxShadow: '0px 2px 5px rgba(0, 0, 0, 0.1)',
                }}
            >
                <Typography variant="caption" component="body1">{message}</Typography>
            </Box>
        </Box>
    );
};

export default MyMessage;