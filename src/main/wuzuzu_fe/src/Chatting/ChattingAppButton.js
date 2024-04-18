import React from "react";
import {IconButton} from '@mui/material';
import ChatIcon from '@mui/icons-material/Chat';
import ChattingApp from "./ChattingApp";

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '20%',
    height: '80%',
    bgcolor: 'background.paper',
    border: '2px solid #000',
    borderRadius: '10px',
    boxShadow: 24,
    overflowX: 'hidden',
};

function ChattingAppButton() {

    const [open, setOpen] = React.useState(false);

    const handleOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <div style={{position: 'fixed', bottom: '5%', right: '5%'}}>
            <IconButton color="primary" aria-label="open chat"
                        onClick={handleOpen}>
                <ChatIcon fontSize="large"/>
            </IconButton>
            {open === true ? (
                <ChattingApp open={open} handleClose={handleClose}/>
            ) : null
            }
        </div>
    );
}

export default ChattingAppButton;