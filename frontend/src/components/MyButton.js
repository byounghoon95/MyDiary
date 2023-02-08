const MyButton = ({ text, onClick, type }) => {
    //부모 컴포넌트에서 우리가 원하는 값이 들어오지 않으면 강제로 default로 변경
    const btnType = ["positive", "negative"].includes(type) ? type : "default";
    return (
        <button className={["myBtn", `myBtn_${type}`].join(" ")} onClick={onClick}>
            {text}
        </button>
    );
};

//props가 넘어오지 않으면 default 설정
MyButton.defaultProps = {
    type: "default",
};

export default MyButton;
//클래스명은 문자열이므로 배열을 전달하면 안되어 join을 통해 문자열로 변경 후 전달
