import {useContext, useEffect, useState} from "react";
import { useNavigate, useParams } from "react-router-dom"
import { DiaryStateContext } from "../App";
import DiaryEditor from "../components/DiaryEditor";

const Edit = () =>{
  const [originData,setOriginData] = useState();
  //useNavigate를 통해 페이지를 이동
  const navigate = useNavigate();
  const {id} = useParams();
  const diaryList = useContext(DiaryStateContext);

  useEffect(()=>{
    const titleElem = document.getElementsByTagName("title")[0];
    titleElem.innerHTML = `감성 일기장 - ${id}번 일기 수정`;
  },[])

  useEffect(() => {
    if (diaryList.length >= 1){
      const targetDiary = diaryList.find(
          (it) => parseInt(it.id) === parseInt(id));

      if(targetDiary){
        setOriginData(targetDiary);
      }else{
        navigate('/',{replace:true});
      }
    }
  },[diaryList,id]);

  return (
      <div>
        {originData && <DiaryEditor isEdit={true} originData={originData}/>}
      </div>
  )
}

export default Edit


//비구조화 할당
//파라미터 부분 이름은 변경되도 무관
// const [searchParams,setSearchParams] = useSearchParams();

//searchParams을 통해 query string으로 들어오는 값을 받고
//setSearchParams으로 경로를 변경할 수 있음
//QS 바꾸기 클릭 시
// const id = searchParams.get('id');
// const mode = searchParams.get('mode');