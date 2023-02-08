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
    setAccount({
      ...account,
      [e.target.name]: e.target.value,
    });
  };

  const join = () => {
    navigate("/join");
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.post("/api/users/login",
        {
          memId: account.id,
          password: account.password,
        })
        .then(response=> console.log(response.data))
    navigate("/home");
  };

  return (
    <div className="login-wrapper">
      <h2>Login</h2>
      <form method="get" onSubmit={handleSubmit} id="login-form">
        <input
          type="text"
          name="id"
          onChange={onChangeAccount}
          placeholder="id"
        />
        <input
          type="password"
          name="password"
          onChange={onChangeAccount}
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
