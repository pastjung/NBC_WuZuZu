import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import {
  Avatar,
  Button,
  CssBaseline,
  TextField,
  FormControl,
  FormControlLabel,
  Checkbox,
  FormHelperText,
  Grid,
  Box,
  Typography,
  Container,
} from '@mui/material/';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import styled from 'styled-components';
import Link from '@mui/material/Link';
import { Modal } from "@mui/material";

const FormHelperTexts = styled(FormHelperText)`
  width: 100%;
  padding-left: 16px;
  font-weight: 700 !important;
  color: #d32f2f !important;
`;

const Boxs = styled(Box)`
  padding-bottom: 40px !important;
`;

const Register = () => {
  const theme = createTheme();
  const [checked, setChecked] = useState(false);
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [registerError, setRegisterError] = useState('');
  const [modalOpen, setModalOpen] = useState(false);
  const [verifyCode, setVerificationCode] = useState('');
  const [isVerified, setIsVerified] = useState(false); // 추가
  const navigate = useNavigate();
  const [mail, setEmail] = useState('');


  const handleAgree = (event) => {
    setChecked(event.target.checked);
  };



  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handleVerifyEmail = async () => {

    // 인증번호 발송 로직
    await axios
    .post('/api/v1/email/users/auth', {mail})
    .then(function (response) {
        // 이메일 인증 모달 열기
        setModalOpen(true);
        console.log(response, '인증번호 발송 성공');

    })
    .catch(function (err) {
      console.log(err);
      setRegisterError(err.response.data.msg);
    });

  };


  const handleCloseModal = () => {
    // 모달 닫기
    setModalOpen(false);
    setIsVerified(true); // 모달이 닫힐 때 인증 완료로 설정
  };

  const handleVerificationCodeChange = (e) => {
    // 인증번호 입력 시 상태 업데이트
    setVerificationCode(e.target.value);
  };

  const handleVerifyCode = async () => {
    // 서버에 인증번호를 전송하고 확인하는 로직을 구현하세요.
    await axios
    .post('/api/v1/email/users/verify', { mail, verifyCode })
    .then(function (response) {
      alert('인증번호 확인 완료!');
      handleCloseModal();
    })
    .catch(function (error) {
      console.error('인증번호 확인 에러:', error);
      alert('인증번호 확인에 실패했습니다. 다시 시도해주세요.');
    });
  };

  const onhandlePost = async (data) => {
    const {email, name, password, confirmPassword, address, userName, petName, petType} = data;
    const postData = {
      email,
      name,
      password,
      confirmPassword,
      address,
      userName,
      petName,
      petType
    };

    // post
    await axios
    .post('/api/v1/users/signup', postData)
    .then(function (response) {
      console.log(response, '성공');
      navigate('/');
    })
    .catch(function (err) {
      console.log(err);
      setRegisterError('회원가입에 실패하였습니다. 다시한번 확인해 주세요.');
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const data = new FormData(e.currentTarget);
    const joinData = {
      email: data.get('email'),
      name: data.get('name'),
      password: data.get('password'),
      confirmPassword: data.get('confirmPassword'),
      address: data.get('address'),
      userName: data.get('userName'),
      petName: data.get('petName'),
      petType: data.get('petType')
    };
    const {
      email,
      password,
      confirmPassword,
    } = joinData;

    // 이메일 유효성 체크
    const emailRegex = /([\w-.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;
    if (!emailRegex.test(email)) {
      setEmailError('올바른 이메일 형식이 아닙니다.');
    } else {
      setEmailError('');
    }

    // // 비밀번호 유효성 체크
    // const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
    // if (!passwordRegex.test(password))
    //   setPasswordState('숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요!');
    // else setPasswordState('');

    // 비밀번호 같은지 체크
    if (password !== confirmPassword) {
      setPasswordError('비밀번호가 일치하지 않습니다.');
    } else {
      setPasswordError('');
    }

    // // 이름 유효성 검사
    // const nameRegex = /^[가-힣a-zA-Z]+$/;
    // if (!nameRegex.test(name) || name.length < 1) setNameError('올바른 이름을 입력해주세요.');
    // else setNameError('');

    // 회원가입 동의 체크
    if (!checked) {
      alert('회원가입 약관에 동의해주세요.');
    }

    if (
        emailRegex.test(email) &&
        // passwordRegex.test(password) &&
        password === confirmPassword &&
        // nameRegex.test(name) &&
        checked
    ) {
      onhandlePost(joinData);
    }
  };

  return (
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <Container component="main" maxWidth="xs">
          <Box
              sx={{
                marginTop: 8,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
              }}
          >
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }} />
            <Typography component="h1" variant="h5">
              회원가입
            </Typography>
            <Boxs component="form" noValidate onSubmit={handleSubmit} sx={{ mt: 3 }}>
              <FormControl component="fieldset" variant="standard">
                <Grid container spacing={2}>
                  <Grid item xs={12}>
                    <TextField
                        required
                        autoFocus
                        fullWidth
                        type="email"
                        id="email"
                        name="email"
                        label="Email"
                        error={emailError !== ''}
                        value={mail}
                        onChange={handleEmailChange}
                    />
                    <Button variant="contained" onClick={handleVerifyEmail} fullWidth sx={{mt: 1, mb: 1}} style={{ backgroundColor: isVerified ? 'green' : undefined }}>
                      {isVerified ? "인증되었습니다" : "인증하기"}
                    </Button>
                  </Grid>
                  <FormHelperTexts>{emailError}</FormHelperTexts>
                  <Grid item xs={12}>
                    <TextField
                        required
                        fullWidth
                        type="password"
                        id="password"
                        name="password"
                        label="password "
                        // error={passwordState !== '' || false}
                    />
                  </Grid>
                  {/*<FormHelperTexts>{passwordState}</FormHelperTexts>*/}
                  <Grid item xs={12}>
                    <TextField
                        required
                        fullWidth
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        label="confirmPassword"
                        error={passwordError !== '' || false}
                    />
                  </Grid>
                  <FormHelperTexts>{passwordError}</FormHelperTexts>

                  <Grid item xs={12}>
                    <TextField
                        required
                        fullWidth
                        id="address"
                        label="address"
                        name="address"
                        autoComplete="address"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                        required
                        fullWidth
                        id="userName"
                        label="userName"
                        name="userName"
                        autoComplete="userName"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                        fullWidth
                        id="petName"
                        label="petName"
                        name="petName"
                        autoComplete="petName"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                        fullWidth
                        id="petType"
                        label="petType"
                        name="petType"
                        autoComplete="petType"
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <FormControlLabel
                        control={<Checkbox onChange={handleAgree}
                                           color="primary"/>}
                        label="회원가입 약관에 동의합니다."
                    />
                  </Grid>
                </Grid>
                <Button
                    type="submit"
                    fullWidth
                    variant="contained"
                    sx={{mt: 3, mb: 2}}
                    size="large"
                    disabled={!isVerified} // 인증되지 않았으면 비활성화
                >
                  회원가입
                </Button>
                <Grid container justifyContent="flex-end">
                  <Grid item>
                    <Link href="/" variant="body2">
                      Already have an account? Sign in
                    </Link>
                  </Grid>
                </Grid>
              </FormControl>
              <FormHelperTexts>{registerError}</FormHelperTexts>
            </Boxs>
          </Box>
        </Container>
        {/* 모달 */}
        <Modal
            open={modalOpen}
            onClose={handleCloseModal}
            aria-labelledby="verification-code-modal"
            aria-describedby="enter-verification-code"
        >
          <Box
              sx={{
                position: 'absolute',
                top: '50%',
                left: '50%',
                transform: 'translate(-50%, -50%)',
                bgcolor: 'white',
                border: '2px solid #000',
                boxShadow: 24,
                p: 4,
                zIndex: 9999, // zIndex 추가
              }}
          >
            <Typography id="verification-code-modal" variant="h6" component="h2" align="center">
              인증번호 입력
            </Typography>
            <TextField
                label="인증번호"
                variant="outlined"
                fullWidth
                value={verifyCode}
                onChange={handleVerificationCodeChange}
            />
            <Button variant="contained" onClick={handleVerifyCode} sx={{ mt: 2 }}>
              확인
            </Button>
          </Box>
        </Modal>
      </ThemeProvider>
  );
};

export default Register;
