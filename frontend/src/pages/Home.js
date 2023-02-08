import MyHeader from "../components/MyHeader";
import MyButton from "../components/MyButton";
import { useContext, useEffect, useState } from "react";
import DiaryList from "../components/DiaryList";
import Paging from "../components/Paging";
import MyFooter from "../components/MyFooter";
import { DiaryStateContext } from "../App";

const Home = () => {
  //useContext가 null일 때, 에러나므로 null 체크
  const useDiaryState = () => {
    const state = useContext(DiaryStateContext);
    if (!state) throw new Error("DiaryStateContext Not Found");
    return state;
  };
  const diaryList = useDiaryState();
  const [data, setData] = useState(diaryList);
  const [curDate, setCurDate] = useState(new Date());
  const headText = `${curDate.getFullYear()}년 ${curDate.getMonth() + 1}월`;

  //curDate가 바뀔때마다 실행
  //해당 월의 첫번째 날과 마지막 날을 계산
  useEffect(() => {
    if (diaryList.length >= 1) {
      const firstday = new Date(
        curDate.getFullYear(),
        curDate.getMonth(),
        1
      ).getTime();
      const lastday = new Date(
        curDate.getFullYear(),
        curDate.getMonth() + 2,
        1
      ).getTime();
      setData(
        diaryList.filter((it) => firstday <= it.date && it.date <= lastday)
      );
    }
  }, [diaryList, curDate]);

  useEffect(() => {
    console.log(data);
  }, [data]);

  const increaseMonth = () => {
    setCurDate(
      new Date(curDate.getFullYear(), curDate.getMonth() + 1, curDate.getDate())
    );
  };
  const decreaseMonth = () => {
    setCurDate(
      new Date(curDate.getFullYear(), curDate.getMonth() - 1, curDate.getDate())
    );
  };
  return (
    <div>
      <MyHeader
        headText={headText}
        leftChild={<MyButton text={"<"} onClick={decreaseMonth} />}
        rightChild={<MyButton text={">"} onClick={increaseMonth} />}
      />
      <DiaryList diaryList={data} />
      <MyFooter />
      <Paging />
    </div>
  );
};
export default Home;
