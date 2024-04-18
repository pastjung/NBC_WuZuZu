import * as React from 'react';
import {useEffect, useState} from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Divider from '@mui/material/Divider';
import Stack from '@mui/material/Stack';
import Typography from '@mui/material/Typography';

import CardHeader from '@mui/material/CardHeader';

import FormControl from '@mui/material/FormControl';
import InputLabel from '@mui/material/InputLabel';
import OutlinedInput from '@mui/material/OutlinedInput';
import Grid from '@mui/material/Unstable_Grid2';
import styled from "styled-components";
import {FormHelperText} from "@mui/material";
import {useNavigate} from "react-router-dom";

const FormHelperTexts = styled(FormHelperText)`
  width: 100%;
  padding-left: 16px;
  font-weight: 700 !important;
  color: #d32f2f !important;
`;

const Mypage = () => {

  const [user, setUser] = useState("");
  const [updateAllError, setUpdateAllError] = useState('');
  const [updatePwError, setUpdatePwError] = useState('');
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [address, setAddress] = useState('');
  const [userName, setUserName] = useState('');
  const [petName, setPetName] = useState('');
  const [petType, setPetType] = useState('');
  const navigate = useNavigate();

  //전체 수정
  const handleUpdateAll = async () => {
    try {
      // Call your API here to perform the login
      const response = await fetch('/api/v1/users', {
        method: 'PATCH',
        headers: {
          'Authorization': localStorage.getItem('Authorization'),
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(
            {currentPassword, address, userName, petName, petType}),
      });

      if (response.ok) {
        console.error('성공');
        navigate("/");
        // Handle successful login (e.g., set user state, redirect, etc.)
      } else {
        console.error('no');
        setUpdateAllError('전체 수정에 실패하였습니다. 다시한번 확인해 주세요.');
      }
    } catch (error) {
      console.error('Error logging in:', error);

    }
  };

  //비밀번호 수정
  const handleUpdatePw = async () => {
    try {
      // Call your API here to perform the login
      const response = await fetch('/api/v1/users/password', {
        method: 'PATCH',
        headers: {
          'Authorization': localStorage.getItem('Authorization'),
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({currentPassword, newPassword, confirmPassword}),
      });

      if (response.ok) {
        console.error('성공');
        window.location.reload();
        // Handle successful login (e.g., set user state, redirect, etc.)
      } else {
        console.error('no');
        setUpdatePwError('비밀번호 수정에 실패하였습니다. 다시한번 확인해 주세요.');
      }
    } catch (error) {
      console.error('Error logging in:', error);

    }
  };

  const handleFavorite = () => {
    navigate("/Favorite");
  };

  // 초기 값 뿌리기
  useEffect(() => {
    const requestOptions = {
      method: 'GET',
      headers: {
        'Authorization': localStorage.getItem('Authorization'),
      },
    };

    fetch("/api/v1/users", requestOptions)
    .then(response => response.json())
    .then(result => setUser(result))
    .catch(error => console.log('error'.error))
  }, [])

  if (user) {
    return (
        <Card>
          <CardContent>
            <Stack spacing={2} sx={{alignItems: 'center'}}>
              <div>
                <Avatar
                    src={'https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Fa4be6608-96ba-4621-a522-d0ffc1f60def%2FUntitled.jpeg?table=block&id=3b370cbc-e1a9-4dfd-b187-c7f79095e67c&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2'}
                    sx={{height: '80px', width: '80px'}}/>
              </div>
              <Stack spacing={1} sx={{textAlign: 'center'}}>
                <Typography variant="h5">{user.data.email}</Typography>
                <Typography color="text.secondary" variant="body2">
                  {user.data.address}
                </Typography>
                <Typography color="text.secondary" variant="body2">
                  {user.data.userName}
                </Typography>
                <Typography color="text.secondary" variant="body2">
                  {user.data.petName}
                </Typography>
                <Typography color="text.secondary" variant="body2">
                  {user.data.petType}
                </Typography>
              </Stack>
            </Stack>
          </CardContent>
          <Divider/>
          <CardActions>
            <Button fullWidth variant="text">
              Upload picture
            </Button>
          </CardActions>

          <CardActions>
            <Button fullWidth variant="text" onClick={handleFavorite}>
              즐겨 찾기
            </Button>
          </CardActions>

          <form
              onSubmit={(event) => {
                event.preventDefault();
              }}
          >
            <Card>
              <CardHeader subheader="The information can be edited"
                          title="Profile"/>
              <Divider/>
              <CardContent>
                <Grid container spacing={3}>
                  <Grid xs={12}>
                    <FormControl fullWidth required>
                      <InputLabel>currentPassword</InputLabel>
                      <OutlinedInput
                          label="currentPassword"
                          name="currentPassword"
                          id="currentPassword"
                          value={currentPassword}
                          onChange={(e) => setCurrentPassword(e.target.value)}
                      />
                    </FormControl>
                  </Grid>
                  <Grid xs={12}>
                    <FormControl fullWidth required>
                      <InputLabel>address</InputLabel>

                      <OutlinedInput defaultValue={user.data.address}
                                     label="address"
                                     name="address"
                                     onChange={(e) => setAddress(
                                         e.target.value)}/>
                    </FormControl>
                  </Grid>
                  <Grid xs={12}>
                    <FormControl fullWidth required>
                      <InputLabel>userName</InputLabel>
                      <OutlinedInput defaultValue={user.data.userName}
                                     label="userName" name="userName"
                                     onChange={(e) => setUserName(
                                         e.target.value)}/>
                    </FormControl>
                  </Grid>
                  <Grid md={6} xs={12}>
                    <FormControl fullWidth>
                      <InputLabel>petName</InputLabel>
                      <OutlinedInput defaultValue={user.data.petName}
                                     label="petName" name="petName"
                                     onChange={(e) => setPetName(
                                         e.target.value)}/>
                    </FormControl>
                  </Grid>
                  <Grid md={6} xs={12}>
                    <FormControl fullWidth>
                      <InputLabel>petType</InputLabel>
                      <OutlinedInput defaultValue={user.data.petType}
                                     label="petType" name="petType"
                                     onChange={(e) => setPetType(
                                         e.target.value)}/>
                    </FormControl>
                  </Grid>
                </Grid>
              </CardContent>
              <Divider/>
              <FormHelperTexts>{updateAllError}</FormHelperTexts>
              <CardActions sx={{justifyContent: 'flex-end'}}>
                <Button variant="contained" onClick={handleUpdateAll}> Save
                  details</Button>
              </CardActions>
            </Card>


            <Card>
              <CardHeader subheader="비밀번호 수정 "
                          title="PW "/>
              <Divider/>
              <CardContent>
                <Grid container spacing={3}>
                  <Grid xs={12}>
                    <FormControl fullWidth required>
                      <InputLabel>currentPassword</InputLabel>
                      <OutlinedInput defaultValue={user.data.password}
                                     label="currentPassword"
                                     name="currentPassword"
                                     value={currentPassword}
                                     onChange={(e) => setCurrentPassword(
                                         e.target.value)}
                      />
                    </FormControl>
                  </Grid>

                  <Grid xs={12}>
                    <FormControl fullWidth required>
                      <InputLabel>newPassword</InputLabel>
                      <OutlinedInput defaultValue={user.data.password}
                                     label="newPassword"
                                     name="newPassword"
                                     value={newPassword}
                                     onChange={(e) => setNewPassword(
                                         e.target.value)}
                      />
                    </FormControl>
                  </Grid>

                  <Grid xs={12}>
                    <FormControl fullWidth required>
                      <InputLabel>confirmPassword</InputLabel>
                      <OutlinedInput defaultValue={user.data.password}
                                     label="confirmPassword"
                                     name="confirmPassword"
                                     value={confirmPassword}
                                     onChange={(e) => setConfirmPassword(
                                         e.target.value)}
                      />
                    </FormControl>
                  </Grid>

                </Grid>
              </CardContent>
              <Divider/>
              <FormHelperTexts>{updatePwError}</FormHelperTexts>
              <CardActions sx={{justifyContent: 'flex-end'}}>
                <Button variant="contained" onClick={handleUpdatePw}>Save
                  pw</Button>
              </CardActions>
            </Card>

          </form>
        </Card>

    );
  } else {
    return null;
  }
};

export default Mypage;