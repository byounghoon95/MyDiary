import React, {useEffect, useState} from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const Join = () => {
  const navigate = useNavigate();

  const [id, setId] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [idValid, setIdValid] = useState(true);
  const [pwValid, setPwValid] = useState(false);

  const inputStyle = {
    width: 'calc(100% - 60px)',
    marginBottom:0
  }

  const handleId = (e) => {
    setId(e.target.value);
  };

  const handlePw = (e) => {
    setPassword(e.target.value);
  };

  const handleName = (e) => {
    setName(e.target.value);
  };

  const moveLogin = () => {
    navigate("/");
  };

  const doubleCheck = () => {
    if (id.length > 5) {
      setIdValid(true);
    } else {
      setIdValid(false);
    }
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    axios.post("/api/users/join", {
      memId: id,
      password: password,
      name: name,
    })
        .then(response => console.log(response));
    navigate("/");
  };

  return (
    <div className="login-wrapper">
      <h2>JOIN</h2>
      <form method="get" onSubmit={handleSubmit} id="login-form">
        <div>Id</div>
        <input
          type="text"
          name="id"
          style={inputStyle}
          onChange={handleId}
          placeholder="id"
        />
        <button type="button" className="chk-btn" onClick={doubleCheck}>중복확인</button>
          {!idValid && (<span id="idMsg" name="idMsg">아이디가 중복됩니다.</span>)}
        <div>Password</div>
        <input
          type="password"
          name="password"
          style={{marginBottom:0}}
          onChange={handlePw}
          placeholder="password"
        />
        <span id="pwMsg" name="pwMsg"></span>
        <div>Name</div>
        <input
            type="text"
            name="name"
            placeholder="name"
            onChange={handleName}
        />
        <input type="submit" value="회원가입" />
        <div className="login-box">
          <span onClick={moveLogin}>로그인</span><span> | </span><span>아이디 찾기</span><span> | </span><span>비밀번호 찾기</span>
        </div>
      </form>
    </div>
  );
};
export default Join;
