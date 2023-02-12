import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Login = () => {
  const navigate = useNavigate();
  const [account, setAccount] = useState({
    id: "",
    password: "",
  });
  // e.target.name = input의 name(id,password)
  const onChangeAccount = (e) => {
    setAccount((prev) => {
      return {...prev, [e.target.name]: e.target.value};
    });
  };

  const param = {
    memId: account.id,
    password: account.password,
  };

  const join = () => {
    navigate("/join");
  };

  const onLoginSuccess = (response) => {
    const { accessToken } = response.data.data.accessToken;
    const expireTime = response.data.data.accessTokenExpireTime
    // accessToken 설정
    axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`;

    // accessToken 만료하기 1분 전에 로그인 연장
    setTimeout(onMakeRefreshToken, expireTime - 60000);
    navigate("/home");
  }

  const onLoginError = () => {
    alert("아이디 혹은 패스워드가 일치하지 않습니다");
    setAccount((prev) => {
      return {...prev, id: "", password: ""};
    });
  }

  const onMakeRefreshToken = () => {
    axios.post('/api/users/reissue', param)
        .then(onLoginSuccess);
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.post("/api/users/login", param)
        .then(onLoginSuccess)
        .catch(onLoginError);
  };

  return (
    <div className="login-wrapper">
      <h2>Login</h2>
      <form method="get" onSubmit={handleSubmit} id="login-form">
        <input
          type="text"
          name="id"
          onChange={onChangeAccount}
          value={account.id}
          placeholder="id"
        />
        <input
          type="password"
          name="password"
          onChange={onChangeAccount}
          value={account.password}
          placeholder="password"
        />
        {/*<label htmlFor="remember-check">*/}
        {/*  <input type="checkbox" id="remember-check" />*/}
        {/*  아이디 저장하기*/}
        {/*</label>*/}
        <input type="submit" value="Login" />
        <div className="login-box">
          <span onClick={join}>회원가입</span><span> | </span><span>아이디 찾기</span><span> | </span><span>비밀번호 찾기</span>
        </div>
      </form>

    </div>
  );
};
export default Login;
