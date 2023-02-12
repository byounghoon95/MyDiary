import React from "react";
const MyHeader = ({ leftChild, rightChild, headText }) => {
    return (
        <header>
            <div className="head_top">이병훈님의 일기</div>
            <div className="head_btn_left">{leftChild}</div>
            <div className="head_text">{headText}</div>
            <div className="head_btn_right">{rightChild}</div>
        </header>
    );
};
export default MyHeader;
