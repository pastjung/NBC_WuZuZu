import React from 'react';
import SignUp from './views/SignUp';
import Login from "./views/Longin";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import Main from "./views/Main";
import Mypage from "./views/Mypage";
import Spot from "./views/Spot";
import Favorite from "./views/Favorite";
import SpotDetail from "./views/SpotDetail";
import ChattingAppButton from "./Chatting/ChattingAppButton";
import CommunityMain from "./Community/CommunityMain";
import OrderMain from "./Order/OrderMain";


function App() {
    return (
        <div>
            <BrowserRouter>
                <Routes>
                    <Route exact path="/" element={<Login/>}/>
                    <Route path="/signup" element={<SignUp/>}/>
                    <Route path="/Main" element={<Main/>}/>
                    <Route path="/Mypage" element={<Mypage/>}/>
                    <Route path="/Spot" element={<Spot/>}/>
                    <Route path="/Favorite" element={<Favorite/>}/>
                    <Route path="/SpotDetail" element={<SpotDetail/>}/>
                    <Route path="/community" element={<CommunityMain/>}/>
                    <Route path="/transaction" element={<OrderMain/>}/>
                </Routes>
            </BrowserRouter>
            <ChattingAppButton/>
        </div>
    );
}
export default App;
