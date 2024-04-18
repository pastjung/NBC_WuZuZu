import {apiClient} from "../Client"
import React, {useState} from 'react';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import {useNavigate, Link} from "react-router-dom";
import {FormHelperText, styled} from "@mui/material";

const FormHelperTexts = styled(FormHelperText)`
  width: 100%;
  padding-left: 16px;
  font-weight: 1500 !important;
  color: #d32f2f !important;
`;


const Login = ({ history }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();
  const [registerError, setRegisterError] = useState('');


  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const result = await apiClient.post('/api/v1/login',
          {email: email, password: password});
      localStorage.setItem("Authorization", result.data.accessToken);
      localStorage.setItem("userId", result.data.userId);
      localStorage.setItem("userName", result.data.userName);
      // alert('로그인 완료되었습니다.');
      navigate("/Main");
    } catch (error) {
      setRegisterError('로그인에 실패하였습니다. 다시한번 확인해 주세요.');
    }
  };

  function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
          {'Copyright © '}
          <Link color="inherit" href="https://mui.com/">
            Your Website
          </Link>{' '}
          {new Date().getFullYear()}
          {'.'}
        </Typography>
    );
  }

  const defaultTheme = createTheme();

  return (
      <ThemeProvider theme={defaultTheme}>
        <Container component="main" maxWidth="xs">
          <CssBaseline />
          <Box
              sx={{
                marginTop: 8,
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
              }}
          >
            <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5">
              Login
            </Typography>
              <TextField
                  margin="normal"
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  autoFocus
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
              />
              <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
              />
              <FormControlLabel
                  control={<Checkbox value="remember" color="primary" />}
                  label="Remember me"
              />
            <FormHelperTexts>{registerError}</FormHelperTexts>
              <Button
                  onClick={handleLogin}
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{ mt: 3, mb: 2 }}
              >
                Log In
              </Button>

              <Grid container>
                <Grid item>
                  <Link to="/signup" variant="body2">
                    {"Don't have an account? Sign Up"}
                  </Link>
                </Grid>
              </Grid>

          </Box>
          <Copyright sx={{ mt: 8, mb: 4 }} />
        </Container>
      </ThemeProvider>
  );
};

export default Login;